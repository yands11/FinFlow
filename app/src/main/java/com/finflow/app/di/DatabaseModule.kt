package com.finflow.app.di

import android.content.Context
import androidx.room.Room
import com.finflow.app.data.local.AppDatabase
import com.finflow.app.data.local.dao.BankAccountDao
import com.finflow.app.data.local.dao.CashIncomeDao
import com.finflow.app.data.local.dao.ExpenseCategoryDao
import com.finflow.app.data.local.dao.ExpenseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "finflow.db"
        ).build()
    }

    @Provides
    fun provideBankAccountDao(db: AppDatabase): BankAccountDao = db.bankAccountDao()

    @Provides
    fun provideExpenseCategoryDao(db: AppDatabase): ExpenseCategoryDao = db.expenseCategoryDao()

    @Provides
    fun provideCashIncomeDao(db: AppDatabase): CashIncomeDao = db.cashIncomeDao()

    @Provides
    fun provideExpenseDao(db: AppDatabase): ExpenseDao = db.expenseDao()
}
