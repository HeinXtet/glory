package com.codigo.mvi

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

abstract class MviActivity<VM : MviViewModel<VS, E, I>, VS, E, I> : AppCompatActivity() {

    protected var compositeDisposable =  CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(getLayoutId())

        compositeDisposable.clear()

        getViewModel().streamViewSates()
            .subscribe { renderInternal(it) }
            .addTo(compositeDisposable)

        getViewModel().streamEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { renderEventInternal(it) }
            .addTo(compositeDisposable)

    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): VM

    abstract fun render(viewState: VS)

    abstract fun renderEvent(event: E)

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun renderEventInternal(event: E) {
        if (isDestroyed) return
        renderEvent(event)
    }

    private fun renderInternal(viewState: VS) {
        if (isDestroyed) return
        render(viewState)
    }
}
