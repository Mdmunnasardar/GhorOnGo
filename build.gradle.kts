// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // Keep Google services plugin here with apply false
    alias(libs.plugins.google.gms.google.services) apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    //id("com.google.gms.google-services") version "4.4.1"

}

// Add this block if not existing:
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Make sure you have the Google services classpath here
        classpath("com.google.gms:google-services:4.4.1")
        // Add other classpath dependencies if needed
    }
}