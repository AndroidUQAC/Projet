package com.example.projetuqac

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.projetuqac.ui.theme.ProjetUqacTheme
import com.example.projetuqac.ui.mainscreen.MainActivity
import kotlinx.coroutines.delay

class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(Unit) {
                delay(1000);
                Intent(applicationContext, MainActivity::class.java).also {
                    startActivity(it)
                }
            }
            ProjetUqacTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting2()
                }
            }
        }
    }
}

@Composable
fun Greeting2(modifier: Modifier = Modifier) {
    Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Launched screen")
}