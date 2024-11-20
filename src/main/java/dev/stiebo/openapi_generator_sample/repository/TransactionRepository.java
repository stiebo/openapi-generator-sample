package dev.stiebo.openapi_generator_sample.repository;

import dev.stiebo.openapi_generator_sample.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT COUNT(DISTINCT t.region) " +
            "FROM Transaction t " +
            "WHERE t.date BETWEEN :startDateTime AND :endDateTime " +
            "AND t.region <> :currentRegion")
    Long countDistinctRegionsInPeriodExcludingCurrentRegion(@Param("startDateTime") OffsetDateTime startDateTime,
                                                           @Param("endDateTime") OffsetDateTime endDateTime,
                                                           @Param("currentRegion") String currentRegion);

    @Query("SELECT COUNT(DISTINCT t.ip) " +
            "FROM Transaction t " +
            "WHERE t.date BETWEEN :startDateTime AND :endDateTime " +
            "AND t.ip <> :currentIp")
    Long countDistinctIpsInPeriodExcludingCurrentIp(@Param("startDateTime") OffsetDateTime startDateTime,
                                                      @Param("endDateTime") OffsetDateTime endDateTime,
                                                      @Param("currentIp") String currentIp);
    List<Transaction> findAllByOrderByIdAsc();
    List<Transaction> findAllByNumberOrderByIdAsc(String number);
}
