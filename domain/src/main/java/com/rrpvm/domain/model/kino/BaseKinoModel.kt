package com.rrpvm.domain.model.kino


data class BaseKinoModel(
    val id: String,
    val title: String,
    val previewImage: String,
)
fun listsEqual(list1: List<BaseKinoModel>, list2: List<BaseKinoModel>): Boolean {

    if (list1.size != list2.size)
        return false

    val pairList = list1.zip(list2)

    return pairList.all { (elt1, elt2) ->
        elt1 == elt2
    }
}