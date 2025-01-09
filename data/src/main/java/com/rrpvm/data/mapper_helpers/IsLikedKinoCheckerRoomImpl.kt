package com.rrpvm.data.mapper_helpers

import com.rrpvm.data.room.dao.KinoDao
import javax.inject.Inject

class IsLikedKinoCheckerRoomImpl @Inject constructor(private val kinoDao: KinoDao) :
    IsLikedKinoChecker {
    override fun isLiked(kinoId: String): Boolean {
        return kinoDao.getKino(kinoId)?.isLiked ?: false
    }
}