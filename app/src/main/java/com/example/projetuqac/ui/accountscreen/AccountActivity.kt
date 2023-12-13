package com.example.projetuqac.ui.accountscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import kotlin.reflect.KFunction0


@Composable
fun AccountActivity(
    name: String,
    modifier: Modifier = Modifier,
    user: String
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Hello $user!")
        Button(onClick = {
            FirebaseAuth.getInstance().signOut()
        }) {
            Text(text = "Sign out")
        }
    }
}