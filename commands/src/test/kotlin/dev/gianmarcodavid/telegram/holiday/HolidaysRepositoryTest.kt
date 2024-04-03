package dev.gianmarcodavid.telegram.holiday

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class HolidaysRepositoryTest {
    private val api = FakeHolidayApi()

    private val repository = HolidaysRepository(api, HolidayMapper())

    @Test
    fun `fetchHolidays returns at most 'limit' holidays, including those falling on 'startingFrom'`() = runTest {
        api returns listOf(
            holidayDto(date = "2024-01-05", name = "The one before"),
            holidayDto(date = "2024-01-06", name = "Holiday"),
            holidayDto(date = "2024-01-06", name = "Another one on the same day"),
            holidayDto(date = "2024-01-07", name = "The one after"),
            holidayDto(date = "2024-02-07", name = "More"),
        )
        val expected = listOf(
            holiday(LocalDate.of(2024, 1, 6), "Holiday"),
            holiday(LocalDate.of(2024, 1, 6), "Another one on the same day"),
            holiday(LocalDate.of(2024, 1, 7), "The one after"),
        )

        val holidays = repository.fetchHolidays(
            countryCode = "IT",
            startingFrom = LocalDate.of(2024, 1, 6),
            limit = 3
        )

        assertEquals(expected, holidays)
    }

    private fun holidayDto(date: String, name: String) = HolidayDto(date, name, "IT", true, null)
    private fun holiday(date: LocalDate, name: String) = Holiday(date, name, true, emptyList())
}

private class FakeHolidayApi : HolidayApi {
    private var returnNext: List<HolidayDto> = emptyList()

    override suspend fun getHolidays(year: Int, countryCode: String): List<HolidayDto> = returnNext

    infix fun returns(holidays: List<HolidayDto>) {
        returnNext = holidays
    }
}
