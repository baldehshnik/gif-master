package com.vd.study.sign_up.domain.usecases

import com.vd.study.core.container.Result
import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.core.entities.MIN_PASSWORD_LENGTH
import com.vd.study.sign_up.domain.entities.AccountEntity
import com.vd.study.sign_up.domain.entities.AccountRegistrationFields
import com.vd.study.sign_up.domain.exceptions.EmptyFieldException
import com.vd.study.sign_up.domain.exceptions.IncorrectEmailFormatException
import com.vd.study.sign_up.domain.exceptions.ShortPasswordException
import com.vd.study.sign_up.domain.repositories.RegistrationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Named

class RegisterAccountUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: RegistrationRepository,
    @Named("EMAIL_PATTERN") private val emailPattern: Pattern
) {

    suspend operator fun invoke(account: AccountEntity): Result<Boolean> = withContext(ioDispatcher) {
        if (account.username.isBlank()) throw EmptyFieldException(AccountRegistrationFields.USERNAME)
        if (account.email.isBlank()) throw EmptyFieldException(AccountRegistrationFields.EMAIL)
        if (account.password.isBlank()) throw EmptyFieldException(AccountRegistrationFields.PASSWORD)

        if (!emailPattern.matcher(account.email).matches()) throw IncorrectEmailFormatException()
        if (account.password.length < MIN_PASSWORD_LENGTH) throw ShortPasswordException()

        return@withContext repository.registerAccount(account)
    }
}