package com.ikhokha.techcheck.ui.common

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.ikhokha.techcheck.mlkit.ScannerActivity

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FabScanner(fabScannerVisibility: Boolean,
               scannedProduct:(String)->Unit){

    val barcodeScannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode != Activity.RESULT_OK) {
            return@rememberLauncherForActivityResult
        }
        scannedProduct(it.data?.data.toString())
    }
    val intent = Intent(LocalContext.current, ScannerActivity::class.java)

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            barcodeScannerLauncher.launch(intent)
        }
    }
    val context = LocalContext.current


    AnimatedVisibility(
        visible = fabScannerVisibility,
        enter = scaleIn(),
        exit = scaleOut(),
    ) {
        FloatingActionButton(onClick = {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) -> {
                    barcodeScannerLauncher.launch(intent)
                }
                else -> {
                    launcher.launch(Manifest.permission.CAMERA)
                }
            }
        }, content = {
            Icon(
                imageVector = Icons.Default.QrCodeScanner,
                contentDescription = null
            )
        })
    }
}