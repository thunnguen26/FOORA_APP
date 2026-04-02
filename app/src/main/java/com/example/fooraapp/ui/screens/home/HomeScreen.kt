package com.example.fooraapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fooraapp.ui.components.ProductCard
import com.example.fooraapp.ui.theme.*
import com.example.fooraapp.viewmodel.HomeViewModel
import com.example.fooraapp.data.model.Product

data class FoodCategory(val emoji: String, val name: String)

private val categories = listOf(
    FoodCategory("🍜", "Cơm"),
    FoodCategory("🍕", "Pizza"),
    FoodCategory("🥗", "Healthy"),
    FoodCategory("🍣", "Sushi"),
    FoodCategory("🍔", "Burger"),
    FoodCategory("🧋", "Trà sữa"),
    FoodCategory("🍰", "Tráng miệng"),
)

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = viewModel(),
    cartViewModel: com.example.fooraapp.viewmodel.CartViewModel = viewModel() // THÊM DÒNG NÀY
) {
    var searchQuery      by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Cơm") }

    Scaffold(
        containerColor = BgLight,
        bottomBar      = { BottomNavBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ── Header + Search + Banner + Category ──────
            Column(
                modifier = Modifier
                    .background(BgLight)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(16.dp))
                HomeHeader(onCartClick = { navController.navigate("cart_screen") })
                Spacer(Modifier.height(20.dp))
                HomeSearchBar(query = searchQuery, onChange = { searchQuery = it })
                Spacer(Modifier.height(20.dp))
                PromoBanner()
                Spacer(Modifier.height(20.dp))

                Text("Danh mục", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(Modifier.height(12.dp))
                CategoryRow(selected = selectedCategory, onSelect = { selectedCategory = it })
                Spacer(Modifier.height(20.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text("Món phổ biến", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(
                        "Xem tất cả",
                        fontSize   = 13.sp,
                        color      = BrandOrange,
                        fontWeight = FontWeight.SemiBold,
                        modifier   = Modifier.clickable { }
                    )
                }
                Spacer(Modifier.height(12.dp))
            }

            // ── Grid sản phẩm ─────────────────────────────
            LazyVerticalGrid(
                columns               = GridCells.Fixed(2),
                contentPadding        = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement   = Arrangement.spacedBy(12.dp),
                modifier              = Modifier.fillMaxSize()
            ) {
                // Trong HomeScreen.kt
                items(items = homeViewModel.productList) { product ->
                    ProductCard(
                        product = product,
                        onAddClick = {
                            cartViewModel.addProductToCart(product)
                        },
                        onClick = {
                            // Kiểm tra: Nếu có ID thì mới chuyển trang, tránh bị crash
                            if (product.id.isNotEmpty()) {
                                navController.navigate("detail_screen/${product.id}")
                            } else {
                                println("Lỗi: Món ăn này chưa có ID!")
                            }
                        }
                    )
                }
                item { Spacer(Modifier.height(8.dp)) }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}

// ── Header ────────────────────────────────────────────────────
@Composable
private fun HomeHeader(onCartClick: () -> Unit) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Column {
            Text("Xin chào! 👋", fontSize = 13.sp, color = TextSecondary)
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, tint = BrandOrange, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(3.dp))
                Text("Đà Nẵng, Việt Nam", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .shadow(8.dp, CircleShape, ambientColor = BrandOrange.copy(0.2f))
                .clip(CircleShape)
                .background(SurfaceCard)
                .clickable { onCartClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.ShoppingCart, "Giỏ hàng", tint = BrandOrange, modifier = Modifier.size(22.dp))
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 2.dp, y = (-2).dp)
                    .clip(CircleShape)
                    .background(BrandDeep),
                contentAlignment = Alignment.Center
            ) {
                Text("2", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ── Search Bar ────────────────────────────────────────────────
@Composable
private fun HomeSearchBar(query: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value           = query,
        onValueChange   = onChange,
        placeholder     = { Text("Bạn muốn ăn gì hôm nay?", fontSize = 14.sp, color = TextSecondary) },
        leadingIcon     = {
            Icon(Icons.Default.Search, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
        },
        trailingIcon    = {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brush.linearGradient(listOf(BrandOrange, BrandDeep)))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Tune, null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        },
        singleLine      = true,
        shape           = RoundedCornerShape(16.dp),
        modifier        = Modifier.fillMaxWidth(),
        colors          = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor    = FieldBorder,
            focusedBorderColor      = BrandOrange,
            focusedContainerColor   = SurfaceCard,
            unfocusedContainerColor = SurfaceCard,
            cursorColor             = BrandOrange
        )
    )
}

// ── Promo Banner ──────────────────────────────────────────────
@Composable
private fun PromoBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .shadow(12.dp, RoundedCornerShape(20.dp), ambientColor = BrandOrange.copy(0.25f))
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(BrandOrange, BrandDeep),
                    start  = Offset(0f, 0f),
                    end    = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            )
    ) {
        // Vòng trang trí nền
        Box(
            modifier = Modifier
                .size(140.dp).align(Alignment.CenterEnd).offset(x = 30.dp)
                .background(Brush.radialGradient(listOf(Color.White.copy(0.12f), Color.Transparent)), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(90.dp).align(Alignment.BottomEnd).offset(x = (-20).dp, y = 20.dp)
                .background(Brush.radialGradient(listOf(Color.White.copy(0.08f), Color.Transparent)), CircleShape)
        )

        Row(
            modifier              = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Surface(shape = RoundedCornerShape(6.dp), color = Color.White.copy(alpha = 0.25f)) {
                    Text(
                        "🔥 Ưu đãi hôm nay",
                        fontSize   = 11.sp,
                        color      = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        modifier   = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text("Giảm 30%", fontSize = 26.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = (-0.5).sp)
                Text("Đơn hàng đầu tiên", fontSize = 13.sp, color = Color.White.copy(0.85f))
                Spacer(Modifier.height(10.dp))
                Surface(shape = RoundedCornerShape(8.dp), color = SurfaceCard, modifier = Modifier.clickable { }) {
                    Text(
                        "Đặt ngay",
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color      = BrandDeep,
                        modifier   = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
            Text("🍜", fontSize = 64.sp)
        }
    }
}

// ── Category Chips ────────────────────────────────────────────
@Composable
private fun CategoryRow(selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier              = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        categories.forEach { cat ->
            val isSelected = cat.name == selected
            Surface(
                shape    = RoundedCornerShape(14.dp),
                color    = if (isSelected) BrandOrange else SurfaceCard,
                modifier = Modifier
                    .shadow(
                        elevation    = if (isSelected) 6.dp else 2.dp,
                        shape        = RoundedCornerShape(14.dp),
                        ambientColor = BrandOrange.copy(if (isSelected) 0.3f else 0.05f)
                    )
                    .clickable { onSelect(cat.name) }
            ) {
                Row(
                    modifier              = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(cat.emoji, fontSize = 16.sp)
                    Text(
                        cat.name,
                        fontSize   = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color      = if (isSelected) Color.White else TextPrimary
                    )
                }
            }
        }
    }
}

// ── Bottom Navigation Bar ─────────────────────────────────────
@Composable
private fun BottomNavBar(navController: NavHostController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        color = SurfaceCard
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            NavBarItem(icon = Icons.Default.Home,          label = "Trang chủ", selected = true)
            NavBarItem(icon = Icons.Default.Search,        label = "Tìm kiếm",  selected = false,
                onClick = { navController.navigate("search_screen") })
            NavBarItem(icon = Icons.Default.Receipt,       label = "Đơn hàng",  selected = false,
                onClick = { navController.navigate("order_screen") })
            NavBarItem(icon = Icons.Default.AccountCircle, label = "Tài khoản", selected = false,
                onClick = { navController.navigate("profile_screen") })
        }
    }
}

@Composable
private fun NavBarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier            = Modifier.clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (selected) {
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BrandOrange.copy(alpha = 0.12f))
                )
            }
            Icon(
                icon,
                contentDescription = label,
                tint     = if (selected) BrandOrange else TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.height(3.dp))
        Text(
            label,
            fontSize   = 10.sp,
            color      = if (selected) BrandOrange else TextSecondary,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}