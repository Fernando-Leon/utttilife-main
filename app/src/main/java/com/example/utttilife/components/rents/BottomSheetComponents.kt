package com.example.utttilife.components.rents

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utttilife.navigation.BottomSheetRouter
import com.example.utttilife.navigation.Screen
import com.example.utttilife.screens.DepartmentsView
import com.example.utttilife.screens.ReviewsView

@Composable
fun DetailsDepartment(header: String, subHeader: String, onDirectionsClick: () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){
        // Componente encabezado
        HeaderText(header)
        // Espaciador vertical
        Spacer(modifier = Modifier.height(5.dp))
        // Component subencabezado
        SubHeaderText(subHeader)
        // Espaciador vertical
        Spacer(modifier = Modifier.height(10.dp))
        // Botones de opción para acciones adicionales
        OptionButtons(onDirectionsClick = onDirectionsClick)
        // Espaciador vertical
        Spacer(modifier = Modifier.height(5.dp))
        // Componente de transición que muestra diferentes vistas en función del estado actual
        Crossfade(targetState = BottomSheetRouter.showedScreen, label = "") {
                currentState ->
            // Evaluar el estado actual y mostrar la vista correspondiente
            when(currentState.value){
                is Screen.BottomSheetDepartmentScreen -> {
                    DepartmentsView()  // Vista de departamentos
                }
                is Screen.BottomSheetReviewsScreen -> {
                    ReviewsView()  // Vista de reseñas
                }
            }
        }
    }
}

@Composable
fun HeaderText(textValue: String){
    // Texto con estilo para encabezados
    Text(
        text = textValue,  // Valor del texto del encabezado
        style = TextStyle(
            fontWeight = FontWeight.SemiBold,  // Grosor de la fuente semi-negrita
            fontSize = 20.sp,  // Tamaño de la fuente en escala independiente de píxeles (sp)
        )
    )
}

@Composable
fun SubHeaderText(textValue: String){
    // Texto con estilo para subencabezados
    Text(
        text = textValue,  // Valor del texto del subencabezado
        style = TextStyle(
            fontWeight = FontWeight.Light,  // Grosor de la fuente ligero
            fontSize = 20.sp,  // Tamaño de la fuente en escala independiente de píxeles (sp)
        )
    )
}