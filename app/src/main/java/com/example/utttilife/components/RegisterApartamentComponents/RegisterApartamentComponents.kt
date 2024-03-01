package com.example.utttilife.components.RegisterApartamentComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.utttilife.components.GaleryViewComponents.GalleryView
import com.example.utttilife.components.GetCordinates.GetCordinatesComponent


@Composable
fun RegisterApartamentComponents(){

    val buttonColor = Color(0xFF02A787) // Color del botón 'Registrarme' y del borde del botón 'Iniciar Sesión'
    val buttonShape = RoundedCornerShape(4.dp) // Forma redondeada para el borde y el botón

    var selectedOption = remember { mutableStateOf("Casa") }

    Spacer(modifier = Modifier.height(56.dp))
    Text("Registro de departamento", style = MaterialTheme.typography.headlineLarge)
    Spacer(modifier = Modifier.height(36.dp))
    val textValueNameApartament= remember{
        mutableStateOf("")
    }
    OutlinedTextField(
        value = textValueNameApartament.value,
        onValueChange = { textValueNameApartament.value=it},
        label = { Text("Nombre de el Edificio") },
        placeholder = { Text("Nombre de tu edificio/ departamento") },
        modifier = Modifier
            .fillMaxWidth()

    )
    Spacer(modifier = Modifier.height(16.dp))
    val textValueDireccion= remember{
        mutableStateOf("")
    }
    OutlinedTextField(
        value = textValueDireccion.value,
        onValueChange = { textValueDireccion.value=it},
        label = { Text("Direccion") },
        placeholder = { Text("Ej.Cda de Argentina s/n") },
        modifier = Modifier
            .fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))
    GetCordinatesComponent()
    Spacer(modifier = Modifier.height(16.dp))

    Spacer(modifier = Modifier.height(56.dp))
    Text("Tipo de departamento", style = MaterialTheme.typography.bodyMedium)
    Spacer(modifier = Modifier.height(36.dp))

    val options = listOf("Casa", "Apartamento")

    // RadioGroup con opciones "Casa" y "Apartamento"
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            Checkbox(
                checked = option == selectedOption.value,
                onCheckedChange = { isChecked -> if (isChecked) selectedOption.value = option }
            )
            Text(
                text = option,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    val textValueDescription= remember{
        mutableStateOf("")
    }
    OutlinedTextField(
        value = textValueDescription.value,
        onValueChange = { textValueDescription.value=it},
        label = { Text("Descripcion") },
        placeholder = { Text("Ej.Apartamento con 3 habitaciones, todos los servicios") },
        modifier = Modifier
            .fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))
    val textValueCost= remember{
        mutableStateOf("")
    }
    OutlinedTextField(
        value = textValueCost.value,
        onValueChange = { textValueCost.value =it},
        label = { Text("Costo Mensual") },
        placeholder = { Text("Ej. $1500 mensuales por persona") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16  .dp))

    GalleryView()
    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = false,
            onCheckedChange = {},

            )
        Text(
            "Aceptar términos y condiciones de servicio",
            color = Color.Black,
            textAlign = TextAlign.Center,
            style =
            MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center // Alinea los elementos horizontalmente
    ) {
        Button(
            onClick = { /* TODO: Implement registration logic */ },
        ) {
            Text(text = "Registrar Vivienda")
        }

        Spacer(modifier = Modifier.width(8.dp))

    }
}