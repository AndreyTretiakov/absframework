package com.tretiakov.absframework.routers;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */

interface Callback<T> {

    fun result(data: T?)

}