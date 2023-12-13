package com.example.projetuqac.ui.historyscreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HistoryScreen(name: String, modifier: Modifier = Modifier, user: Any) {
    Text(
        text = name,
        modifier = modifier
    )
}