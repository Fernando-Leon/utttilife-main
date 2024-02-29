package com.example.utttilife.screens

    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.painter.Painter
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.res.painterResource
    import androidx.compose.foundation.Image
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.rememberNavController
    import com.example.utttilife.R


    @Preview
    @Composable
    fun LoginScreen() {
        val backgroundImage: Painter = painterResource(id = R.drawable.fondo)
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                Box (
                    modifier = Modifier.fillMaxSize()
                ){
                    Image(
                        painter = backgroundImage,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    LoginComponents(
                        onLoginSuccess = {
                            // Aquí manejarías lo que sucede cuando el login es exitoso, por ejemplo, navegando a la pantalla principal
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true } // Esto elimina la pantalla de login del stack de navegación
                            }
                        },
                        onRegisterClicked = {
                            navController.navigate("register")
                        }
                    )
                }
            }
            composable("register") {
                RegisterScreen()
            }
            composable("home") {
                MainScreen()
            }
        }
    }

