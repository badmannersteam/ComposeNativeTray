package com.kdroid.composetray.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.kdroid.composetray.lib.mac.MacOSMenuBarThemeDetector
import io.github.kdroidfilter.platformtools.LinuxDesktopEnvironment
import io.github.kdroidfilter.platformtools.OperatingSystem.*
import io.github.kdroidfilter.platformtools.darkmodedetector.isSystemInDarkMode
import io.github.kdroidfilter.platformtools.detectLinuxDesktopEnvironment
import io.github.kdroidfilter.platformtools.getOperatingSystem
import java.util.function.Consumer

@Composable
fun isMenuBarInDarkMode(): Boolean {
    return when (getOperatingSystem()) {
        MACOS -> isMacOsMenuBarInDarkMode()
        WINDOWS -> isSystemInDarkMode()
        LINUX -> when (detectLinuxDesktopEnvironment()) {
            LinuxDesktopEnvironment.GNOME -> true
            LinuxDesktopEnvironment.KDE -> isSystemInDarkMode()
            LinuxDesktopEnvironment.XFCE -> true
            LinuxDesktopEnvironment.CINNAMON -> true
            LinuxDesktopEnvironment.MATE -> true
            LinuxDesktopEnvironment.UNKNOWN -> true
            null -> isSystemInDarkMode()
        }
        else -> true
    }
}

@Composable
internal fun isMacOsMenuBarInDarkMode(): Boolean {
    val darkModeState = remember { mutableStateOf(MacOSMenuBarThemeDetector.isDark()) }
    DisposableEffect(Unit) {
        val listener = Consumer<Boolean> { newValue ->
            darkModeState.value = newValue
        }
        MacOSMenuBarThemeDetector.registerListener(listener)
        onDispose {
            MacOSMenuBarThemeDetector.removeListener(listener)
        }
    }
    return darkModeState.value
}