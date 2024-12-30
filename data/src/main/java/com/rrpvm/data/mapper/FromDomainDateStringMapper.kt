package com.rrpvm.data.mapper

import java.util.Date

object FromDomainDateStringMapper {
    fun mapToDomainString(date: Date): String {
        return kotlin.runCatching {
            com.rrpvm.domain.util.Const.baseFullDateFormat.format(date)
        }.getOrElse {
            throw IllegalArgumentException("illegal date format in DOMAIN!")
        }
    }

    fun mapToDomainDate(domainString: String): Date {
        return kotlin.runCatching {
            com.rrpvm.domain.util.Const.baseFullDateFormat.parse(domainString)
        }.getOrElse {
            throw IllegalArgumentException("illegal date format in DOMAIN!")
        }
    }
}