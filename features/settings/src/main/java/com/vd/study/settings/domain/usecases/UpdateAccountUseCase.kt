package com.vd.study.settings.domain.usecases

import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.core.container.Result
import com.vd.study.core.entities.MIN_PASSWORD_LENGTH
import com.vd.study.settings.domain.entities.AccountEntity
import com.vd.study.settings.domain.entities.AccountEntityFields
import com.vd.study.settings.domain.exceptions.EmptyFieldException
import com.vd.study.settings.domain.exceptions.IncorrectEmailFormatException
import com.vd.study.settings.domain.exceptions.IncorrectPasswordLengthException
import com.vd.study.settings.domain.repositories.SettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Named

class UpdateAccountUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: SettingsRepository,
    @Named("EMAIL_PATTERN") private val emailPattern: Pattern
) {

    suspend operator fun invoke(account: AccountEntity): Result<Boolean> = withContext(ioDispatcher) {
        if (account.username.isBlank()) throw EmptyFieldException(AccountEntityFields.USERNAME)
        if (account.email.isBlank()) throw EmptyFieldException(AccountEntityFields.EMAIL)
        if (account.password.isBlank()) throw EmptyFieldException(AccountEntityFields.PASSWORD)

        if (!emailPattern.matcher(account.email).matches()) throw IncorrectEmailFormatException()
        if (account.password.length < MIN_PASSWORD_LENGTH) throw IncorrectPasswordLengthException()

        return@withContext repository.updateAccount(account)
    }
}