package uk.ac.tees.mad.careerconnect.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.careerconnect.data.repoImpl.RepositoryImpl
import uk.ac.tees.mad.careerconnect.domain.repo.Repository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {



    private val _currentUserData = MutableStateFlow(GetUserInfo())

    val currentUserData: StateFlow<GetUserInfo> = _currentUserData

    val db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun logoutUser() {

        auth.signOut()

    }
    fun signUp(
        email: String,
        password: String,
        name: String,
        onResult: (String, Boolean) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val userId = user?.uid

                            if (userId != null) {
                                // Create donor info
                                val userInfo = PostUserInfo(
                                    profileImageUrl = "",
                                    title = "Job Seeker",
                                    mobNumber = "7668532625",
                                    name = name,
                                    email = email,
                                    uid = userId ,
                                    passkey = password
                                )

                                db.collection("user").document(userId).set(userInfo)
                                    .addOnSuccessListener {
                                        onResult("Signup successful", true)
                                    }
                                    .addOnFailureListener { exception ->
                                        auth.currentUser?.delete() // rollback user creation
                                        onResult("Failed to save user info", false)
                                    }
                            } else {
                                onResult("User ID not found", false)
                            }
                        } else {
                            val errorMessage = when (task.exception) {
                                is FirebaseAuthUserCollisionException -> "This email is already registered"
                                is FirebaseAuthWeakPasswordException -> "Password is too weak"
                                else -> task.exception?.localizedMessage ?: "Signup failed"
                            }
                            onResult(errorMessage, false)
                        }
                    }
            } catch (e: Exception) {
                onResult("Unexpected error: ${e.localizedMessage}", false)
            }
        }
    }



    fun logIn(
        email: String,
        passkey: String,
        onResult: (String, Boolean) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, passkey)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onResult("Login successful", true)
                        } else {
                            val errorMessage = task.exception?.localizedMessage ?: "Login failed"
                            onResult(errorMessage, false)
                        }
                    }
            } catch (e: Exception) {
                onResult("Error: ${e.localizedMessage}", false)
            }
        }
    }







}


data class PostUserInfo(
    val profileImageUrl: String,
    val title: String,
    val mobNumber: String,
    val name: String,
    val email: String,
    val uid: String,
    val passkey: String,
)

data class GetUserInfo(
    val profileImageUrl: String = "",
    val title: String = "",
    val mobNumber: String = "",
    val name: String = "",
    val email: String = "",
    val uid: String = "",
    val passkey: String = "",
)
