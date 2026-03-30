package com.example.fooraapp.ui.screens.auth

// 2. Import các thư viện cần thiết cho UI và Logic
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fooraapp.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    // --- KHỐI QUẢN LÝ TRẠNG THÁI (STATE) ---
    // Dùng 'remember' để giữ lại giá trị khi màn hình vẽ lại (re-composition)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // --- BỐ CỤC GIAO DIỆN (UI LAYOUT) ---
    Column(
        modifier = Modifier
            .fillMaxSize()           // Chiếm toàn bộ màn hình
            .padding(24.dp),         // Khoảng cách lề 24dp
        horizontalAlignment = Alignment.CenterHorizontally, // Căn giữa theo chiều ngang
        verticalArrangement = Arrangement.Center           // Căn giữa theo chiều dọc
    ) {
        // Tiêu đề chính của màn hình
        Text(
            text = "Tạo tài khoản mới",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary // Sử dụng màu Cam đã cấu hình
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Ô nhập Họ và tên: Kết nối trực tiếp với biến 'name'
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Họ và tên") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Ô nhập Số điện thoại: Dùng để liên hệ giao hàng trong ERD
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Ô nhập Email: Dùng làm định danh đăng nhập Firebase
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Ô nhập Mật khẩu: Sử dụng PasswordVisualTransformation để ẩn ký tự (****)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // --- KHỐI HIỂN THỊ LỖI ---
        // Nếu AuthViewModel có lỗi (ví dụ: sai định dạng email), dòng chữ này sẽ hiện ra
        authViewModel.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- KHỐI XỬ LÝ NÚT BẤM (LOGIC BUTTON) ---
        // Kiểm tra xem hệ thống có đang bận xử lý (isLoading) không
        if (authViewModel.isLoading) {
            // Hiện vòng xoay chờ nếu đang gửi dữ liệu lên Firebase
            CircularProgressIndicator()
        } else {
            // Hiện nút bấm bình thường nếu không bận
            Button(
                onClick = {
                    // Gọi hàm register đã viết trong AuthViewModel
                    authViewModel.register(email, password, name, phone)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Đăng ký ngay")
            }
        }

        // Nút quay lại: Dùng popBackStack để quay về màn hình trước đó (Login)
        TextButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Đã có tài khoản? Đăng nhập ngay")
        }
    }

    // --- KHỐI ĐIỀU HƯỚNG TỰ ĐỘNG ---
    // Lắng nghe biến isLoginSuccess, nếu true thì tự động chuyển trang
    LaunchedEffect(authViewModel.isLoginSuccess) {
        if (authViewModel.isLoginSuccess) {
            navController.navigate("home_screen") {
                // Xóa màn hình Login và Register khỏi hàng đợi để người dùng không bấm Back quay lại được
                popUpTo("login_screen") { inclusive = true }
            }
        }
    }
}