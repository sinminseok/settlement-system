package com.domain.settlement.repository;

import com.domain.settlement.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, UUID>, CustomSettlementRepository {

//    @Query("SELECT s FROM Settlement s WHERE s.shopId = :shopId AND FUNCTION('DATE', s.settlementDateTime) = :date")
//    Optional<Settlement> findByShopIdAndSettlementDate(@Param("shopId") Long shopId, @Param("date") LocalDate date);

}
