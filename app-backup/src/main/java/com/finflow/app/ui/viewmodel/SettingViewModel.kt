package com.finflow.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finflow.app.data.repository.FlowRepository
import com.finflow.app.domain.model.flow.expense.BankAccount
import com.finflow.app.domain.model.flow.expense.ExpenseCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: FlowRepository,
) : ViewModel() {

    val bankAccounts: StateFlow<List<BankAccount>> = repository.getAllBankAccounts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<ExpenseCategory>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addBankAccount(name: String, bankName: String, accountNumber: String, ownerName: String) {
        viewModelScope.launch {
            repository.addBankAccount(name, bankName, accountNumber, ownerName, "")
        }
    }

    fun updateBankAccount(id: Long, name: String, bankName: String, accountNumber: String, ownerName: String) {
        viewModelScope.launch {
            repository.updateBankAccount(id, name, bankName, accountNumber, ownerName)
        }
    }

    fun deleteBankAccount(id: Long) {
        viewModelScope.launch {
            repository.deleteBankAccount(id)
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            repository.addCategory(name)
        }
    }

    fun updateCategory(id: Long, name: String) {
        viewModelScope.launch {
            repository.updateCategory(id, name)
        }
    }

    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            repository.deleteCategory(id)
        }
    }
}
