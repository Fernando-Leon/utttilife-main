package com.example.utttilife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utttilife.navigation.AppNavigation
import com.example.utttilife.ui.theme.UtttilifeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            UtttilifeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun TopBar(){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Image(
            painter = painterResource(id = R.drawable.info_icon),
            modifier = Modifier.size(30.dp, 30.dp),
            contentDescription = null)
        Row {
            Text(text = "UTTT", color = Color.Green, fontSize = 30.sp, fontWeight = FontWeight.Black)
            Text(text = "ILIFE", fontSize = 30.sp, fontWeight = FontWeight.Black)
        }
        Image(
            painter = painterResource(id = R.drawable.acount_icon),
            modifier = Modifier.size(30.dp, 30.dp),
            contentDescription = null)
    }
}