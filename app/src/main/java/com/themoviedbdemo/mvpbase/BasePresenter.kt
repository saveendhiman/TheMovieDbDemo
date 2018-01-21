package com.themoviedbdemo.mvpbase

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T> {
    protected var target: T? = null
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun takeTarget(target: T) {
        this.target = target
    }

    fun dropTarget() {
        this.target = null
        compositeDisposable.dispose()
    }
}

