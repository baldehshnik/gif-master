package com.vd.study.data.local.accounts

import android.database.sqlite.SQLiteConstraintException
import androidx.test.filters.SmallTest
import com.vd.study.data.local.LocalDatabase
import com.vd.study.data.local.accounts.entities.AccountDataEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class AccountDaoTest {

    @get:Rule
    var rule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var database: LocalDatabase

    @Before
    fun initRoomDatabase() {
        rule.inject()
    }

    @After
    fun closeRoomDatabase() {
        database.close()
    }

    @Test
    fun readAccountIdByEmailAndPassword_AfterReading_ReturnOne() = runTest {
        val accountDao = database.accountDao
        val entities = createAccountEntities(1)
        val accountEntity = entities[0]

        val insertedAccountId = accountDao.writeAccount(accountEntity)
        val accountIdAfterReading = accountDao.readAccountIdByEmailAndPassword(
            accountEntity.email,
            accountEntity.password
        )

        assertEquals(1, insertedAccountId)
        assertEquals(1, accountIdAfterReading)
    }

    @Test
    fun readAccountIdByEmailAndPassword_AfterReading_ReturnNegativeOneInvalidEmail() = runTest {
        val accountDao = database.accountDao
        val entities = createAccountEntities(1)
        val accountEntity = entities[0]

        val insertedAccountId = accountDao.writeAccount(accountEntity)
        val accountIdAfterReading = accountDao.readAccountIdByEmailAndPassword(
            "invalid_email@example.com",
            accountEntity.password
        )

        assertEquals(1, insertedAccountId)
        assertEquals(null, accountIdAfterReading)
    }

    @Test
    fun readAccountIdByEmailAndPassword_AfterReading_ReturnNegativeOneInvalidPassword() = runTest {
        val accountDao = database.accountDao
        val entities = createAccountEntities(1)
        val accountEntity = entities[0]

        val insertedAccountId = accountDao.writeAccount(accountEntity)
        val accountIdAfterReading = accountDao.readAccountIdByEmailAndPassword(
            accountEntity.email,
            "incorrect_password"
        )

        assertEquals(1, insertedAccountId)
        assertEquals(null, accountIdAfterReading)
    }

    @Test
    fun readAccountIdByEmailAndPassword_AfterReading_ReturnNegativeOneBecauseNonExistentAccount() = runTest {
        val accountDao = database.accountDao

        val accountIdAfterReading = accountDao.readAccountIdByEmailAndPassword(
            "email@example.com",
            "password"
        )

        assertEquals(null, accountIdAfterReading)
    }

    @Test
    fun addAccount_afterAdding_ReturnIdOfInsertedAccount() = runTest {
        val accountDao = database.accountDao
        val entities = createAccountEntities(1)
        val accountEntity = entities[0]

        val insertedId = accountDao.writeAccount(accountEntity)

        assertEquals(1, insertedId)
    }

    @Test
    fun addAccounts_afterAdding5Accounts_ReturnIdsOf5InsertedAccounts() = runTest {
        val accountDao = database.accountDao
        val entities = createAccountEntities(5)

        val ids = mutableListOf<Long>()
        entities.forEach {
            ids += accountDao.writeAccount(it)
        }

        assertEquals(5, ids.size)
        assertEquals(1, ids[0])
        assertEquals(2, ids[1])
        assertEquals(3, ids[2])
        assertEquals(4, ids[3])
        assertEquals(5, ids[4])
    }

    @Test(expected = SQLiteConstraintException::class)
    fun addAccount_afterAddingAccountWithEmailThatIsInDatabase_ThrowSQLiteConstraintException() =
        runTest {
            val accountDao = database.accountDao
            val firstEntity = createAccountEntities(1)[0]
            val secondEntity = firstEntity.copy()

            accountDao.writeAccount(firstEntity)
            accountDao.writeAccount(secondEntity)
        }

    @Test
    fun deleteAccount_afterDeleting_Return1ThatMeansAccountWasDeleted() = runTest {
        val accountDao = database.accountDao
        val entity = createAccountEntities(1)[0]

        val id = accountDao.writeAccount(entity)
        val insertedAccount = accountDao.readAccounts()[0]
        val isDeleted = accountDao.removeAccount(insertedAccount)

        assertEquals(1, id)
        assertEquals(1, isDeleted)
    }

    @Test
    fun deleteAccount_afterDeleting_Return0ThatMeansAccountWasNotDeleted() = runTest {
        val accountDao = database.accountDao
        val entity = createAccountEntities(1)[0]

        val isDeleted = accountDao.removeAccount(entity)

        assertEquals(0, isDeleted)
    }

    @Test
    fun readAccount_afterReading_ReturnAccount() = runTest {
        val accountDao = database.accountDao
        val entity = createAccountEntities(1)[0]

        val id = accountDao.writeAccount(entity)
        val account = accountDao.readAccount(entity.email)

        assertEquals(1, id)
        assertNotEquals(null, account)
        assertEquals(entity.email, account!!.email)
        assertEquals(entity.avatarUrl, account.avatarUrl)
        assertEquals(entity.username, account.username)
        assertEquals(entity.password, account.password)
    }

    @Test
    fun readAccount_afterReading_ReturnNull() = runTest {
        val accountDao = database.accountDao
        val email = "email"

        val account = accountDao.readAccount(email)

        assertEquals(null, account)
    }

    @Test
    fun readAccounts_afterReading_ReturnEmptyList() = runTest {
        val accountDao = database.accountDao

        val entities = accountDao.readAccounts()

        assertEquals(true, entities.isEmpty())
    }

    @Test
    fun readAccounts_afterReading_ReturnListWithOneAccountEntity() = runTest {
        val accountDao = database.accountDao
        val entity = createAccountEntities(1)[0]

        val id = accountDao.writeAccount(entity)
        val entities = accountDao.readAccounts()

        assertEquals(1, id)
        assertEquals(1, entities.size)
        assertEquals(entity.email, entities[0].email)
    }

    @Test
    fun readAccounts_afterReading_ReturnListWithThreeAccountEntities() = runTest {
        val accountDao = database.accountDao
        val entities = createAccountEntities(3)

        entities.forEach {
            accountDao.writeAccount(it)
        }
        val accounts = accountDao.readAccounts()

        assertEquals(3, accounts.size)
        assertEquals(entities[0].email, accounts[0].email)
        assertEquals(entities[1].email, accounts[1].email)
        assertEquals(entities[2].email, accounts[2].email)
    }

    @Test
    fun updateAccount_afterUsernameUpdate_ReturnIdOfUpdatedAccount() = runTest {
        val accountDao = database.accountDao
        val entity = createAccountEntities(1)[0]

        accountDao.writeAccount(entity)
        val insertedAccount = accountDao.readAccounts()[0]
        val id = accountDao.updateAccount(insertedAccount.copy(username = "new username"))
        val updatedAccount = accountDao.readAccounts()[0]

        assertEquals(1, id)
        assertEquals("new username", updatedAccount.username)
    }

    @Test
    fun updateAccount_afterAvatarUrlUpdate_ReturnIdOfUpdatedAccount() = runTest {
        val accountDao = database.accountDao
        val entity = createAccountEntities(1)[0]

        accountDao.writeAccount(entity)
        val insertedAccount = accountDao.readAccounts()[0]
        val id = accountDao.updateAccount(insertedAccount.copy(avatarUrl = "new avatar url"))
        val updatedAccount = accountDao.readAccounts()[0]

        assertEquals(1, id)
        assertEquals("new avatar url", updatedAccount.avatarUrl)
    }

    @Test
    fun updateAccount_afterPasswordUpdate_ReturnIdOfUpdatedAccount() = runTest {
        val accountDao = database.accountDao
        val entity = createAccountEntities(1)[0]

        accountDao.writeAccount(entity)
        val insertedAccount = accountDao.readAccounts()[0]
        val id = accountDao.updateAccount(insertedAccount.copy(password = "new password"))
        val updatedAccount = accountDao.readAccounts()[0]

        assertEquals(1, id)
        assertEquals("new password", updatedAccount.password)
    }

    @Test
    fun updateAccount_afterAccountUpdate_ReturnZeroBecauseDBHasNotUpdatableAccount() = runTest {
        val accountDao = database.accountDao
        val entity = createAccountEntities(1)[0].copy(id = 15)

        val id = accountDao.updateAccount(entity)

        assertEquals(0, id)
    }

    private fun createAccountEntities(count: Int): List<AccountDataEntity> {
        val result = mutableListOf<AccountDataEntity>()
        for (i in 1..count) {
            result += AccountDataEntity(
                username = "username $i",
                avatarUrl = "url $i",
                email = "email $i",
                password = "password $i",
                date = Date()
            )
        }

        return result
    }
}