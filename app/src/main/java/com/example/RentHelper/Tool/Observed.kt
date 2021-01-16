package com.example.RentHelper.Tool
interface Observer<T> {
    fun onBind(observed: Observed<T>)
    fun update(data: T)
}


abstract class Observed<T> {
    private val observers: ArrayList<Observer<T>> = ArrayList()
    fun register(observer: Observer<T>) {
        observers.add(observer)
        onRegister()
    }

    fun unregister(observer: Observer<T>) {
        observers.remove(observer)
        onUnRegister()
    }

    fun closeObserved() {
        observers.clear()
        onCloseObserved()
    }

    abstract fun onRegister()

    abstract fun onUnRegister()

    abstract fun onCloseObserved()

    fun notify(data: T) {
        for (i in observers) {
            i.update(data)
        }
    }
}

abstract class ObserverSomethings<T>:Observer<T>{
    private val observeds:ArrayList<Observed<T>> = ArrayList()
    fun close (){
        observeds.forEach {
             it.unregister(this)
        }
    }
    override fun onBind(observed: Observed<T>) {
        if(!observeds.contains(observed)){
            observed.register(this)
            observeds.add(observed)
        }
    }
}