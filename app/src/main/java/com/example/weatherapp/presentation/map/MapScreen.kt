@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.weatherapp.presentation.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.weatherapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navigateToProfile: () -> Unit
) {
    var location by remember { mutableStateOf<Location?>(null) }
    var address by remember { mutableStateOf<String>("") }
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    Scaffold(
        topBar = {
            MapTopAppBar(navigateToProfile = navigateToProfile)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StyledButton(
                    text = "Pobierz lokalizację",
                    onClick = {
                        if (locationPermissionState.status.isGranted) {
                            fetchLocation(context) { loc -> location = loc }
                        } else {
                            locationPermissionState.launchPermissionRequest()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                StyledButton(
                    text = "Pobierz adres",
                    onClick = {
                        location?.let {
                            fetchAddress(context, it) { addr -> address = addr }
                        } ?: Toast.makeText(context, "Najpierw pobierz lokalizację.", Toast.LENGTH_SHORT).show()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                location?.let {
                    Text("Lokalizacja: Lat: ${it.latitude}, Long: ${it.longitude}")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (address.isNotEmpty()) {
                    Text("Adres: $address")
                    Spacer(modifier = Modifier.height(16.dp))

                    location?.let {
                        OpenStreetMapView(latitude = it.latitude, longitude = it.longitude, zoom = 12)
                    }
                }
            }
        }
    )
}

@Composable
fun StyledButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapTopAppBar(
    navigateToProfile: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Mapa") },
        navigationIcon = {
            IconButton(onClick = navigateToProfile) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}

fun fetchLocation(context: Context, onLocationFetched: (Location) -> Unit) {
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let { onLocationFetched(it) } ?: Toast.makeText(context, "Nie można pobrać lokalizacji.", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Log.e("Location", "Error getting location: ${e.message}")
        }
    } else {
        Toast.makeText(context, "Brak uprawnień do lokalizacji.", Toast.LENGTH_SHORT).show()
    }
}

fun fetchAddress(context: Context, location: Location, onAddressFetched: (String) -> Unit) {
    val geocoder = Geocoder(context)
    try {
        val addresses: MutableList<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        val address = addresses?.firstOrNull()?.getAddressLine(0)
        address?.let { onAddressFetched(it) } ?: Toast.makeText(context, "Nie znaleziono adresu.", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        Toast.makeText(context, "Błąd pobierania adresu.", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun OpenStreetMapView(latitude: Double, longitude: Double, zoom: Int) {
    val url = "https://streetmap.pl/#$latitude,$longitude,${zoom}z"
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.allowContentAccess = true
                settings.setSupportZoom(true)
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW // Ensure only HTTPS content

                webViewClient = object : android.webkit.WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return false // Ensures navigation happens within the WebView
                    }
                }

                loadUrl(url)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

