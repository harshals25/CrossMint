package com.example.crossmint

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.crossmint.network.MegaverseRetrofit
import com.example.crossmint.repository.MegaverseRepository
import com.example.crossmint.ui.theme.CrossMintTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CrossMintTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        val repo = MegaverseRepository(MegaverseRetrofit.api)
        lifecycleScope.launch {
            try {
                val candidateId = "9cbfae8d-9dc1-4237-af7f-18d81b4a7b3d"
//                runPhase1(candidateId, repo) // already done
                runPhase2(candidateId,repo)
                Log.d("Megaverse", "Phase 1 done MainActivity")
            } catch (t: Throwable) {
                Log.e("Megaverse", "Phase 1 failed: ${t.message}", t)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CrossMintTheme {
        Greeting("Android")
    }
}