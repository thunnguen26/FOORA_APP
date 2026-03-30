package com.example.fooraapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.fooraapp.utils.Constants
import kotlinx.coroutines.tasks.await

class AuthRepository{
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()


    fun isUserLoggedIn() = auth.currentUser != null

    fun getCurrenUser() = auth.currentUser?.uid

    fun logout() = auth.signOut()



}