package com.example.enggo.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.enggo.EngGoApplication
import com.example.enggo.ui.course.CourseViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for CourseViewModel
        initializer {
            CourseViewModel(inventoryApplication().container.courseRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.inventoryApplication(): EngGoApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as EngGoApplication)
