package com.zorvyn.financebackend.service;



import java.util.List;

import com.zorvyn.financebackend.dto.response.DashboardSummaryResponse;
import com.zorvyn.financebackend.dto.response.MonthlyTrendResponse;

public interface DashboardService {
    DashboardSummaryResponse getSummary();
    List<MonthlyTrendResponse> getMonthlyTrends();
}
