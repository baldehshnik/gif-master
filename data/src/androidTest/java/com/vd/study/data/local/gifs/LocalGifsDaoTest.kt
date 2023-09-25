package com.vd.study.data.local.gifs

import androidx.test.filters.SmallTest
import com.vd.study.data.local.LocalDatabase
import com.vd.study.data.local.accounts.entities.AccountDataEntity
import com.vd.study.data.local.gifs.entities.LocalGifAuthorDataEntity
import com.vd.study.data.local.gifs.entities.LocalGifDataEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class LocalGifsDaoTest {

    @get:Rule
    var rule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var database: LocalDatabase

    @Before
    fun initLocalDatabase() {
        rule.inject()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertGif_afterInsertion_ReturnIdOfInsertedGif() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gif = createGifEntities(1)[0]

        val insertedAccountId = accountDao.writeAccount(account)
        val insertedGifId = gifsDao.writeGif(gif)

        assertEquals(1, insertedAccountId)
        assertEquals(1, insertedGifId)
    }

    @Test
    fun insertGif_afterInsertion5Gifs_ReturnIdsOfInsertedGifs() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val entities = createGifEntities(5)

        val insertedAccountId = accountDao.writeAccount(account)
        val gifIds = mutableListOf<Long>()
        entities.forEach {
            gifIds += gifsDao.writeGif(it)
        }

        assertEquals(1, insertedAccountId)
        assertEquals(5, gifIds.size)
        assertEquals(1, gifIds[0])
        assertEquals(2, gifIds[1])
        assertEquals(3, gifIds[2])
        assertEquals(4, gifIds[3])
        assertEquals(5, gifIds[4])
    }

    @Test
    fun insertGif_afterInsertionInTwoDifferentAccounts_ReturnIdsOfInsertedGifsAndIdsOfAccounts() =
        runTest {
            val gifsDao = database.gifsDao
            val accountDao = database.accountDao
            val accounts = createAccountEntities(2)
            val gifs = createGifEntities(2)

            accounts.forEach {
                accountDao.writeAccount(it)
            }
            val firstInsertedGifId = gifsDao.writeGif(gifs[0])
            val secondInsertedGifId = gifsDao.writeGif(gifs[1].copy(accountId = 2))
            val firstGif = gifsDao.readGifById(1)
            val secondGif = gifsDao.readGifById(2)

            assertEquals(1, firstInsertedGifId)
            assertEquals(2, secondInsertedGifId)
            assertNotEquals(null, firstGif)
            assertNotEquals(null, secondGif)
            assertEquals(1, firstGif!!.accountId)
            assertEquals(2, secondGif!!.accountId)
        }

    @Test
    fun readGif_AfterReading_ReturnNull() = runTest {
        val gifsDao = database.gifsDao

        val gif = gifsDao.readGifById(1)

        assertEquals(null, gif)
    }

    @Test
    fun readGif_AfterReading_ReturnInsertedGif() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gif = createGifEntities(1)[0]

        val insertedAccountId = accountDao.writeAccount(account)
        val insertedGifId = gifsDao.writeGif(gif)
        val insertedGif = gifsDao.readGifById(1)

        assertEquals(1, insertedAccountId)
        assertEquals(1, insertedGifId)
        assertNotEquals(null, insertedGif)
        assertEquals(gif, insertedGif!!.copy(id = 0))
    }

    @Test
    fun readGifByUrl_AfterReading_ReturnInsertedGif() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gif = createGifEntities(1)[0]

        val insertedAccountId = accountDao.writeAccount(account)
        val insertedGifId = gifsDao.writeGif(gif)
        val insertedGif = gifsDao.readGifByUrl(1, gif.url)

        assertEquals(1, insertedAccountId)
        assertEquals(1, insertedGifId)
        assertNotEquals(null, insertedGif)
        assertEquals(gif, insertedGif.copy(id = 0))
    }

    @Test
    fun readSavedGifsCount_AfterReading_Return0() = runTest {
        val gifsDao = database.gifsDao

        val savedGifsCount = gifsDao.readSavedGifsCount(1)

        assertEquals(0, savedGifsCount)
    }

    @Test
    fun readSavedGifsCount_AfterReading_Return1() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gif = createGifEntities(1)[0]

        val insertedAccountId = accountDao.writeAccount(account)
        val insertedGifId = gifsDao.writeGif(gif)
        val savedGifsCount = gifsDao.readSavedGifsCount(1)

        assertEquals(1, insertedAccountId)
        assertEquals(1, insertedGifId)
        assertEquals(1, savedGifsCount)
    }

    @Test
    fun readSavedGifsCount_AfterReading_Return3() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gifs = createGifEntities(4).toMutableList()

        gifs[1] = gifs[1].copy(isSaved = false)
        val insertedAccountId = accountDao.writeAccount(account)
        gifs.forEach {
            gifsDao.writeGif(it)
        }
        val savedGifsCount = gifsDao.readSavedGifsCount(1)

        assertEquals(1, insertedAccountId)
        assertEquals(3, savedGifsCount)
    }

    @Test
    fun readLikedGifsCount_AfterReading_Return0() = runTest {
        val gifsDao = database.gifsDao

        val savedGifsCount = gifsDao.readLikedGifsCount(1)

        assertEquals(0, savedGifsCount)
    }

    @Test
    fun readLikedGifsCount_AfterReading_Return1() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gif = createGifEntities(1)[0].copy(isLiked = true)

        val insertedAccountId = accountDao.writeAccount(account)
        val insertedGifId = gifsDao.writeGif(gif)
        val savedGifsCount = gifsDao.readLikedGifsCount(1)

        assertEquals(1, insertedAccountId)
        assertEquals(1, insertedGifId)
        assertEquals(1, savedGifsCount)
    }

    @Test
    fun readLikedGifsCount_AfterReading_Return2() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gifs = createGifEntities(4).toMutableList()

        gifs[1] = gifs[1].copy(isLiked = true)
        gifs[2] = gifs[2].copy(isLiked = true)
        val insertedAccountId = accountDao.writeAccount(account)
        gifs.forEach {
            gifsDao.writeGif(it)
        }
        val savedGifsCount = gifsDao.readLikedGifsCount(1)

        assertEquals(1, insertedAccountId)
        assertEquals(2, savedGifsCount)
    }

    @Test
    fun readLikedGifs_AfterReading_ReturnEmptyList() = runTest {
        val gifsDao = database.gifsDao

        val result: List<LocalGifDataEntity> = gifsDao.readLikedGifs(1).first()

        assertEquals(true, result.isEmpty())
    }

    @Test
    fun readLikedGifs_AfterReading_Return3() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gifs = createGifEntities(3).map { it.copy(isLiked = true) }

        accountDao.writeAccount(account)
        gifs.forEach {
            gifsDao.writeGif(it)
        }
        val result: List<LocalGifDataEntity> = gifsDao.readLikedGifs(1).first()

        assertEquals(3, result.size)
        assertEquals(1, result[0].id)
        assertEquals(2, result[1].id)
        assertEquals(3, result[2].id)
    }

    @Test
    fun readLikedGifs_AfterReading_Return1() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gifs = createGifEntities(3).toMutableList()

        accountDao.writeAccount(account)
        gifs.add(gifs[0].copy(title = "title copy", isLiked = true))
        gifs.forEach {
            gifsDao.writeGif(it)
        }
        val result: List<LocalGifDataEntity> = gifsDao.readLikedGifs(1).first()

        assertEquals(1, result.size)
    }

    @Test
    fun readSavedGifs_AfterReading_ReturnEmptyList() = runTest {
        val gifsDao = database.gifsDao

        val result: List<LocalGifDataEntity> = gifsDao.readSavedGifs(1).first()

        assertEquals(true, result.isEmpty())
    }

    @Test
    fun readSavedGifs_AfterReading_Return3() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gifs = createGifEntities(3)

        accountDao.writeAccount(account)
        gifs.forEach {
            gifsDao.writeGif(it)
        }
        val result: List<LocalGifDataEntity> = gifsDao.readSavedGifs(1).first()

        assertEquals(3, result.size)
        assertEquals(1, result[0].id)
        assertEquals(2, result[1].id)
        assertEquals(3, result[2].id)
    }

    @Test
    fun readSavedGifs_AfterReading_Return1() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gifs = createGifEntities(1).toMutableList()

        accountDao.writeAccount(account)
        gifs.add(gifs[0].copy(title = "title copy", isSaved = false, isLiked = true))
        gifs.forEach {
            gifsDao.writeGif(it)
        }
        val result: List<LocalGifDataEntity> = gifsDao.readLikedGifs(1).first()

        assertEquals(1, result.size)
    }

    @Test
    fun insertOrReplace_AfterMethodCall_WriteNewGif() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gif = createGifEntities(1)[0]

        val insertedAccountId = accountDao.writeAccount(account)
        val answer = gifsDao.updateOrInsert(gif)
        val insertedGif = gifsDao.readGifById(1)

        assertEquals(1, insertedAccountId)
        assertEquals(1, answer)
        assertNotEquals(null, insertedGif)
        assertEquals("title 1", insertedGif!!.title)
    }

    @Test
    fun insertOrReplace_AfterMethodCall_UpdateInsertedGif() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gif = createGifEntities(1)[0]

        val insertedAccountId = accountDao.writeAccount(account)
        val insertedGifId = gifsDao.writeGif(gif)
        val insertedGif = gifsDao.readGifById(1)
        val updatedGifId = gifsDao.updateOrInsert(insertedGif!!.copy(title = "new title"))
        val updatedGif = gifsDao.readGifById(1)
        val gifsCount = gifsDao.readSavedGifsCount(1)

        assertEquals(1, insertedAccountId)
        assertEquals(1, insertedGifId)
        assertEquals("title 1", insertedGif.title)
        assertEquals(1, updatedGifId)
        assertNotEquals(null, updatedGif)
        assertEquals("new title", updatedGif!!.title)
        assertEquals(1, gifsCount)
    }

    @Test
    fun updateGif_AfterRatingUpdating_UpdateRating() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gif = createGifEntities(1)[0]

        val insertedAccountId = accountDao.writeAccount(account)
        val insertedGifId = gifsDao.writeGif(gif)
        val insertedGif = gifsDao.readGifById(1)
        val updatedGifId = gifsDao.updateGif(insertedGif!!.copy(rating = "new rating"))
        val updatedGif = gifsDao.readGifById(1)
        val gifsCount = gifsDao.readSavedGifsCount(1)

        assertEquals(1, insertedAccountId)
        assertEquals(1, insertedGifId)
        assertEquals(1, updatedGifId)
        assertNotEquals(insertedGif, updatedGif)
        assertEquals("new rating", updatedGif!!.rating)
        assertEquals(1, gifsCount)
    }

    @Test
    fun updateGif_AfterIsSavedUpdating_TriggerShouldDeleteGifFromDatabase() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gif = createGifEntities(1)[0]

        val insertedAccountId = accountDao.writeAccount(account)
        val insertedGifId = gifsDao.writeGif(gif)
        val insertedGif = gifsDao.readGifById(1)
        gifsDao.updateGif(insertedGif!!.copy(isSaved = false))
        val updatedGif = gifsDao.readGifById(1)

        assertEquals(1, insertedAccountId)
        assertEquals(1, insertedGifId)
        assertEquals(null, updatedGif)
    }

    @Test
    fun updateGif_AfterIsLikedUpdating_TriggerShouldDeleteGifFromDatabase() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val gif = createGifEntities(1)[0].copy(isSaved = false, isLiked = true)

        val insertedAccountId = accountDao.writeAccount(account)
        val insertedGifId = gifsDao.writeGif(gif)
        val insertedGif = gifsDao.readGifById(1)
        gifsDao.updateGif(insertedGif!!.copy(isLiked = false))
        val updatedGif = gifsDao.readGifById(1)

        assertEquals(1, insertedAccountId)
        assertEquals(1, insertedGifId)
        assertEquals(null, updatedGif)
    }

    @Test
    fun updateGif_AfterAuthorUsernameUpdating_ReturnGifWithNewAuthorUsername() = runTest {
        val gifsDao = database.gifsDao
        val accountDao = database.accountDao
        val account = createAccountEntities(1)[0]
        val author = LocalGifAuthorDataEntity("username", "avatar")
        val gif = createGifEntities(1)[0].copy(author = author)

        val insertedAccountId = accountDao.writeAccount(account)
        val insertedGifId = gifsDao.writeGif(gif)
        val insertedGif = gifsDao.readGifById(1)
        val updatedGifId = gifsDao.updateGif(insertedGif!!.copy(author = author.copy(username = "new username")))
        val updatedGif = gifsDao.readGifById(1)

        assertEquals(1, insertedAccountId)
        assertEquals(1, insertedGifId)
        assertEquals(1, updatedGifId)
        assertEquals("new username", updatedGif!!.author!!.username)
    }

    private fun createGifEntities(count: Int): List<LocalGifDataEntity> {
        val result = mutableListOf<LocalGifDataEntity>()
        for (i in 1..count) {
            result += LocalGifDataEntity(
                title = "title $i",
                url = "url $i",
                rating = "rating $i",
                isLiked = false,
                isSaved = true,
                accountId = 1
            )
        }

        return result
    }

    private fun createAccountEntities(count: Int): List<AccountDataEntity> {
        val result = mutableListOf<AccountDataEntity>()
        for (i in 1..count) {
            result += AccountDataEntity(
                username = "username $i",
                avatarUrl = "url $i",
                email = "email $i",
                password = "password $i"
            )
        }

        return result
    }
}