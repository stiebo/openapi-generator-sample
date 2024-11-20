package dev.stiebo.openapi_generator_sample.service.impl;

import dev.stiebo.openapi_generator_sample.domain.StolenCard;
import dev.stiebo.openapi_generator_sample.domain.SuspiciousIp;
import dev.stiebo.openapi_generator_sample.domain.Transaction;
import dev.stiebo.openapi_generator_sample.domain.TransactionLimit;
import dev.stiebo.openapi_generator_sample.exception.*;
import dev.stiebo.openapi_generator_sample.mapper.AntiFraudMapper;
import dev.stiebo.openapi_generator_sample.model.*;
import dev.stiebo.openapi_generator_sample.repository.StolenCardRepository;
import dev.stiebo.openapi_generator_sample.repository.SuspiciousIpRepository;
import dev.stiebo.openapi_generator_sample.repository.TransactionLimitRepository;
import dev.stiebo.openapi_generator_sample.repository.TransactionRepository;
import dev.stiebo.openapi_generator_sample.service.AntiFraudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AntiFraudServiceImpl implements AntiFraudService {
    TransactionRepository transactionRepository;
    SuspiciousIpRepository suspiciousIPRepository;
    StolenCardRepository stolenCardRepository;
    AntiFraudMapper mapper;
    TransactionLimitRepository transactionLimitRepository;
    TransactionLimit transactionLimit;

    @Autowired
    public AntiFraudServiceImpl(TransactionRepository transactionRepository,
                                SuspiciousIpRepository suspiciousIPRepository,
                                StolenCardRepository stolenCardRepository,
                                AntiFraudMapper mapper,
                                TransactionLimitRepository transactionLimitRepository,
                                @Qualifier("defaultMaxAllowed")
                                Long defaultMaxAllowed,
                                @Qualifier("defaultMaxManual")
                                Long defaultMaxManual) {
        this.transactionRepository = transactionRepository;
        this.suspiciousIPRepository = suspiciousIPRepository;
        this.stolenCardRepository = stolenCardRepository;
        this.mapper = mapper;
        this.transactionLimitRepository = transactionLimitRepository;
        transactionLimit = transactionLimitRepository.findById(1L)
                .orElseGet(() -> {
                    return transactionLimitRepository.save(new TransactionLimit(defaultMaxAllowed, defaultMaxManual));
                });
    }

    @Override
    public PostTransactionOutDto postTransaction(PostTransactionInDto postTransactionInDto) {
        String result;
        StringBuilder info = new StringBuilder();

        if (postTransactionInDto.getAmount() <= transactionLimit.getMaxAllowed()) {
            result = "ALLOWED";
            info.append("none");
        } else if (postTransactionInDto.getAmount() <= transactionLimit.getMaxManual()) {
            result = "MANUAL_PROCESSING";
            info.append("amount");
        } else {
            result = "PROHIBITED";
            info.append("amount");
        }

        if (stolenCardRepository.existsByNumber(postTransactionInDto.getNumber())) {
            if (result.equals("PROHIBITED")) {
                info.append(", ");
            } else {
                result = "PROHIBITED";
                info.setLength(0);
            }
            info.append("card-number");
        }

        if (suspiciousIPRepository.existsByIp(postTransactionInDto.getIp())) {
            if (result.equals("PROHIBITED")) {
                info.append(", ");
            } else {
                result = "PROHIBITED";
                info.setLength(0);
            }
            info.append("ip");
        }

        OffsetDateTime oneHourAgo = postTransactionInDto.getDate().minusHours(1);
        Long countTransactionsDiffRegion = transactionRepository.
                countDistinctRegionsInPeriodExcludingCurrentRegion(oneHourAgo, postTransactionInDto.getDate(),
                        postTransactionInDto.getRegion());

        if (countTransactionsDiffRegion > 2) {
            if (result.equals("PROHIBITED")) {
                info.append(", ");
            } else {
                result = "PROHIBITED";
                info.setLength(0);
            }
            info.append("region-correlation");
        } else if (countTransactionsDiffRegion == 2 && !result.equals("PROHIBITED")) {
            if (result.equals("MANUAL_PROCESSING")) {
                info.append(", ");
            } else {
                result = "MANUAL_PROCESSING";
                info.setLength(0);
            }
            info.append("region-correlation");
        }

        Long countTransactionUniqueDiffIp = transactionRepository
                .countDistinctIpsInPeriodExcludingCurrentIp(oneHourAgo, postTransactionInDto.getDate(),
                        postTransactionInDto.getIp());

        if (countTransactionUniqueDiffIp > 2) {
            if (result.equals("PROHIBITED")) {
                info.append(", ");
            } else {
                result = "PROHIBITED";
                info.setLength(0);
            }
            info.append("ip-correlation");
        } else if (countTransactionUniqueDiffIp == 2 && !result.equals("PROHIBITED")) {
            if (result.equals("MANUAL_PROCESSING")) {
                info.append(", ");
            } else {
                result = "MANUAL_PROCESSING";
                info.setLength(0);
            }
            info.append("ip-correlation");
        }

        Transaction newTransaction = mapper.toTransaction(postTransactionInDto)
                .setResult(result)
                .setFeedback("");
        transactionRepository.save(newTransaction);
        return new PostTransactionOutDto().result(result). info(info.toString());
    }

    @Override
    public TransactionOutDto updateTransactionFeedback(UpdateTransactionFeedback feedback) {
        Transaction transaction = transactionRepository.findById(feedback.getTransactionId())
                .orElseThrow(TransactionNotFoundException::new);
        if (!transaction.getFeedback().isEmpty()) {
            throw new TransactionFeedbackAlreadyExistsException();
        }

        // if validity equals feedback: exception
        if (transaction.getResult().equals(feedback.getFeedback())) {
            throw new TransactionFeedbackUnprocessableException();
        }

        // save feedback into db
        transaction.setFeedback(feedback.getFeedback());
        transactionRepository.save(transaction);

        // adjust limits
        if (transaction.getResult().equals("ALLOWED")) {
            // dec maxAllowed
            transactionLimit.setMaxAllowed(updateLimit(transactionLimit.getMaxAllowed(),
                    transaction.getAmount(), false));
            if (feedback.getFeedback().equals("PROHIBITED")) {
                // dec maxManual
                transactionLimit.setMaxManual(updateLimit(transactionLimit.getMaxManual(),
                        transaction.getAmount(), false));
            }
        } else if (transaction.getResult().equals("MANUAL_PROCESSING")) {
            if (feedback.getFeedback().equals("ALLOWED")) {
                // inc maxAllowed
                transactionLimit.setMaxAllowed(updateLimit(transactionLimit.getMaxAllowed(),
                        transaction.getAmount(), true));
            } else {
                // dec maxManual
                transactionLimit.setMaxManual(updateLimit(transactionLimit.getMaxManual(),
                        transaction.getAmount(), false));
            }
        } else {
            // inc Manual
            transactionLimit.setMaxManual(updateLimit(transactionLimit.getMaxManual(),
                    transaction.getAmount(), true));
            if (feedback.getFeedback().equals("ALLOWED")) {
                // inc maxAllowed
                transactionLimit.setMaxAllowed(updateLimit(transactionLimit.getMaxAllowed(),
                        transaction.getAmount(), true));
            }
        }

        // save new limit
        transactionLimit = transactionLimitRepository.save(transactionLimit);
        return mapper.toDto(transaction);
    }

    private Long updateLimit(Long currentLimit, Long transactionValue, Boolean increase) {
        return (long) Math.ceil((0.8 * currentLimit +
                (increase ? 0.2 * transactionValue : -0.2 * transactionValue)));
    }

    @Override
    public List<TransactionOutDto> getTransactionHistory() {
        List<Transaction> transactions = transactionRepository.findAllByOrderByIdAsc();
        return transactions.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<TransactionOutDto> getTransactionHistoryByNumber(String number) {
        List<Transaction> transactions = transactionRepository.findAllByNumberOrderByIdAsc(number);
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException();
        }
        return transactions.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public SuspiciousIpOutDto postSuspiciousIp(SuspiciousIpInDto suspiciousIpInDto) {
        if (suspiciousIPRepository.existsByIp(suspiciousIpInDto.getIp())) {
            throw new SuspiciousIpExistsException();
        }
        return mapper.toDto(suspiciousIPRepository.save(mapper.toSuspisiousIp(suspiciousIpInDto)));
    }

    @Override
    public void deleteSuspiciousIp(String ip) {
        SuspiciousIp suspiciousIp = suspiciousIPRepository.findByIp(ip)
                .orElseThrow(SuspiciousIpNotFoundException::new);
        suspiciousIPRepository.delete(suspiciousIp);
    }

    @Override
    public List<SuspiciousIpOutDto> getSuspiciousIps() {
        List<SuspiciousIp> suspiciousIps = suspiciousIPRepository.findAllByOrderByIdAsc();
        return suspiciousIps.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public StolenCardOutDto postStolenCard(StolenCardInDto stolenCardInDto) {
        if (stolenCardRepository.existsByNumber(stolenCardInDto.getNumber())) {
            throw new StolenCardExistsException();
        }
        return mapper.toDto(stolenCardRepository.save(mapper.toStolenCard(stolenCardInDto)));
    }

    @Override
    public void deleteStolenCard(String number) {
        StolenCard stolenCard = stolenCardRepository.findByNumber(number)
                .orElseThrow(StolenCardNotFoundException::new);
        stolenCardRepository.delete(stolenCard);
    }

    @Override
    public List<StolenCardOutDto> getStolenCards() {
        List<StolenCard> stolenCards = stolenCardRepository.findAllByOrderByIdAsc();
        return stolenCards.stream()
                .map(mapper::toDto)
                .toList();
    }
}
