package com.example.ecommerceapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import util.States
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
):ViewModel() {
    private val _login = MutableSharedFlow<States<FirebaseUser>>()
    val login = _login.asSharedFlow()

    //forgot password authentication
    private val _resetPassword = MutableSharedFlow<States<String>>()
    val resetPassword = _resetPassword.asSharedFlow()


    fun loginUser(email:String, password:String){
        viewModelScope.launch {
            _login.emit(States.Loading())
        }
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                viewModelScope.launch {
                    it.user?.let {
                        _login.emit(States.OnSuccess(it))
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _login.emit(States.OnFailure(it.message.toString()))
                }
            }
    }

    fun resetPassword(email:String){

    }
}