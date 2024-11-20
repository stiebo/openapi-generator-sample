package dev.stiebo.openapi_generator_sample.service;

import dev.stiebo.openapi_generator_sample.model.*;

import java.util.List;

public interface AntiFraudService {
    PostTransactionOutDto postTransaction(PostTransactionInDto postTransactionInDto);

    TransactionOutDto updateTransactionFeedback(UpdateTransactionFeedback feedback);

    List<TransactionOutDto> getTransactionHistory();

    List<TransactionOutDto> getTransactionHistoryByNumber(String number);

    SuspiciousIpOutDto postSuspiciousIp(SuspiciousIpInDto suspiciousIpInDto);

    void deleteSuspiciousIp(String ip);

    List<SuspiciousIpOutDto> getSuspiciousIps();

    StolenCardOutDto postStolenCard(StolenCardInDto stolenCardInDto);

    void deleteStolenCard(String number);

    List<StolenCardOutDto> getStolenCards();
}
