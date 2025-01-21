package com.example.weatherapp.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.weatherapp.R

@Composable
fun ProfileTopBar(
    signOut: () -> Unit,
    deleteUser: () -> Unit,
    isDarkTheme: Boolean,
    toggleTheme: () -> Unit
) {
    var openMenu by remember { mutableStateOf(false) }

    TopAppBar (
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        id = R.string.profile_screen_title
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            openMenu = !openMenu
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = null,
                        )
                    }
                }
            }
        },
        actions = {
            DropdownMenu(
                expanded = openMenu,
                onDismissRequest = {
                    openMenu = !openMenu
                }
            ) {
                DropdownMenuItem(
                    onClick = {
                        signOut()
                        openMenu = !openMenu
                    }
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.sign_out_item
                        )
                    )
                }
                DropdownMenuItem(
                    onClick = {
                        deleteUser()
                        openMenu = !openMenu
                    }
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.delete_user_item
                        )
                    )
                }
                DropdownMenuItem(
                    onClick = {
                        toggleTheme()
                        openMenu = !openMenu
                    }
                ) {
                    Text(
                        text = if (isDarkTheme) {
                            stringResource(id = R.string.light_theme_item)
                        } else {
                            stringResource(id = R.string.dark_theme_item)
                        }
                    )
                }
            }
        }
    )
}