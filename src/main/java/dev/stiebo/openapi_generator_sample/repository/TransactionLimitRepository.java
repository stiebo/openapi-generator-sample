package dev.stiebo.openapi_generator_sample.repository;

import dev.stiebo.openapi_generator_sample.domain.TransactionLimit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionLimitRepository extends JpaRepository<TransactionLimit,Long> {
}
