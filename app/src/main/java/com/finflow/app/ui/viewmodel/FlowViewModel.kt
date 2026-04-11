package com.finflow.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finflow.app.data.repository.FlowRepository
import com.finflow.app.domain.model.flow.MonthFlow
import com.finflow.app.domain.model.flow.expense.BankAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class FlowViewModel @Inject constructor(
    private val repository: FlowRepository,
) : ViewModel() {

    private val _currentYearMonth = MutableStateFlow(YearMonth.now())
    val currentYearMonth: StateFlow<YearMonth> = _currentYearMonth.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val monthFlow: StateFlow<MonthFlow> = _currentYearMonth
        .flatMapLatest { ym ->
            repository.getMonthFlow(ym.year, ym.monthValue)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MonthFlow())

    val bankAccounts: StateFlow<List<BankAccount>> = repository.getAllBankAccounts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun moveToPreviousMonth() {
        _currentYearMonth.value = _currentYearMonth.value.minusMonths(1)
    }

    fun moveToNextMonth() {
        _currentYearMonth.value = _currentYearMonth.value.plusMonths(1)
    }

    val currentMonthLabel: String
        get() {
            val ym = _currentYearMonth.value
            return "${ym.year}년 ${ym.monthValue}월"
        }
}
