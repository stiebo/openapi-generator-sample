package dev.stiebo.openapi_generator_sample.repository;

import dev.stiebo.openapi_generator_sample.domain.StolenCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StolenCardRepository extends JpaRepository<StolenCard,Long> {
    Boolean existsByNumber (String ip);
    Optional<StolenCard> findByNumber (String number);
    List<StolenCard> findAllByOrderByIdAsc();
}
