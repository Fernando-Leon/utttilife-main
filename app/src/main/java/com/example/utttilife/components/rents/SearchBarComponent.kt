package com.example.utttilife.components.rents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.utttilife.R

@Composable
fun SearchBar(){
    // Estado para almacenar el valor del texto en el campo de búsqueda
    val textValue = remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        value = textValue.value,  // Valor del campo de texto
        onValueChange = {
            textValue.value = it  // Actualiza el valor del campo de texto cuando cambia
        },
        leadingIcon = {
            // Icono que precede al campo de texto
            Icon(painter = painterResource(id = R.drawable.search), contentDescription = "")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(IntrinsicSize.Min)  // Altura mínima
            .clip(RoundedCornerShape(50))  // Forma redondeada para los bordes
            .background(colorResource(id = R.color.search_bar)),  // Color de fondo del campo de búsqueda
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),  // Opciones del teclado virtual
        keyboardActions = KeyboardActions(onSearch = {
            /* Define la acción de búsqueda aquí */  // Acción que se ejecuta cuando se presiona el botón de búsqueda en el teclado
        }),
        singleLine = true,  // Especifica que el campo de texto es de una sola línea
        placeholder = { Text(text = "Buscar renta") },  // Texto de marcador de posición
        shape = RoundedCornerShape(50)  // Forma redondeada para los bordes del campo de texto
    )
}