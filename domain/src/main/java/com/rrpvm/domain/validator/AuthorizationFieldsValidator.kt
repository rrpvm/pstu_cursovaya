package com.rrpvm.domain.validator

class AuthorizationFieldsValidator {
    companion object {
        const val PASSWORD_MIN_LENGTH = 6
        const val PASSWORD_MAX_LENGTH = 30
        const val MAX_USERNAME_LENGTH = 30
        // ?! -запрет совпадения шаблона
        // d+ - любое количество цифр
        // \$ - конец строки.т.е только цифры
      //  val USERNAME_VALID_REGEX = "^(?!\\d+\$)[a-zA-Z0-9_]{3,30}\$".toRegex()
        val USERNAME_VALID_REGEX = "^(?=.*[a-zA-Z])[a-zA-Z0-9_]{3,${MAX_USERNAME_LENGTH}}\$".toRegex()
    }

    fun validateUsername(username: String): Boolean {
        return  username.matches(USERNAME_VALID_REGEX)
    }

    fun validatePassword(password: String):Boolean {
        return password.trim().let {
            it.length in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH
        }
    }
}