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
import androidx.navigation.NavHostController
import com.example.utttilife.data.Responses.ResponsesRents.Opinion
import com.example.utttilife.data.Responses.ResponsesRents.Tenant
import com.example.utttilife.navigation.BottomSheetRouter
import com.example.utttilife.navigation.Screen
import com.example.utttilife.screens.DepartmentsView
import com.example.utttilife.screens.ReviewsView

@Composable
fun DetailsDepartment(navController: NavHostController, id: String, coordinates: List<Double>, name: String, address: String, opinions: List<Opinion>, tenant: Tenant, onDirectionsClick: () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){
        // Componente encabezado
        HeaderText(name)
        // Espaciador vertical
        Spacer(modifier = Modifier.height(5.dp))
        // Component subencabezado
        SubHeaderText(address)
        // Espaciador vertical
        Spacer(modifier = Modifier.height(10.dp))
        // Component subencabezado
        SubHeaderText(tenant.name)
        // Espaciador vertical
        Spacer(modifier = Modifier.height(10.dp))
        // Component subencabezado
        SubHeaderText(tenant.email)
        // Espaciador vertical
        Spacer(modifier = Modifier.height(10.dp))
        // Component subencabezado
        SubHeaderText(tenant.phone)
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
                    DepartmentsView(navController, coordinates)  // Vista de departamentos
                }
                is Screen.BottomSheetReviewsScreen -> {
                    ReviewsView(id, opinions)  // Vista de reseñas
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