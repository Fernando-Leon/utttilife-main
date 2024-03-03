package com.example.utttilife.components.rents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.utttilife.R.*
import com.example.utttilife.navigation.BottomSheetRouter
import com.example.utttilife.navigation.Screen

@Composable
fun OptionButtons(onDirectionsClick: () -> Unit){
    // Estado de desplazamiento para la fila de botones
    val scrollState = rememberScrollState()
    // Fila que contiene los botones de opción
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),  // Permite el desplazamiento horizontal
        horizontalArrangement = Arrangement.SpaceEvenly  // Distribución horizontal uniforme
    ) {
        Spacer(modifier = Modifier.width(5.dp))  // Espaciador
        // Botón genérico para mostrar los departamentos
        GenericOutlinedButton(
            name = "Departamentos",  // Nombre del botón
            painterResource = painterResource(id = drawable.home_work),  // Icono del botón
            backColor = Color.Transparent,  // Color de fondo transparente
            color = MaterialTheme.colorScheme.secondary  // Color del texto del botón
        ) {
            BottomSheetRouter.navigateTo(Screen.BottomSheetDepartmentScreen)  // Navega a la pantalla de departamentos
        }
        Spacer(modifier = Modifier.width(5.dp))  // Espaciador
        // Botón genérico para mostrar las opiniones
        GenericOutlinedButton(
            name = "Opiniones",  // Nombre del botón
            painterResource = painterResource(id = drawable.reviews),  // Icono del botón
            backColor = Color.Transparent,  // Color de fondo transparente
            color = MaterialTheme.colorScheme.secondary  // Color del texto del botón
        ) {
            BottomSheetRouter.navigateTo(Screen.BottomSheetReviewsScreen)  // Navega a la pantalla de opiniones
        }
        Spacer(modifier = Modifier.width(5.dp))  // Espaciador
        // Botón genérico para la función "Cómo llegar"
        GenericOutlinedButton(
            name = "Como llegar",  // Nombre del botón
            painterResource = painterResource(id = drawable.map),  // Icono del botón
            backColor = Color.Transparent,  // Color de fondo transparente
            color = MaterialTheme.colorScheme.secondary  // Color del texto del botón
        ) {
            onDirectionsClick()  // Ejecuta la acción de "Cómo llegar" proporcionada por el parámetro
        }
    }
}


@Composable
fun GenericOutlinedButton(
    name: String,  // Texto del botón
    painterResource: Painter,  // Icono del botón
    backColor: Color,  // Color de fondo del botón
    color: Color,  // Color del texto del botón
    onClick: () -> Unit  // Acción que se ejecuta al hacer clic en el botón
){
    // Botón con borde delgado y esquinas redondeadas
    OutlinedButton(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),  // Borde del botón
        shape = RoundedCornerShape(50),  // Forma redondeada para las esquinas
        onClick = onClick,  // Acción de clic del botón
        colors = ButtonDefaults.buttonColors(  // Colores personalizados para el botón
            contentColor = color,  // Color del texto del botón
            containerColor = backColor  // Color de fondo del botón

        ),
    ) {
        // Icono del botón
        Icon(painter = painterResource, contentDescription = "")
        // Espaciador horizontal
        Spacer(modifier = Modifier.width(2.dp))
        // Texto del botón
        Text(text = name)
    }
}