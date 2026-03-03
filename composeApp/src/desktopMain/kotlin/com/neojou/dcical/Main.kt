package com.neojou.dcical

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

/**
 * Desktop entry point for the Currency Viewer app.
 *
 * Creates a Compose for Desktop [Window] within [application]
 * and hosts the shared [App] composable.
 */
fun main() { // 增加 args 以確保 JVM 兼容性
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Currency Viewer",
        ) {
            App()
        }
    }
}