package viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.User
import com.example.ecommerceapp.util.Constants.USER_COLLECTION
import com.example.ecommerceapp.util.RegisterFieldState
import com.example.ecommerceapp.util.RegisterValidation
import com.example.ecommerceapp.util.validateEmail
import com.example.ecommerceapp.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import util.States
import javax.inject.Inject

@HiltViewModel//inject dependencies
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestoredb: FirebaseFirestore
):ViewModel(){
    //create a channel for emitting values
    private val _validation = Channel<RegisterFieldState>()//does not require initial value
    val validation = _validation.receiveAsFlow()
    //mutable state flow listener to listen for different results
    private val _register = MutableStateFlow<States<User>>(States.Starting())
     val register: Flow<States<User>> = _register
    //firebase provides the methods for registering
    fun createAccount(user: User, password:String) {
        if (checkValidation(user, password)) {
            runBlocking {
                _register.emit(States.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid,user)
                    }

                }
                .addOnFailureListener {
                    _register.value = States.OnFailure(it.message.toString())
                }
        }else{
            val registerFieldState = RegisterFieldState(
                validateEmail(user.email),
                validatePassword(password)
            )
            viewModelScope.launch {
                _validation.send(registerFieldState)
            }
        }
    }

    private fun saveUserInfo(userId: String,user:User) {
        firestoredb.collection(USER_COLLECTION)
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                  _register.value = States.OnSuccess(user)

            }
            .addOnFailureListener {
                _register.value = States.OnFailure(it.message.toString())

            }
    }

    fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)

        return emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success
    }
}