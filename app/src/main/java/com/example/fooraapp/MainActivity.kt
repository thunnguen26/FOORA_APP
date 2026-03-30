package com.example.fooraapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.fooraapp.ui.navigation.SetupNavGraph
import com.example.fooraapp.ui.theme.FooraAppTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FooraAppTheme {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}
@Composable
fun TestFirestoreScreen(db: FirebaseFirestore, onShowToast: (String) -> Unit) {
        var foodName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = foodName,
            onValueChange = { foodName = it },
            label = { Text("Nhập tên món ăn để test") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (foodName.isNotEmpty()) {
                    // Tạo một object để gửi lên
                    val data = hashMapOf("name" to foodName)

                    // Gửi lên collection tên là "TestProducts"
                    db.collection("TestProducts")
                        .add(data)
                        .addOnSuccessListener {
                            onShowToast("Gửi thành công!")
                            foodName = "" // Xóa text sau khi gửi
                        }
                        .addOnFailureListener { e ->
                            onShowToast("Lỗi: ${e.message}")
                        }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gửi dữ liệu lên Firestore")
        }
    }
}