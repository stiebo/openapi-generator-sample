package dev.stiebo.openapi_generator_sample.apiDelegateImpl;

import dev.stiebo.openapi_generator_sample.api.ClearDataControllerApiDelegate;
import dev.stiebo.openapi_generator_sample.model.ClearData200Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClearDataControllerApiDelegateImpl implements ClearDataControllerApiDelegate {

    @Override
    public ResponseEntity<ClearData200Response> clearData() {
        return ResponseEntity.ok(new ClearData200Response().status("Demo server has been reset and all data removed."));
    }
}
