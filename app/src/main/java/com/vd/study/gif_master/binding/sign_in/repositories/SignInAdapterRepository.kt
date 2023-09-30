package com.vd.study.gif_master.binding.sign_in.repositories

import com.vd.study.core.container.Result
import com.vd.study.data.AccountsDataRepository
import com.vd.study.gif_master.binding.sign_in.mappers.AccountEntityMapper
import com.vd.study.gif_master.binding.sign_in.mappers.CheckAccountDataEntityMapper
import com.vd.study.sign_in.domain.entities.AccountEntity
import com.vd.study.sign_in.domain.entities.CheckAccountEntity
import com.vd.study.sign_in.domain.repositories.SignInRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignInAdapterRepository @Inject constructor(
    private val repository: AccountsDataRepository,
    private val accountEntityMapper: AccountEntityMapper,
    private val checkAccountDataEntityMapper: CheckAccountDataEntityMapper
) : SignInRepository {

    override suspend fun readAccounts(): Result<List<AccountEntity>> {
        return repository.readAccounts().suspendMap { items ->
            items.map(accountEntityMapper::map)
        }
    }

    override suspend fun isAccountExistsAndCorrect(account: CheckAccountEntity): Result<Boolean> {
        return repository.checkAccountExistence(checkAccountDataEntityMapper.map(account))
    }
}