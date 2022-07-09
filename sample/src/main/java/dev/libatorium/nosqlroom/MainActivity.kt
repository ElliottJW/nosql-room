package dev.libatorium.nosqlroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.libatorium.nosqlroom.ui.app.SampleApp
import dev.libatorium.nosqlroom.ui.app.SampleAppViewModel
import dev.libatorium.nosqlroom.ui.theme.NoSqlRoomSampleTheme

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sampleAppViewModel: SampleAppViewModel = hiltViewModel()
            SampleApp(
                sampleAppViewModel = sampleAppViewModel
            )
        }
    }
}