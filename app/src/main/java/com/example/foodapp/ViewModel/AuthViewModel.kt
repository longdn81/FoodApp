package com.example.foodapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.Model.UserModel
import com.example.foodapp.Repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState




    init {
        checkAuthStatus()
    }


    fun checkAuthStatus(){
        val uid = auth.currentUser?.uid
        if (uid == null) {
            _authState.value = AuthState.Unauthenticated
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val snapshot = firestore.collection("users").document(uid).get().await()
                val user = snapshot.toObject(UserModel::class.java)
                if (user != null) {
                    _authState.value = AuthState.Authenticated(user)
                } else {
                    _authState.value = AuthState.Error("Không tìm thấy người dùng")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Lỗi khi tải người dùng: ${e.message}")
            }
        }
    }

    fun login(email : String,password : String){
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        viewModelScope.launch {
                            try {
                                val snapshot = firestore.collection("users").document(uid).get().await()
                                val user = snapshot.toObject(UserModel::class.java)
                                if (user != null) {
                                    _authState.value = AuthState.Authenticated(user)
                                } else {
                                    _authState.value = AuthState.Error("Không tìm thấy người dùng")
                                }
                            } catch (e: Exception) {
                                _authState.value = AuthState.Error("Lỗi khi lấy dữ liệu: ${e.message}")
                            }
                        }
                    } else {
                        _authState.value = AuthState.Error("UID không tồn tại")
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Đăng nhập thất bại")
                }
            }
    }

    fun signupWithUser(user: UserModel, password: String) {
        if (user.email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        // Sử dụng coroutine để xử lý bất đồng bộ
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                // Tạo người dùng trong Firebase Authentication
                val authResult = auth.createUserWithEmailAndPassword(user.email, password).await()
                val uid = authResult.user?.uid ?: throw Exception("Không tìm thấy ID người dùng")

                // Thêm UID vào UserModel và lưu vào Firestore
                val userWithUid = user.copy(uid = uid)
                firestore.collection("users").document(uid).set(userWithUid).await()

                // Cập nhật trạng thái thành công
                _authState.value = AuthState.Authenticated(userWithUid)
            } catch (e: Exception) {
                // Xử lý lỗi và thông báo
                _authState.value = AuthState.Error("Đăng ký thất bại: ${e.message}")
            }
        }
    }

    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun loadUserProfile() {
        val uid = auth.currentUser?.uid ?: run {
            _authState.value = AuthState.Unauthenticated
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val snapshot = firestore.collection("users").document(uid).get().await()
                val user = snapshot.toObject(UserModel::class.java)
                if (user != null) {
                    _authState.value = AuthState.Authenticated(user)
                } else {
                    _authState.value = AuthState.Error("Không tìm thấy thông tin người dùng.")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Lỗi tải thông tin: ${e.message}")
            }
        }
    }


}
sealed class AuthState{
    data class Authenticated(val user: UserModel) : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}