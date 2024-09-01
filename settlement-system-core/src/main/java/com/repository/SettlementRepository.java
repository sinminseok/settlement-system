package com.repository;

import com.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    @Query("SELECT s FROM Settlement s WHERE s.shopId = :shopId AND FUNCTION('DATE', s.settlementDateTime) = :date")
    Optional<Settlement> findByShopIdAndSettlementDate(@Param("shopId") Long shopId, @Param("date") LocalDate date);

}
