package com.rrpvm.domain.repository

import com.rrpvm.domain.model.FilterModel
import kotlinx.coroutines.flow.Flow


interface FilterRepository {
    fun <T : FilterModel> getFilterBy(clasz: Class<T>): Flow<List<out T>>
    fun getFilters(): Flow<List<FilterModel>>
    fun addFilter(filter: FilterModel): Result<Boolean>
    fun removeAllFilters(): Result<Boolean>
    fun removeFilter(filter: FilterModel): Result<Boolean>
}