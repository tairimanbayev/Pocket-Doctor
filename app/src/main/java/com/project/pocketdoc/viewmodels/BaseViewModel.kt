package com.project.pocketdoc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.pocketdoc.util.Status

abstract class BaseViewModel<T>: ViewModel() {
    protected val _status = MutableLiveData<Status<T>>()
}

abstract class StatusViewModel<T> : BaseViewModel<T>() {
    val status: LiveData<Status<T>>
        get() = _status
}

abstract class LazyViewModel<T> : BaseViewModel<T>() {
    val status: LiveData<Status<T>> by lazy {
        loadData()
        _status
    }

    protected abstract fun loadData()
}
