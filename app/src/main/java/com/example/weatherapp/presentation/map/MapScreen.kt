@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.weatherapp.presentation.map

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.weatherapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
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
            MapTopAppBar(
                navigateToProfile = navigateToProfile
            )
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
                // Move buttons up
                StyledButton(
                    text = "Pobierz lokalizację",
                    onClick = {
                        if (locationPermissionState.status.isGranted) {
                            fetchLocation(context) { loc ->
                                location = loc
                            }
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
                            fetchAddress(context, it) { addr ->
                                address = addr
                            }
                        } ?: run {
                            Toast.makeText(context, "Najpierw pobierz lokalizację.", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Display location (latitude, longitude)
                location?.let {
                    Text("Lokalizacja: Lat: ${it.latitude}, Long: ${it.longitude}")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display address
                if (address.isNotEmpty()) {
                    Text("Adres: $address")
                    Spacer(modifier = Modifier.height(16.dp))

                    // Display OpenStreetMap using WebView
                    OpenStreetMapView(address)
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
        title = {
            Text(text = "Mapa")
        },
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

// Fetch the current location
fun fetchLocation(context: Context, onLocationFetched: (Location) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                onLocationFetched(it)
            } ?: run {
                Log.e("Location", "No location found")
                Toast.makeText(context, "Nie można pobrać lokalizacji.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Log.e("Location", "Error getting location: ${e.message}")
        }
    } else {
        Log.e("Location", "Permission not granted")
        Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()
    }
}

// Fetch address from location
fun fetchAddress(context: Context, location: Location, onAddressFetched: (String) -> Unit) {
    val geocoder = Geocoder(context)
    try {
        val addresses: MutableList<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        val address = addresses?.firstOrNull()?.getAddressLine(0)
        address?.let {
            onAddressFetched(it)
        } ?: run {
            Log.e("Geocoder", "No address found")
            Toast.makeText(context, "Nie znaleziono adresu.", Toast.LENGTH_SHORT).show()
        }
    } catch (e: IOException) {
        Log.e("Geocoder", "Error fetching address: ${e.message}")
        Toast.makeText(context, "Błąd pobierania adresu.", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun OpenStreetMapView(address: String) {
    val url = "https://streetmap.pl/?q=$address"  // Correct URL without extra slash
    val context = LocalContext.current
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                // Enable JavaScript and other settings
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.allowContentAccess = true
                settings.setSupportZoom(true)
                settings.builtInZoomControls = true
                settings.displayZoomControls = false

                // Handle redirects and loading errors
                webViewClient = object : android.webkit.WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                    }

                    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                        super.onReceivedError(view, errorCode, description, failingUrl)
                        // Handle loading errors here (optional)
                    }
                }

                loadUrl(url)
            }
        },
        modifier = Modifier.fillMaxSize() // Makes WebView take the remaining space
    )
}
