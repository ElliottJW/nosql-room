package dev.libatorium.nosqlroom.client

import androidx.test.platform.app.InstrumentationRegistry
import dev.libatorium.nosqlroom.data.MockCar
import dev.libatorium.nosqlroom.data.MockUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class RoomDatabaseClientImplTest {

    private val userId = UUID.randomUUID().toString()
    private lateinit var underTest: DatabaseClient

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        underTest = RoomDatabaseClientImpl(
            context = context,
            defaultDispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun getCount_withNoItemsSaved_returns_zero() = runTest {
        val actual = underTest.count(userId, MockUser::class)
        val expected = 0
        assertEquals(expected, actual)
    }

    @Test
    fun getCount_withItemsSaved_returns_itemCount() = runTest {
        val items = arrayOf(
            MockUser(
                id = "1",
                firstName = "Test",
                lastName = "McTestFace",
                emailAddress = "test.mctester@gmail.com"
            ),
            MockUser(
                id = "2",
                firstName = "Jason",
                lastName = "Mraz",
                emailAddress = "jason.mraz@gmail.com"
            ),
            MockUser(
                id = "3",
                firstName = "Bugs",
                lastName = "Bunny",
                emailAddress = "bugs.bunny@gmail.com"
            ),
        )
        underTest.save(userId, items = items)

        val actual = underTest.count(userId, MockUser::class)
        val expected = items.size
        assertEquals(expected, actual)
    }

    @Test
    fun getItem_retrieves_previouslySavedItem() = runTest {
        // Given
        val expected = MockUser(
            id = "1",
            firstName = "Test",
            lastName = "McTestFace",
            emailAddress = "test.mctester@gmail.com"
        )
        underTest.save(userId, expected)

        // When
        val actual = underTest.get(userId, itemId = expected.id, MockUser::class).first()

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun noItemsFound_ifNoneAvailable() = runTest {
        // Given
        val item = MockUser(
            id = "1",
            firstName = "Test",
            lastName = "McTestFace",
            emailAddress = "test.mctester@gmail.com"
        )
        underTest.save(userId, item)

        // When
        val actual = underTest.get(userId, itemId = "bad_id", MockUser::class).firstOrNull()

        // Then
        assertNull(actual)
    }

    @Test
    fun onlyReturnItems_ofRequestedType() = runTest {
        val cars = arrayOf(
            MockCar(
                id = UUID.randomUUID().toString(),
                brand = "Subaru"
            ),
            MockCar(
                id = UUID.randomUUID().toString(),
                brand = "Honda"
            ),
            MockCar(
                id = UUID.randomUUID().toString(),
                brand = "Mercedes"
            ),
            MockCar(
                id = UUID.randomUUID().toString(),
                brand = "Audi"
            )
        )
        val users = arrayOf(
            MockUser(
                id = "1",
                firstName = "Test",
                lastName = "McTestFace",
                emailAddress = "test.mctester@gmail.com"
            ),
            MockUser(
                id = "2",
                firstName = "Jason",
                lastName = "Mraz",
                emailAddress = "jason.mraz@gmail.com"
            ),
            MockUser(
                id = "3",
                firstName = "Bugs",
                lastName = "Bunny",
                emailAddress = "bugs.bunny@gmail.com"
            )
        )
        val items = arrayOf(*cars, *users)
        underTest.save(userId, items = items)

        val actual = underTest.getAll(userId, MockCar::class).first()
        val expected = cars.toList()
        assertEquals(expected, actual)
    }

    @Test
    fun onlyReturnSpecificItem() = runTest {
        val cars = arrayOf(
            MockCar(
                id = UUID.randomUUID().toString(),
                brand = "Subaru"
            ),
            MockCar(
                id = UUID.randomUUID().toString(),
                brand = "Honda"
            ),
            MockCar(
                id = UUID.randomUUID().toString(),
                brand = "Mercedes"
            ),
            MockCar(
                id = UUID.randomUUID().toString(),
                brand = "Audi"
            )
        )
        val users = arrayOf(
            MockUser(
                id = "1",
                firstName = "Test",
                lastName = "McTestFace",
                emailAddress = "test.mctester@gmail.com"
            ),
            MockUser(
                id = "2",
                firstName = "Jason",
                lastName = "Mraz",
                emailAddress = "jason.mraz@gmail.com"
            ),
            MockUser(
                id = "3",
                firstName = "Bugs",
                lastName = "Bunny",
                emailAddress = "bugs.bunny@gmail.com"
            )
        )
        val items = arrayOf(*cars, *users)
        underTest.save(userId, items = items)

        val expected = users[0]
        val actual = underTest.get(userId, itemId = expected.id, MockUser::class).first()
        assertEquals(expected, actual)
    }

    @Test
    fun deleteItemById_returnsDeletedItemCount() = runTest {
        val item = MockCar(
            id = UUID.randomUUID().toString(),
            brand = "Subaru"
        )
        underTest.save(userId, item)

        // When
        val actual = underTest.delete(userId, item.id)
        val expected = 1

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun deleteSpecificItem_returnsDeletedItemCount() = runTest {
        val item = MockCar(
            id = UUID.randomUUID().toString(),
            brand = "Subaru"
        )
        underTest.save(userId, item)

        // When
        val actual = underTest.delete(userId, item)
        val expected = 1

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun returnEmptyList_ifNoItemsFound() = runTest {
        val expected = emptyList<MockCar>()
        val actual = underTest.getAll(userId, MockCar::class).first()
        assertEquals(expected, actual)
    }

    @Test
    fun returnZero_ifNoItemsDeleted() = runTest {
        val expected = 0
        val actual = underTest.delete(userId, "bad_item_id")
        assertEquals(expected, actual)
    }

    @Test
    fun savingItem_returnsSavedItemCount() = runTest {
        val items = arrayOf(
            MockCar(
                id = UUID.randomUUID().toString(),
                brand = "Subaru"
            ),
            MockCar(
                id = UUID.randomUUID().toString(),
                brand = "Honda"
            ),
            MockCar(
                id = UUID.randomUUID().toString(),
                brand = "Mercedes"
            ),
            MockCar(
                id = UUID.randomUUID().toString(),
                brand = "Audi"
            )
        )
        underTest.save(userId, *items)

        val actual = underTest.count(userId, MockCar::class)
        val expected = items.size

        assertEquals(expected, actual)
    }
}