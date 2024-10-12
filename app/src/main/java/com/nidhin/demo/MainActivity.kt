package com.nidhin.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nidhin.demo.feature.product_details.ProductDetailsScreen
import com.nidhin.demo.feature.product_listing.ProductListingScreen
import com.nidhin.demo.feature.product_listing.ProductListingViewModel
import com.nidhin.demo.models.ProductUiModel
import com.nidhin.demo.ui.theme.ProductListingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ProductListingTheme {
                AppNavHost(navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(navController: NavHostController, productListingViewModel: ProductListingViewModel = hiltViewModel()) {
    var isBackButtonVisible by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Products")
                },
                navigationIcon = {
                    if(isBackButtonVisible){
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { _padding ->

        NavHost(
            navController = navController,
            startDestination = "products"
        ) {
            composable("products") {
                ProductListingScreen(
                    modifier = Modifier.padding(top = _padding.calculateTopPadding()),
                    navController = navController,
                    viewModel = productListingViewModel
                )
            }
            composable(
                "product_details?product_id={product_id}",
                arguments = listOf(
                    navArgument(name = "product_id") {
                        type = NavType.StringType
                        defaultValue = ""
                    })
            ) {
                val productId =
                    it.arguments?.getString("product_id") ?: ""

                val product= (productListingViewModel.state.items.find { it.id.toString() == productId })
                isBackButtonVisible = navController.previousBackStackEntry != null
                product?.let { it1 ->
                    ProductDetailsScreen(
                        modifier = Modifier.padding(top = _padding.calculateTopPadding()),
                        product = it1
                    )
                }
            }
        }
    }
}