package dev.stiebo.openapi_generator_sample.repository;

import dev.stiebo.openapi_generator_sample.domain.SuspiciousIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuspiciousIpRepository extends JpaRepository<SuspiciousIp,Long> {
    Boolean existsByIp (String ip);
    Optional<SuspiciousIp> findByIp (String ip);
    List<SuspiciousIp> findAllByOrderByIdAsc();
}
