package com.example.user_feature.presenter.home.ui

import Elevation
import PaddingValues
import Spacing
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.domain.model.User

@Composable
internal fun UserItem(user: User?, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues.small)
            .clickable { onItemClick() },
        elevation = Elevation.small,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues.medium)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user?.name ?: "",
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(Spacing.small))
                Text(
                    text = user?.email ?: "",
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}