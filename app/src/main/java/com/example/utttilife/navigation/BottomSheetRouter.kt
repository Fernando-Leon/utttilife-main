package com.example.utttilife.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen(){
    object BottomSheetDepartmentScreen: Screen()
    object BottomSheetReviewsScreen: Screen()
}

object BottomSheetRouter{
    val showedScreen: MutableState<Screen> = mutableStateOf(Screen.BottomSheetDepartmentScreen)

    fun navigateTo(destiny: Screen){
        showedScreen.value = destiny
    }
}