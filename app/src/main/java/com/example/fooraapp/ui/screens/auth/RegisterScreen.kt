package com.example.fooraapp.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fooraapp.viewmodel.AuthViewModel

// ── Màu sắc (giữ đồng bộ với LoginScreen) ────────────────
private val BrandOrange   = Color(0xFFFF6B35)
private val BrandDeep     = Color(0xFFE84E0F)
private val SurfaceCard   = Color(0xFFFFFBF8)
private val TextPrimary   = Color(0xFF1A1A2E)
private val TextSecondary = Color(0xFF6B7280)
private val ErrorRed      = Color(0xFFDC2626)
private val FieldBorder   = Color(0xFFE5E7EB)
private val FieldFocus    = Color(0xFFFF6B35)

@Composable
fun RegisterScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    var name            by remember { mutableStateOf("") }
    var phone           by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFFFF3EE), Color(0xFFFFF9F6), Color(0xFFFFF3EE)),
                    start  = Offset(0f, 0f),
                    end    = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        // Vòng trang trí
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = (-50).dp, y = (-50).dp)
                .background(
                    Brush.radialGradient(listOf(BrandOrange.copy(0.15f), Color.Transparent)),
                    RoundedCornerShape(50)
                )
        )
        Box(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-30).dp)
                .background(
                    Brush.radialGradient(listOf(BrandDeep.copy(0.10f), Color.Transparent)),
                    RoundedCornerShape(50)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())   // cuộn khi bàn phím bật lên
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Logo ─────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .shadow(12.dp, RoundedCornerShape(20.dp), ambientColor = BrandOrange.copy(0.3f))
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(BrandOrange, BrandDeep))),
                contentAlignment = Alignment.Center
            ) {
                Text("F", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Black)
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Tạo tài khoản",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = (-0.5).sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Điền thông tin để bắt đầu trải nghiệm",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            // ── Card form ────────────────────────────────
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(20.dp, RoundedCornerShape(24.dp), ambientColor = Color.Black.copy(0.08f)),
                shape = RoundedCornerShape(24.dp),
                color = SurfaceCard,
                tonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    // ── Section label ────────────────────
                    SectionLabel("Thông tin cá nhân")

                    RegisterField(
                        value         = name,
                        onValueChange = { name = it },
                        label         = "Họ và tên",
                        icon          = Icons.Default.Person,
                        keyboardType  = KeyboardType.Text
                    )

                    RegisterField(
                        value         = phone,
                        onValueChange = { phone = it },
                        label         = "Số điện thoại",
                        icon          = Icons.Default.Phone,
                        keyboardType  = KeyboardType.Phone
                    )

                    Divider(color = FieldBorder, thickness = 0.8.dp, modifier = Modifier.padding(vertical = 4.dp))

                    SectionLabel("Thông tin đăng nhập")

                    RegisterField(
                        value         = email,
                        onValueChange = { email = it },
                        label         = "Email",
                        icon          = Icons.Default.Email,
                        keyboardType  = KeyboardType.Email
                    )

                    // Password field với toggle
                    OutlinedTextField(
                        value                = password,
                        onValueChange        = { password = it },
                        label                = { Text("Mật khẩu", fontSize = 14.sp) },
                        singleLine           = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape                = RoundedCornerShape(14.dp),
                        modifier             = Modifier.fillMaxWidth(),
                        leadingIcon          = {
                            Icon(
                                Icons.Default.Lock, null,
                                tint     = if (password.isNotEmpty()) BrandOrange else TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle mật khẩu",
                                    tint     = TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        colors = registerFieldColors()
                    )

                    // Error
                    AnimatedVisibility(
                        visible = authViewModel.errorMessage != null,
                        enter   = fadeIn() + expandVertically(),
                        exit    = fadeOut() + shrinkVertically()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = ErrorRed.copy(alpha = 0.08f)
                        ) {
                            Text(
                                text     = authViewModel.errorMessage ?: "",
                                color    = ErrorRed,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    // ── Nút đăng ký ──────────────────────
                    if (authViewModel.isLoading) {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                color       = BrandOrange,
                                modifier    = Modifier.size(36.dp),
                                strokeWidth = 3.dp
                            )
                        }
                    } else {
                        Button(
                            onClick        = { authViewModel.register(email, password, name, phone) },
                            modifier       = Modifier.fillMaxWidth().height(52.dp),
                            shape          = RoundedCornerShape(14.dp),
                            colors         = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.linearGradient(listOf(BrandOrange, BrandDeep)),
                                        RoundedCornerShape(14.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Tạo tài khoản",
                                    fontWeight    = FontWeight.SemiBold,
                                    fontSize      = 16.sp,
                                    color         = Color.White,
                                    letterSpacing = 0.3.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Đăng nhập ────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Đã có tài khoản?", fontSize = 14.sp, color = TextSecondary)
                TextButton(
                    onClick        = { navController.popBackStack() },
                    contentPadding = PaddingValues(horizontal = 6.dp)
                ) {
                    Text(
                        "Đăng nhập",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = BrandOrange
                    )
                }
            }
        }
    }

    LaunchedEffect(authViewModel.isLoginSuccess) {
        if (authViewModel.isLoginSuccess) {
            navController.navigate("home_screen") {
                popUpTo("login_screen") { inclusive = true }
            }
        }
    }
}

// ── Composable helpers ────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text       = text,
        fontSize   = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color      = BrandOrange,
        letterSpacing = 0.8.sp
    )
}

@Composable
private fun RegisterField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value           = value,
        onValueChange   = onValueChange,
        label           = { Text(label, fontSize = 14.sp) },
        singleLine      = true,
        shape           = RoundedCornerShape(14.dp),
        modifier        = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon     = {
            Icon(
                icon, null,
                tint     = if (value.isNotEmpty()) BrandOrange else TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        },
        colors = registerFieldColors()
    )
}

@Composable
private fun registerFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor      = FieldFocus,
    unfocusedBorderColor    = FieldBorder,
    focusedLabelColor       = FieldFocus,
    unfocusedLabelColor     = TextSecondary,
    cursorColor             = BrandOrange,
    focusedContainerColor   = Color.White,
    unfocusedContainerColor = Color(0xFFFAFAFA)
)