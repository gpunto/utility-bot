package dev.gianmarcodavid.telegram.holiday

import me.tatarka.inject.annotations.Inject
import java.time.LocalDate

@Inject
class HolidaysRepository(
    private val holidayApi: HolidayApi,
    private val mapper: HolidayMapper,
) {
    suspend fun fetchHolidays(countryCode: String, startingFrom: LocalDate, limit: Int): List<Holiday> =
        holidayApi.getHolidays(year = startingFrom.year, countryCode = countryCode)
            .map(mapper::map)
            .dropWhile { it.date < startingFrom }
            .take(limit)
}
