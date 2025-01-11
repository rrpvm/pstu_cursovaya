package com.rrpvm.data.repository

import com.rrpvm.domain.model.FilterModel
import com.rrpvm.domain.repository.FilterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FilterRepositoryImpl : FilterRepository {
    private val mFilters = MutableStateFlow<List<FilterModel>>(emptyList())
    override fun getFilters(): Flow<List<FilterModel>> {
        return mFilters.asStateFlow()
    }

    override fun <T : FilterModel> getFilterBy(clasz: Class<T>): Flow<List<T>> {
        return mFilters.map {
            it.filterIsInstance(clasz)
        }
    }


    override fun addFilter(filter: FilterModel): Result<Boolean> {
        return kotlin.runCatching {
            mFilters.value = mFilters.value.toMutableList().apply {
                add(filter)
            }.toSet().toList()
            true
        }
    }

    override fun removeAllFilters(): Result<Boolean> {
        return kotlin.runCatching {
            mFilters.value = emptyList()
            true
        }
    }

    override fun removeFilter(filter: FilterModel): Result<Boolean> {
        return kotlin.runCatching {
            mFilters.value -= filter
            true
        }
    }
}