package com.example.fooraapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooraapp.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    // Sử dụng Repository để đúng kiến trúc MVVM
    private val repository = AuthRepository()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Kiểm tra trạng thái đăng nhập ngay khi khởi tạo
    val isUserAuthenticated = repository.isUserLoggedIn()

    // Quản lý trạng thái UI bằng Delegate "by" (giúp code UI sạch hơn)
    var isLoading by mutableStateOf(false)
        private set // Chỉ ViewModel mới được sửa

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoginSuccess by mutableStateOf(false)
        private set

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            errorMessage = "Vui lòng nhập đầy đủ thông tin"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    isLoginSuccess = true
                    isLoading = false
                }
                .addOnFailureListener {
                    errorMessage = it.localizedMessage ?: "Lỗi đăng nhập không xác định"
                    isLoading = false
                }
        }
    }

    // Thêm hàm này vào trong class AuthViewModel
    fun register(email: String, pass: String, name: String, phone: String) {
        // 1. Kiểm tra đầu vào cơ bản để tránh gửi dữ liệu rỗng lên server
        if (email.isBlank() || pass.isBlank() || name.isBlank() || phone.isBlank()) {
            errorMessage = "Vui lòng điền đầy đủ tất cả các trường"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            // 2. Tạo tài khoản trên Firebase Authentication
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid ?: ""

                    // 3. Tạo một đối tượng Map để lưu vào Firestore (đúng theo ERD USERS)
                    val userDate = hashMapOf(
                        "uid" to uid,
                        "displayName" to name,
                        "email" to email,
                        "phone" to phone,
                        "role" to "user", // Mặc định là khách hàng
                        "createdAt" to System.currentTimeMillis(),
                        "isActive" to true
                    )

                    // 4. Lưu dữ liệu vào Collection "Users" với ID chính là UID của Auth
                    val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    db.collection("Users").document(uid).set(userDate)
                        .addOnSuccessListener {
                            isLoginSuccess = true // Đăng ký xong coi như đăng nhập luôn
                            isLoading = false
                        }
                        .addOnFailureListener { e ->
                            errorMessage = "Lưu dữ liệu thất bại: ${e.localizedMessage}"
                            isLoading = false
                        }
                }
                .addOnFailureListener { e ->
                    errorMessage = "Đăng ký thất bại: ${e.localizedMessage}"
                    isLoading = false
                }
        }
    }

    // Hàm để reset thông báo lỗi khi cần
    fun clearError() {
        errorMessage = null
    }
}