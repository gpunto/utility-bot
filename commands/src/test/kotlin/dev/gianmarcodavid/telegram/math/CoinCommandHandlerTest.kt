package dev.gianmarcodavid.telegram.math

import dev.gianmarcodavid.telegram.command.Reply
import dev.gianmarcodavid.telegram.command.bold
import dev.gianmarcodavid.telegram.command.regular
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class CoinCommandHandlerTest {
    private val random = TestRandom()

    private val handler = CoinCommandHandler(random)

    @Test
    fun `when nextBoolean returns true, return 'heads' reply`() = runTest {
        testHandle(1, "heads")
    }

    @Test
    fun `when nextBoolean returns false, return 'tails' reply`() = runTest {
        testHandle(0, "tails")
    }

    private suspend fun testHandle(nextBits: Int, side: String) {
        random.nextBits = nextBits
        val expected = Reply("Coin flip: ".regular(), side.bold())

        assertEquals(expected, handler.handle(""))
    }
}

private class TestRandom : Random() {
    var nextBits = 0

    override fun nextBits(bitCount: Int) = nextBits
}
