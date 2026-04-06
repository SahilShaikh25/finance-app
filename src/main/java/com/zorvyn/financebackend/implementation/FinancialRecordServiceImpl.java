package com.zorvyn.financebackend.implementation;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zorvyn.financebackend.dto.request.FinancialRecordRequest;
import com.zorvyn.financebackend.dto.response.FinancialRecordResponse;
import com.zorvyn.financebackend.dto.response.PageResponse;
import com.zorvyn.financebackend.entity.FinancialRecord;
import com.zorvyn.financebackend.entity.User;
import com.zorvyn.financebackend.enums.TransactionType;
import com.zorvyn.financebackend.exception.ResourceNotFoundException;
import com.zorvyn.financebackend.repository.FinancialRecordRepository;
import com.zorvyn.financebackend.repository.UserRepository;
import com.zorvyn.financebackend.service.FinancialRecordService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FinancialRecordServiceImpl implements FinancialRecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public FinancialRecordResponse createRecord(FinancialRecordRequest request, String userEmail) {
        User user = getUserByEmail(userEmail);

        FinancialRecord record = FinancialRecord.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory().trim())
                .date(request.getDate())
                .notes(request.getNotes())
                .createdBy(user)
                .build();

        return FinancialRecordResponse.from(recordRepository.save(record));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FinancialRecordResponse> getAllRecords(
            TransactionType type,
            String category,
            LocalDate from,
            LocalDate to,
            Pageable pageable
    ) {
        Page<FinancialRecordResponse> page = recordRepository
                .findAllWithFilters(type, category, from, to, pageable)
                .map(FinancialRecordResponse::from);

        return PageResponse.from(page);
    }

    @Override
    @Transactional(readOnly = true)
    public FinancialRecordResponse getRecordById(Long id) {
        FinancialRecord record = getActiveRecord(id);
        return FinancialRecordResponse.from(record);
    }

    @Override
    @Transactional
    public FinancialRecordResponse updateRecord(Long id, FinancialRecordRequest request, String userEmail) {
        FinancialRecord record = getActiveRecord(id);

        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory().trim());
        record.setDate(request.getDate());
        record.setNotes(request.getNotes());

        return FinancialRecordResponse.from(recordRepository.save(record));
    }

    @Override
    @Transactional
    public void deleteRecord(Long id) {
        FinancialRecord record = getActiveRecord(id);
        record.setDeletedAt(LocalDateTime.now());
        recordRepository.save(record);
    }

    // ── helpers ────────────────────────────────────────────────────────────────

    private FinancialRecord getActiveRecord(Long id) {
        return recordRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Financial record not found with id: " + id));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
