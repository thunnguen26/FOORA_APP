package com.example.fooraapp.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.fooraapp.model.Product
import com.example.fooraapp.ui.theme.*   // ← import màu từ theme

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    var isFav by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(18.dp), ambientColor = Color.Black.copy(0.06f))
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = SurfaceCard
    ) {
        Column {

            // ── Ảnh sản phẩm ─────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                    .background(Color(0xFFF5F5F5))
            ) {
                SubcomposeAsyncImage(
                    model              = product.mainImageUrl,
                    contentDescription = product.name,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.fillMaxSize(),
                    loading = {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFFF0F0F0), Color(0xFFE8E8E8), Color(0xFFF0F0F0))
                                    )
                                )
                        )
                    },
                    error = {
                        Box(
                            Modifier.fillMaxSize().background(Color(0xFFF5F5F5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.BrokenImage,
                                contentDescription = null,
                                tint     = FieldBorder,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                )

                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(0.18f))
                            )
                        )
                )

                // Badge category
                if (product.category.isNotEmpty()) {
                    Surface(
                        shape    = RoundedCornerShape(6.dp),
                        color    = BrandOrange,
                        modifier = Modifier.padding(8.dp).align(Alignment.TopStart)
                    ) {
                        Text(
                            text       = product.category,
                            fontSize   = 9.sp,
                            color      = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier   = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Nút yêu thích
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(30.dp)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.92f))
                        .clickable { isFav = !isFav },
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (isFav) "❤️" else "🤍", fontSize = 14.sp)
                }
            }

            // ── Thông tin sản phẩm ───────────────────────
            Column(modifier = Modifier.padding(10.dp)) {

                Text(
                    text       = product.name,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = TextPrimary,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(3.dp))

                if (product.description.isNotEmpty()) {
                    Text(
                        text     = product.description,
                        fontSize = 11.sp,
                        color    = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                }

                // Rating + thời gian giao
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Star, null, tint = BrandYellow, modifier = Modifier.size(12.dp))
                    Text(
                        text       = product.rating.toString(),
                        fontSize   = 11.sp,
                        color      = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Text("·", fontSize = 11.sp, color = TextSecondary)
                    Text(product.deliveryTime, fontSize = 11.sp, color = TextSecondary)
                }

                Spacer(Modifier.height(8.dp))

                // Giá + nút thêm giỏ
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "%,dđ".format(product.price).replace(',', '.'),
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color      = BrandOrange
                    )

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.linearGradient(listOf(BrandOrange, BrandDeep)))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, "Thêm vào giỏ", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}