package com.vd.study.sign_in.domain.usecases

import com.vd.study.core.container.Result
import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.sign_in.domain.entities.AccountEntityFields
import com.vd.study.sign_in.domain.entities.CheckAccountEntity
import com.vd.study.sign_in.domain.exceptions.EmptyFieldException
import com.vd.study.sign_in.domain.exceptions.IncorrectEmailFormatException
import com.vd.study.sign_in.domain.repositories.SignInRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Named

class IsAccountExistsAndCorrectUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: SignInRepository,
    @Named("EMAIL_PATTERN") private val emailPattern: Pattern
) {

    suspend operator fun invoke(account: CheckAccountEntity): Result<Boolean> = withContext(ioDispatcher) {
        if (account.email.isBlank()) throw EmptyFieldException(AccountEntityFields.EMAIL)
        if (account.password.isBlank()) throw EmptyFieldException(AccountEntityFields.PASSWORD)

        if (!emailPattern.matcher(account.email).matches()) throw IncorrectEmailFormatException()

        return@withContext repository.isAccountExistsAndCorrect(account)
    }
}