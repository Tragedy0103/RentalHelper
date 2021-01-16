package com.example.RentHelper.Global

import java.io.IOException

class ActionNotFoundException(val key:String):IOException(){
    fun getErrorKey():String{
        return key
    }
}