package dev.stiebo.openapi_generator_sample.mapper;

import dev.stiebo.openapi_generator_sample.domain.StolenCard;
import dev.stiebo.openapi_generator_sample.domain.SuspiciousIp;
import dev.stiebo.openapi_generator_sample.domain.Transaction;
import dev.stiebo.openapi_generator_sample.model.*;
import org.springframework.stereotype.Component;

@Component
public class AntiFraudMapper {
    public Transaction toTransaction(PostTransactionInDto postTransactionInDto) {
        return new Transaction()
                .setAmount(postTransactionInDto.getAmount())
                .setIp(postTransactionInDto.getIp())
                .setNumber(postTransactionInDto.getNumber())
                .setRegion(postTransactionInDto.getRegion())
                .setDate(postTransactionInDto.getDate());
    }

    public TransactionOutDto toDto(Transaction transaction) {
        return new TransactionOutDto()
                .transactionId(transaction.getId())
                .amount(transaction.getAmount())
                .ip(transaction.getIp())
                .number(transaction.getNumber())
                .region(transaction.getRegion())
                .date(transaction.getDate())
                .result(transaction.getResult())
                .feedback(transaction.getFeedback());
    }

    public SuspiciousIp toSuspisiousIp(SuspiciousIpInDto suspiciousIPInDto) {
        return new SuspiciousIp()
                .setIp(suspiciousIPInDto.getIp());
    }

    public SuspiciousIpOutDto toDto(SuspiciousIp suspiciousIP) {
        return new SuspiciousIpOutDto()
                .id(suspiciousIP.getId())
                .ip(suspiciousIP.getIp());
    }

    public StolenCard toStolenCard(StolenCardInDto stolenCardInDto) {
        return new StolenCard()
                .setNumber(stolenCardInDto.getNumber());
    }

    public StolenCardOutDto toDto(StolenCard stolenCard) {
        return new StolenCardOutDto()
                .id(stolenCard.getId())
                .number(stolenCard.getNumber());
    }
}
