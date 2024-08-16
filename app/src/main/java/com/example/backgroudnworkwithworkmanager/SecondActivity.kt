package com.example.backgroudnworkwithworkmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.backgroudnworkwithworkmanager.ui.theme.BackgroudnWorkWithWorkManagerTheme

class SecondActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            BackgroudnWorkWithWorkManagerTheme {

                Text(
                    text = "Second Activity",
                    modifier = Modifier.fillMaxSize().padding(24.dp)
                )


            }
        }

    }
}
