package dev.stiebo.openapi_generator_sample.apiDelegateImpl;

import dev.stiebo.openapi_generator_sample.api.ClearDataControllerApiDelegate;
import dev.stiebo.openapi_generator_sample.exception.ClearDataErrorException;
import dev.stiebo.openapi_generator_sample.model.ClearData200Response;
import dev.stiebo.openapi_generator_sample.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClearDataControllerApiDelegateImpl implements ClearDataControllerApiDelegate {
    private final StolenCardRepository stolenCardRepository;
    private final SuspiciousIpRepository suspiciousIpRepository;
    private final TransactionLimitRepository transactionLimitRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Autowired
    public ClearDataControllerApiDelegateImpl(StolenCardRepository stolenCardRepository,
                                              SuspiciousIpRepository suspiciousIpRepository,
                                              TransactionLimitRepository transactionLimitRepository,
                                              TransactionRepository transactionRepository,
                                              UserRepository userRepository) {
        this.stolenCardRepository = stolenCardRepository;
        this.suspiciousIpRepository = suspiciousIpRepository;
        this.transactionLimitRepository = transactionLimitRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<ClearData200Response> clearData() {
        try {
            stolenCardRepository.deleteAll();
            suspiciousIpRepository.deleteAll();
            transactionLimitRepository.deleteAll();
            transactionRepository.deleteAll();
            userRepository.deleteAll();
        } catch (Exception e) {
            throw new ClearDataErrorException();
        }
        return ResponseEntity.ok(new ClearData200Response().status("Demo server has been reset and all data removed."));
    }
}
