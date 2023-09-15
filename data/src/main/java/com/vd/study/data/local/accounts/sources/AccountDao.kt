package com.vd.study.data.local.accounts.sources

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vd.study.data.local.LocalDatabaseCore
import com.vd.study.data.local.accounts.entities.AccountDataEntity

@Dao
interface AccountDao {

    @Insert
    suspend fun writeAccount(account: AccountDataEntity): Long

    @Delete
    suspend fun removeAccount(account: AccountDataEntity): Int

    @Update
    suspend fun updateAccount(account: AccountDataEntity): Int

    @Query("SELECT * FROM ${LocalDatabaseCore.ACCOUNTS_TABLE_NAME}")
    suspend fun readAccounts(): List<AccountDataEntity>

    @Query("SELECT * FROM ${LocalDatabaseCore.ACCOUNTS_TABLE_NAME} WHERE email = :email LIMIT 1")
    suspend fun readAccount(email: String): AccountDataEntity?
}