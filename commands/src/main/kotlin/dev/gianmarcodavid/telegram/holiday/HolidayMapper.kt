package dev.gianmarcodavid.telegram.holiday

import me.tatarka.inject.annotations.Inject
import java.time.LocalDate

@Inject
class HolidayMapper {
    fun map(dto: HolidayDto): Holiday = with(dto) {
        Holiday(
            date = LocalDate.parse(date),
            name = name,
            global = global,
            counties = counties?.map { county -> stripCountry(county, countryCode) }.orEmpty()
        )
    }

    private fun stripCountry(county: String, countryCode: String) = county.removePrefix("$countryCode-")
}
