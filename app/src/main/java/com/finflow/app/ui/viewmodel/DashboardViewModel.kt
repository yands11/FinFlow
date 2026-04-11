package com.finflow.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finflow.app.data.repository.FlowRepository
import com.finflow.app.ui.model.MonthStatUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    repository: FlowRepository,
) : ViewModel() {

    val monthlyStats: StateFlow<List<MonthStatUiModel>> = repository.getMonthlyStats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
