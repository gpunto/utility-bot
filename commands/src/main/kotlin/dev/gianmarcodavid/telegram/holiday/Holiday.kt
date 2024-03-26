package dev.gianmarcodavid.telegram.holiday

import java.time.LocalDate

data class Holiday(
    val date: LocalDate,
    val name: String,
    val global: Boolean,
    val counties: List<String>
)
