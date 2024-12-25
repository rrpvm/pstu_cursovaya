package com.rrpvm.domain

import com.rrpvm.domain.validator.AuthorizationFieldsValidator
import org.junit.Test

import org.junit.Assert.*
import java.lang.StringBuilder
import kotlin.random.Random

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ValidatorUnitTest {
    @Test
    fun usernameValidatorPassTest() {
        val validator = AuthorizationFieldsValidator()
        assertEquals(true, validator.validateUsername("aZAz"))
        assertEquals(true, validator.validateUsername("rrpvm"))
        assertEquals(true, validator.validateUsername("rrpvm1"))
        assertEquals(true, validator.validateUsername("a_a_a_a"))
        assertEquals(true, validator.validateUsername("r1_r2_r3"))

    }

    @Test
    fun usernameValidatorNonPassTest() {
        val validator = AuthorizationFieldsValidator()
        assertEquals(false, validator.validateUsername("11111"))
        assertEquals(false, validator.validateUsername("1_1_1_1"))
        assertEquals(false, validator.validateUsername("grew@fv"))
        assertEquals(false, validator.validateUsername("ff _feed"))
        assertEquals(false, validator.validateUsername("ff_feed?"))
        assertEquals(false, validator.validateUsername("ff_fe\\f"))
    }

    @Test
    fun usernameValidatorNonPassTestLength() {
        val validator = AuthorizationFieldsValidator()
        assertEquals(false, validator.validateUsername("1"))
        val builder = StringBuilder().apply {
            val rand = Random(1)
            repeat(AuthorizationFieldsValidator.MAX_USERNAME_LENGTH + 1) {
                append(rand.nextInt('a'.toInt(), 'z'.toInt()).toChar())
            }
        }.toString()
        assertEquals(
            false,
            validator.validateUsername(builder)
        )
    }

}