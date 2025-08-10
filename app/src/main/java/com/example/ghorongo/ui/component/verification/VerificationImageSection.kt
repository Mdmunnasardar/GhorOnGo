package com.example.ghorongo.ui.component.verification

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun VerificationImageSection(
    imageUrl: String?,
    verified: Boolean,
    onUploadClick: () -> Unit
) {
    Column {
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Verification Image",
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )
            Text(
                text = if (verified) "Verified" else "Pending Verification",
                color = if (verified) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
        } else {
            Text("No verification image uploaded yet.")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onUploadClick) {
            Text("Upload National ID")
        }
    }
}