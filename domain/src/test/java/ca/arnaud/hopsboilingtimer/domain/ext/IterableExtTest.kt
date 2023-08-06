package ca.arnaud.hopsboilingtimer.domain.ext

import ca.arnaud.hopsboilingtimer.domain.extension.associateByNotNull
import org.junit.Assert.assertEquals
import org.junit.Test

class IterableExtTest {

    @Test
    fun `GIVEN valid keys and valueTransform WHEN associating VERIFY correct map`() {
        val keys = listOf(1, 2, 3, 4)
        val valueTransform: (Int) -> List<String>? = { key ->
            when (key) {
                1 -> listOf("un", "one")
                3 -> listOf("trois", "tres")
                else -> null
            }
        }

        val map = keys.associateByNotNull(valueTransform = valueTransform)

        assertEquals(2, map.size)
        assertEquals(listOf("un", "one"), map[1])
        assertEquals(listOf("trois", "tres"), map[3])
    }

    @Test
    fun `GIVEN valid keys, keyTransform and valueTransform WHEN associating VERIFY correct map`() {
        val keys = listOf("A", "B", "C", "D")
        val keyTransform: (String) -> Int = { key ->
            when (key) {
                "A" -> 1
                "B" -> 2
                "C" -> 3
                else -> 4
            }
        }
        val valueTransform: (Int, String) -> List<String>? = { key, item ->
            when (key) {
                1 -> listOf("un", "one")
                3 -> listOf("trois", "tres")
                else -> null
            }
        }

        val map = keys.associateByNotNull(
            keyTransform = keyTransform,
            valueTransform = valueTransform,
        )

        assertEquals(2, map.size)
        assertEquals(listOf("un", "one"), map[1])
        assertEquals(listOf("trois", "tres"), map[3])
    }
}