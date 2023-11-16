package com.example.projetuqac.ui.mainscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.example.projetuqac.ui.ApiViewModel
import com.example.projetuqac.db.data.LocalDataPosts
import com.example.projetuqac.ui.MyNavbar
import com.example.projetuqac.ui.UiState
import com.example.projetuqac.ui.theme.ProjetUqacTheme
import com.example.projetuqac.ui.utils.DevicePosture
import com.example.projetuqac.ui.utils.isBookPosture
import com.example.projetuqac.ui.utils.isSeparating
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ApiViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val devicePostureFlow =  WindowInfoTracker.getOrCreate(this).windowLayoutInfo(this)
            .flowWithLifecycle(this.lifecycle)
            .map { layoutInfo ->
                val foldingFeature =
                    layoutInfo.displayFeatures
                        .filterIsInstance<FoldingFeature>()
                        .firstOrNull()
                when {
                    isBookPosture(foldingFeature) ->
                        DevicePosture.BookPosture(foldingFeature.bounds)

                    isSeparating(foldingFeature) ->
                        DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

                    else -> DevicePosture.NormalPosture
                }
            }
            .stateIn(
                scope = lifecycleScope,
                started = SharingStarted.Eagerly,
                initialValue = DevicePosture.NormalPosture
            )

        setContent {
            ProjetUqacTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val windowSize = calculateWindowSizeClass(this)
                    val uiState = viewModel.uiState.value
                    val devicePosture = devicePostureFlow.collectAsState().value
                    MyNavbar(uiState = uiState, windowSize = windowSize.widthSizeClass, foldingDevicePosture = devicePosture)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier, uiState: UiState, windowSize: WindowWidthSizeClass) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = "Projet UQAC", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            })
        },
        modifier = Modifier.drawBehind {
            val strokeWidth = 2.dp.toPx()
            val y = size.height - strokeWidth / 2
            drawLine(
                color = Color.LightGray,
                strokeWidth = strokeWidth,
                start = Offset(0f, y),
                end = Offset(size.width, y)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                if (uiState.posts != null) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(uiState.posts) { post ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Transparent)
                            ) {
                                Text(
                                    text = post.title,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentSize(Alignment.Center)
                                        .padding(16.dp),
                                )
                                Divider(
                                    color = Color.LightGray,
                                    thickness = 1.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                } else {
                    Text(text = uiState.error!!, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompactPreview() {
    ProjetUqacTheme {
        MainScreen(
            uiState = UiState(
                isLoading = false,
                posts = LocalDataPosts.getPosts()
            ),
            windowSize = WindowWidthSizeClass.Compact
        )
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun MediumPreviewDark() {
    ProjetUqacTheme() {
        MainScreen(
            uiState = UiState(
                isLoading = false,
                posts = LocalDataPosts.getPosts()
            ),
            windowSize = WindowWidthSizeClass.Medium
        )
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun ExpandedPreviewDark() {
    ProjetUqacTheme() {
        MainScreen(
            uiState = UiState(
                isLoading = false,
                posts = LocalDataPosts.getPosts()
            ),
            windowSize = WindowWidthSizeClass.Expanded
        )
    }
}