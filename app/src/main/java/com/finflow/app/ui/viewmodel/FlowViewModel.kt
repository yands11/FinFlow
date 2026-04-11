package com.finflow.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finflow.app.data.repository.FlowRepository
import com.finflow.app.domain.model.flow.MonthFlow
import com.finflow.app.domain.model.flow.expense.BankAccount
import com.finflow.app.domain.model.flow.expense.ExpenseCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    val categories: StateFlow<List<ExpenseCategory>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun moveToPreviousMonth() {
        _currentYearMonth.value = _currentYearMonth.value.minusMonths(1)
    }

    fun moveToNextMonth() {
        _currentYearMonth.value = _currentYearMonth.value.plusMonths(1)
    }

    fun addIncome(title: String, amount: Long) {
        val ym = _currentYearMonth.value
        viewModelScope.launch {
            repository.addIncome(title, amount, ym.year, ym.monthValue)
        }
    }

    fun addExpense(type: String, categoryId: Long, name: String, amount: Long, bankAccountId: Long) {
        val ym = _currentYearMonth.value
        viewModelScope.launch {
            repository.addExpense(type, categoryId, name, amount, bankAccountId, ym.year, ym.monthValue)
        }
    }

    fun addCategory(name: String, onCreated: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.addCategory(name)
            onCreated(id)
        }
    }

    fun addBankAccount(
        name: String,
        bankName: String,
        accountNumber: String,
        ownerName: String,
        onCreated: (Long) -> Unit,
    ) {
        viewModelScope.launch {
            val id = repository.addBankAccount(name, bankName, accountNumber, ownerName, "")
            onCreated(id)
        }
    }

    fun updateIncome(id: Long, title: String, amount: Long) {
        viewModelScope.launch {
            repository.updateIncome(id, title, amount)
        }
    }

    fun updateExpense(id: Long, categoryId: Long, name: String, amount: Long, bankAccountId: Long) {
        viewModelScope.launch {
            repository.updateExpense(id, categoryId, name, amount, bankAccountId)
        }
    }

    fun deleteIncome(id: Long) {
        viewModelScope.launch {
            repository.deleteIncome(id)
        }
    }

    fun deleteExpense(id: Long) {
        viewModelScope.launch {
            repository.deleteExpense(id)
        }
    }
}
