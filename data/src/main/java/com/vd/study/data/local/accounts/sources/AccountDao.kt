package com.vd.study.data.local.accounts.sources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vd.study.data.local.LocalDatabaseCore
import com.vd.study.data.local.accounts.entities.AccountDataEntity

@Dao
interface AccountDao {

    @Insert
    suspend fun writeAccount(account: AccountDataEntity): Long

    @Query("DELETE FROM ${LocalDatabaseCore.ACCOUNTS_TABLE_NAME} WHERE id = :accountId")
    suspend fun removeAccount(accountId: Int): Int

    @Update
    suspend fun updateAccount(account: AccountDataEntity): Int

    @Query("SELECT * FROM ${LocalDatabaseCore.ACCOUNTS_TABLE_NAME}")
    suspend fun readAccounts(): List<AccountDataEntity>

    @Query("SELECT * FROM ${LocalDatabaseCore.ACCOUNTS_TABLE_NAME} WHERE email = :email LIMIT 1")
    suspend fun readAccount(email: String): AccountDataEntity?

    @Query("SELECT * FROM ${LocalDatabaseCore.ACCOUNTS_TABLE_NAME} WHERE id = :id LIMIT 1")
    suspend fun readAccountById(id: Int): AccountDataEntity?

    @Query("SELECT id FROM ${LocalDatabaseCore.ACCOUNTS_TABLE_NAME} WHERE email = :email AND password = :password LIMIT 1")
    suspend fun readAccountIdByEmailAndPassword(email: String, password: String): Int?

    @Query("DELETE FROM ${LocalDatabaseCore.ACCOUNTS_TABLE_NAME} WHERE email = :email")
    suspend fun deleteAccountByEmail(email: String): Int
}
