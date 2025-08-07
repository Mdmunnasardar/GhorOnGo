package com.example.ghorongo.ui.component.search
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tune

@Composable
fun FilterButton(
    onClick: () -> Unit
) {
    FilledTonalIconButton(
        onClick = onClick,
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Icon(
            imageVector = Icons.Outlined.Tune,
            contentDescription = "Filters",
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
