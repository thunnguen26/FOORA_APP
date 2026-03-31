package com.example.fooraapp.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

// ── Màu sắc ───────────────────────────────────────────────
private val BrandOrange   = Color(0xFFFF6B35)
private val BrandDeep     = Color(0xFFE84E0F)
private val SurfaceCard   = Color(0xFFFFFBF8)
private val TextPrimary   = Color(0xFF1A1A2E)
private val TextSecondary = Color(0xFF6B7280)
private val ErrorRed      = Color(0xFFDC2626)
private val FieldBorder   = Color(0xFFE5E7EB)
private val FieldFocus    = Color(0xFFFF6B35)

@Composable
fun LoginScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    var email          by remember { mutableStateOf("") }
    var password       by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // ── Nền gradient ─────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFFFF3EE), Color(0xFFFFF9F6), Color(0xFFFFF3EE)),
                    start = Offset(0f, 0f),
                    end   = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        // Vòng trang trí phía trên
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = (-60).dp, y = (-60).dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(BrandOrange.copy(alpha = 0.15f), Color.Transparent)
                    ),
                    shape = RoundedCornerShape(50)
                )
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopEnd)
                .offset(x = 60.dp, y = (-40).dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(BrandDeep.copy(alpha = 0.10f), Color.Transparent)
                    ),
                    shape = RoundedCornerShape(50)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── Logo / Tiêu đề ───────────────────────────
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .shadow(12.dp, RoundedCornerShape(20.dp), ambientColor = BrandOrange.copy(0.3f))
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(listOf(BrandOrange, BrandDeep))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("F", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Black)
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Chào mừng trở lại",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = (-0.5).sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Đăng nhập vào tài khoản FooraApp",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(36.dp))

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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Email field
                    StyledTextField(
                        value         = email,
                        onValueChange = { email = it },
                        label         = "Email",
                        leadingIcon   = {
                            Icon(Icons.Default.Email, null,
                                tint = if (email.isNotEmpty()) BrandOrange else TextSecondary,
                                modifier = Modifier.size(20.dp))
                        },
                        keyboardType  = KeyboardType.Email
                    )

                    // Password field
                    StyledTextField(
                        value         = password,
                        onValueChange = { password = it },
                        label         = "Mật khẩu",
                        leadingIcon   = {
                            Icon(Icons.Default.Lock, null,
                                tint = if (password.isNotEmpty()) BrandOrange else TextSecondary,
                                modifier = Modifier.size(20.dp))
                        },
                        trailingIcon  = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        keyboardType = KeyboardType.Password
                    )

                    // Error message
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

                    // Login button
                    if (authViewModel.isLoading) {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                color     = BrandOrange,
                                modifier  = Modifier.size(36.dp),
                                strokeWidth = 3.dp
                            )
                        }
                    } else {
                        Button(
                            onClick  = { authViewModel.login(email, password) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape    = RoundedCornerShape(14.dp),
                            colors   = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
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
                                    "Đăng nhập",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize   = 16.sp,
                                    color      = Color.White,
                                    letterSpacing = 0.3.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Đăng ký
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Chưa có tài khoản?", fontSize = 14.sp, color = TextSecondary)
                TextButton(
                    onClick      = { navController.navigate("register_screen") },
                    contentPadding = PaddingValues(horizontal = 6.dp)
                ) {
                    Text(
                        "Đăng ký",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = BrandOrange
                    )
                }
            }
        }
    }

    // Điều hướng khi đăng nhập thành công
    LaunchedEffect(authViewModel.isLoginSuccess) {
        if (authViewModel.isLoginSuccess) {
            navController.navigate("home_screen") {
                popUpTo("login_screen") { inclusive = true }
            }
        }
    }
}

// ── Reusable styled text field ────────────────────────────────────────────────
@Composable
private fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value               = value,
        onValueChange       = onValueChange,
        label               = { Text(label, fontSize = 14.sp) },
        leadingIcon         = leadingIcon,
        trailingIcon        = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions     = KeyboardOptions(keyboardType = keyboardType),
        singleLine          = true,
        shape               = RoundedCornerShape(14.dp),
        modifier            = Modifier.fillMaxWidth(),
        colors              = OutlinedTextFieldDefaults.colors(
            focusedBorderColor      = FieldFocus,
            unfocusedBorderColor    = FieldBorder,
            focusedLabelColor       = FieldFocus,
            unfocusedLabelColor     = TextSecondary,
            cursorColor             = BrandOrange,
            focusedContainerColor   = Color.White,
            unfocusedContainerColor = Color(0xFFFAFAFA)
        )
    )
}