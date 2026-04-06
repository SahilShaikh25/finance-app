package com.zorvyn.financebackend.repository;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zorvyn.financebackend.entity.FinancialRecord;
import com.zorvyn.financebackend.enums.TransactionType;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    // Find active (not soft deleted) record by id
    @Query("SELECT r FROM FinancialRecord r WHERE r.id = :id AND r.deletedAt IS NULL")
    Optional<FinancialRecord> findActiveById(@Param("id") Long id);

    // Filtered paginated listing
    @Query("""
            SELECT r FROM FinancialRecord r
            WHERE r.deletedAt IS NULL
            AND (:type IS NULL OR r.type = :type)
            AND (:category IS NULL OR LOWER(r.category) = LOWER(:category))
            AND (:from IS NULL OR r.date >= :from)
            AND (:to IS NULL OR r.date <= :to)
            """)
    Page<FinancialRecord> findAllWithFilters(
            @Param("type") TransactionType type,
            @Param("category") String category,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            Pageable pageable
    );

    // Dashboard: total income
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinancialRecord r WHERE r.type = 'INCOME' AND r.deletedAt IS NULL")
    BigDecimal getTotalIncome();

    // Dashboard: total expenses
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinancialRecord r WHERE r.type = 'EXPENSE' AND r.deletedAt IS NULL")
    BigDecimal getTotalExpenses();

    // Dashboard: category-wise totals
    @Query("SELECT r.category, r.type, SUM(r.amount) FROM FinancialRecord r WHERE r.deletedAt IS NULL GROUP BY r.category, r.type")
    List<Object[]> getCategoryTotals();

    // Dashboard: monthly trends (last 12 months)
    @Query(value = """
            SELECT DATE_FORMAT(date, '%Y-%m') AS month,
                   type,
                   SUM(amount) AS total
            FROM financial_records
            WHERE deleted_at IS NULL
              AND date >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH)
            GROUP BY month, type
            ORDER BY month ASC
            """, nativeQuery = true)
    List<Object[]> getMonthlyTrends();

    // Dashboard: recent records
    @Query("SELECT r FROM FinancialRecord r WHERE r.deletedAt IS NULL ORDER BY r.createdAt DESC")
    List<FinancialRecord> findRecentRecords(Pageable pageable);
}
