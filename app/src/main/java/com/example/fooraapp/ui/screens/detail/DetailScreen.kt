package com.example.fooraapp.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.fooraapp.ui.theme.*
import com.example.fooraapp.viewmodel.DetailViewModel

@Composable
fun DetailScreen(
    navController: NavHostController,
    productId: String,
    viewModel: DetailViewModel = viewModel()
) {
    // 1. Vừa mở màn hình lên là gọi ViewModel đi lấy dữ liệu ngay
    LaunchedEffect(productId) {
        viewModel.fetchProductById(productId)
    }

    val product = viewModel.product

    // 2. Nếu dữ liệu chưa tải xong (product == null), hiện vòng xoay chờ
    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = BrandOrange)
        }
    } else {
        // 3. Đã có dữ liệu, bắt đầu vẽ giao diện
        Scaffold(
            containerColor = BgLight,
            bottomBar = {
                BottomOrderBar(
                    price = product.price,
                    quantity = viewModel.quantity,
                    onIncrease = { viewModel.increaseQuantity() },
                    onDecrease = { viewModel.decreaseQuantity() },
                    onAddToCart = {
                        viewModel.addToCart { success ->
                            if (success) {
                                // Thêm thành công thì quay về trang trước
                                navController.popBackStack()
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // ── Phần 1: Ảnh món ăn & Nút Back ──
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                ) {
                    AsyncImage(
                        model = product.mainImageUrl,
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Nút Back
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .padding(top = 40.dp, start = 16.dp)
                            .background(Color.White.copy(alpha = 0.8f), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = TextPrimary)
                    }
                }

                // ── Phần 2: Thông tin chi tiết món ăn ──
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = product.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Đánh giá và Thời gian giao hàng
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = BrandYellow, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${product.rating}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = " · ${product.deliveryTime}",
                            fontSize = 15.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Mô tả",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = product.description.ifEmpty { "Món ăn này chưa có mô tả chi tiết." },
                        fontSize = 15.sp,
                        color = TextSecondary,
                        lineHeight = 22.sp
                    )

                    // Thêm khoảng trống ở cuối để không bị lấp bởi BottomBar
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

// ── Thành phần thanh đặt hàng ở dưới cùng ──
@Composable
fun BottomOrderBar(
    price: Long,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onAddToCart: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 16.dp,
        color = SurfaceCard,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cụm tăng/giảm số lượng
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(BgLight, RoundedCornerShape(50.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                IconButton(onClick = onDecrease, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Remove, contentDescription = "Giảm", tint = BrandOrange)
                }
                Text(
                    text = "$quantity",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                IconButton(onClick = onIncrease, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Tăng", tint = BrandOrange)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Nút Thêm vào giỏ
            val totalPrice = price * quantity
            Button(
                onClick = onAddToCart,
                colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
            ) {
                Text(
                    text = "Thêm - %,dđ".format(totalPrice).replace(',', '.'),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}