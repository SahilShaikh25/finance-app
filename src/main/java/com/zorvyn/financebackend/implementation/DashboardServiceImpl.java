package com.zorvyn.financebackend.implementation;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zorvyn.financebackend.dto.response.DashboardSummaryResponse;
import com.zorvyn.financebackend.dto.response.DashboardSummaryResponse.CategoryTotal;
import com.zorvyn.financebackend.dto.response.FinancialRecordResponse;
import com.zorvyn.financebackend.dto.response.MonthlyTrendResponse;
import com.zorvyn.financebackend.repository.FinancialRecordRepository;
import com.zorvyn.financebackend.service.DashboardService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final FinancialRecordRepository recordRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary() {
        BigDecimal totalIncome   = recordRepository.getTotalIncome();
        BigDecimal totalExpenses = recordRepository.getTotalExpenses();
        BigDecimal netBalance    = totalIncome.subtract(totalExpenses);

        List<CategoryTotal> categoryTotals = recordRepository.getCategoryTotals()
                .stream()
                .map(row -> CategoryTotal.builder()
                        .category((String) row[0])
                        .type(row[1].toString())
                        .total((BigDecimal) row[2])
                        .build())
                .toList();

        List<FinancialRecordResponse> recentRecords = recordRepository
                .findRecentRecords(PageRequest.of(0, 10))
                .stream()
                .map(FinancialRecordResponse::from)
                .toList();

        return DashboardSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(netBalance)
                .categoryTotals(categoryTotals)
                .recentRecords(recentRecords)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonthlyTrendResponse> getMonthlyTrends() {
        return recordRepository.getMonthlyTrends()
                .stream()
                .map(row -> MonthlyTrendResponse.builder()
                        .month((String) row[0])
                        .type((String) row[1])
                        .total(new BigDecimal(row[2].toString()))
                        .build())
                .toList();
    }
}