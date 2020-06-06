package com.project.pocketdoc.util

class PageParams(var pageNumber: Int, private var history_: Int, private var active_: Int) {
    val history
        get() = history_

    val active
        get() = active_

    fun setActive(): Boolean {
        if (checkActive())
            return false
        history_ = 0
        active_ = 1
        return true
    }

    fun checkActive() = (history_ == 0 && active_ == 1)

    fun setHistory(): Boolean {
        if (checkHistory())
            return false
        history_ = 1
        active_ = 0
        return true
    }

    fun checkHistory() = (history_ == 1 && active_ == 0)

    fun setZeros(): Boolean {
        if (checkZeros())
            return false
        history_ = 0
        active_ = 0
        return true
    }

    fun checkZeros() = (history_ == 0 && active_ == 0)

    companion object {
        fun getActive(pageNumber: Int = 0) = PageParams(pageNumber, 0, 1)
        fun getZeros(pageNumber: Int = 0) = PageParams(pageNumber, 0, 0)
        fun getHistory(pageNumber: Int = 0) = PageParams(pageNumber, 1, 0)
    }
}