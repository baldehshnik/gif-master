package com.vd.study.data.local.accounts

import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.data.AccountsDataRepository
import com.vd.study.data.exceptions.UnknownException
import com.vd.study.data.local.accounts.entities.AccountDataEntity
import com.vd.study.data.local.accounts.sources.AccountDao
import javax.inject.Inject
import com.vd.study.core.container.Result
import com.vd.study.data.exceptions.FailedInsertException
import com.vd.study.data.exceptions.FailedRemoveException
import com.vd.study.data.exceptions.FailedUpdateException
import com.vd.study.data.exceptions.NotFoundException
import com.vd.study.data.local.catchReadingExceptionOf
import com.vd.study.data.local.executeDatabaseUpdating
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Singleton
class AccountsDataRepositoryImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val accountDao: AccountDao
) : AccountsDataRepository {

    override suspend fun saveNewAccount(account: AccountDataEntity): Result<Boolean> =
        withContext(ioDispatcher) {
            return@withContext executeDatabaseUpdating(
                operation = {
                    accountDao.writeAccount(account)
                }, error = Result.Error(FailedInsertException())
            )
        }

    override suspend fun readAccounts(): Result<List<AccountDataEntity>> =
        withContext(ioDispatcher) {
            return@withContext catchReadingExceptionOf {
                accountDao.readAccounts()
            }
        }

    override suspend fun removeAccount(account: AccountDataEntity): Result<Boolean> =
        withContext(ioDispatcher) {
            return@withContext executeDatabaseUpdating(
                operation = {
                    accountDao.removeAccount(account).toLong()
                }, error = Result.Error(FailedRemoveException())
            )
        }

    override suspend fun updateAccount(account: AccountDataEntity): Result<Boolean> =
        withContext(ioDispatcher) {
            return@withContext executeDatabaseUpdating(
                operation = {
                    accountDao.updateAccount(account).toLong()
                }, error = Result.Error(FailedUpdateException())
            )
        }

    override suspend fun readAccount(email: String): Result<AccountDataEntity> =
        withContext(ioDispatcher) {
            return@withContext try {
                val accountEntity = accountDao.readAccount(email)
                if (accountEntity == null) {
                    Result.Error(NotFoundException())
                } else {
                    Result.Correct(accountEntity)
                }
            } catch (e: Exception) {
                Result.Error(UnknownException())
            }
        }
}