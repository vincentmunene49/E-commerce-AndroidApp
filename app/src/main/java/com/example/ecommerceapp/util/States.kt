package util

import java.lang.Thread.State

sealed class States<T>(
    val data:T?= null,
    val message:String? = null
){


    class OnSuccess<T>(data:T):States<T>(data)
    class OnFailure<T>(message: String) :States<T>(message = message)
    class Loading<T>():States<T>()
    class Starting<T>():States<T>()
}
