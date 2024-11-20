package dev.stiebo.openapi_generator_sample.apiDelegateImpl;

import dev.stiebo.openapi_generator_sample.api.AntiFraudControllerApiDelegate;
import dev.stiebo.openapi_generator_sample.model.*;
import dev.stiebo.openapi_generator_sample.service.AntiFraudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AntiFraudControllerApiDelegateImpl implements AntiFraudControllerApiDelegate {
    private final AntiFraudService antiFraudService;

    @Autowired
    public AntiFraudControllerApiDelegateImpl(AntiFraudService antiFraudService) {
        this.antiFraudService = antiFraudService;
    }

    @Override
    public ResponseEntity<DeleteStolenCard200Response> deleteStolenCard(String number) {
        antiFraudService.deleteStolenCard(number);
        return ResponseEntity.ok(new DeleteStolenCard200Response()
                .status("Card %s successfully removed!".formatted(number)));
    }

    @Override
    public ResponseEntity<DeleteSuspiciousIp200Response> deleteSuspiciousIp(String ip) {
        antiFraudService.deleteSuspiciousIp(ip);
        return ResponseEntity.ok(new DeleteSuspiciousIp200Response()
                .status("IP %s successfully removed!".formatted(ip)));
    }

    @Override
    public ResponseEntity<List<StolenCardOutDto>> getStolenCards() {
        return ResponseEntity.ok(antiFraudService.getStolenCards());
    }

    @Override
    public ResponseEntity<List<SuspiciousIpOutDto>> getSuscipiousIps() {
        return ResponseEntity.ok(antiFraudService.getSuspiciousIps());
    }

    @Override
    public ResponseEntity<List<TransactionOutDto>> getTransactionHistory() {
        return ResponseEntity.ok(antiFraudService.getTransactionHistory());
    }

    @Override
    public ResponseEntity<List<TransactionOutDto>> getTransactionHistoryByNumber(String number) {
        return ResponseEntity.ok(antiFraudService.getTransactionHistoryByNumber(number));
    }

    @Override
    public ResponseEntity<StolenCardOutDto> postStolenCard(StolenCardInDto stolenCardInDto) {
        return ResponseEntity.ok(antiFraudService.postStolenCard(stolenCardInDto));
    }

    @Override
    public ResponseEntity<SuspiciousIpOutDto> postSuspiciousIp(SuspiciousIpInDto suspiciousIpInDto) {
        return ResponseEntity.ok(antiFraudService.postSuspiciousIp(suspiciousIpInDto));
    }

    @Override
    public ResponseEntity<PostTransactionOutDto> postTransaction(PostTransactionInDto postTransactionInDto) {
        return ResponseEntity.ok(antiFraudService.postTransaction(postTransactionInDto));
    }

    @Override
    public ResponseEntity<TransactionOutDto> uploadTransactionFeedback(UpdateTransactionFeedback updateTransactionFeedback) {
        return ResponseEntity.ok(antiFraudService.updateTransactionFeedback(updateTransactionFeedback));
    }
}
