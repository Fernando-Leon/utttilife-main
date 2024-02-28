package com.example.utttilife.components.recipes

import android.content.ContentResolver
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.utttilife.R

// Component for selected image from gallery
@Composable
fun ImageSelector(selectedImage: MutableState<Bitmap?>) {
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver
    //Remember the result for activity for get image selected
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        // Refresh value image with selected image
        selectedImage.value = uri?.let { decodeUri(contentResolver, it) }
    }
    IconButton(onClick = { galleryLauncher.launch("image/*") }) {
        Icon(
            painter = painterResource(id = R.drawable.add_image),
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
            contentDescription = "Agregar imagen",
            modifier = Modifier.size(24.dp)
        )
    }
}


// Component to view selected image
@Composable
fun SelectedImageView(selectedImage: MutableState<Bitmap?>) {
    selectedImage.value?.let { bitmap ->
        //Box for image
        Box(
            modifier = Modifier
                .padding(16.dp)
                .shadow(4.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Image(
                painter = BitmapPainter(bitmap.asImageBitmap()),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat())
            )
        }
    }
}
