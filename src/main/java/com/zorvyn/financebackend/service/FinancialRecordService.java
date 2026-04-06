package com.zorvyn.financebackend.service;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;

import com.zorvyn.financebackend.dto.request.FinancialRecordRequest;
import com.zorvyn.financebackend.dto.response.FinancialRecordResponse;
import com.zorvyn.financebackend.dto.response.PageResponse;
import com.zorvyn.financebackend.enums.TransactionType;

public interface FinancialRecordService {

    FinancialRecordResponse createRecord(FinancialRecordRequest request, String userEmail);

    PageResponse<FinancialRecordResponse> getAllRecords(
            TransactionType type,
            String category,
            LocalDate from,
            LocalDate to,
            Pageable pageable
    );

    FinancialRecordResponse getRecordById(Long id);

    FinancialRecordResponse updateRecord(Long id, FinancialRecordRequest request, String userEmail);

    void deleteRecord(Long id);
}
