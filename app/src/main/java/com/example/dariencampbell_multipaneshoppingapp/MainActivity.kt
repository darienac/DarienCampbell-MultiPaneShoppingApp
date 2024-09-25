package com.example.dariencampbell_multipaneshoppingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.dariencampbell_multipaneshoppingapp.ui.theme.DarienCampbellMultiPaneShoppingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DarienCampbellMultiPaneShoppingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppLayout(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class Product(val name: String, val price: String, val desc: String) {}
enum class Pane {
    PRODUCT_LIST,
    PRODUCT_DETAILS
}

@Composable
fun ProductListEntry(product: Product, selected: Boolean, onClick: ()->Unit) {
    OutlinedCard(modifier=Modifier.fillMaxWidth().padding(8.dp, 0.dp).clickable(onClick = onClick)) {
        Text(product.name, modifier=Modifier.padding(32.dp, 16.dp, 16.dp, 16.dp), fontSize=24.sp, fontWeight=(if (selected) FontWeight.Bold else null))
    }
}

@Composable
fun ProductListPane(products: List<Product>, selectedProduct: Int, onClick: (index: Int)->Unit) {
    LazyColumn(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            Spacer(Modifier.height(0.dp))
        }
        items(products.size) { index ->
            ProductListEntry(products[index], index == selectedProduct) {
                onClick(index)
            }
        }
        item {
            Spacer(Modifier.height(0.dp))
        }
    }
}

@Composable
fun ProductDetailsPane(products: List<Product>, selectedProduct: Int, navigateBack: Boolean = false, onNavigateBack: ()->Unit = {}) {
    Column(modifier=Modifier.fillMaxSize()) {
        if (selectedProduct == -1) {
            Text("Select a product to view details", fontSize=24.sp, fontWeight= FontWeight.Bold, modifier=Modifier.padding(16.dp, 8.dp, 8.dp, 0.dp))
        } else {
            var product = products[selectedProduct]
            Text(product.name, fontSize=30.sp, fontWeight= FontWeight.Bold, modifier=Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp))
            Text(product.price, fontSize=24.sp, textDecoration=TextDecoration.Underline, modifier=Modifier.padding(16.dp, 0.dp, 0.dp, 24.dp))
            Text(product.desc, fontSize=20.sp, modifier=Modifier.padding(16.dp))
        }
        if (navigateBack) {
            Column(modifier=Modifier.fillMaxSize().weight(1f).padding(0.dp, 0.dp, 0.dp, 8.dp), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick=onNavigateBack) {
                    Text("View Products", fontSize=24.sp)
                }
            }
        }
    }
}

@Composable
fun AppLayout(modifier: Modifier = Modifier) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val products = listOf(
        Product("Product A", "$100", "This is a great product A."),
        Product("Product B", "$150", "This is product B with more features."),
        Product("Product C", "$200", "Premium product C."),
        Product("Product D", "$225", "Product A under a different name."),
        Product("Product E", "$250", "Contains five $50 bills."),
        Product("Product F", "$0", "Please buy our product."),
        Product("Product G", "$90", "Generic brand product A."),
        Product("Product H", "$500", "The most premium product H."),
    )
    var selectedProduct by rememberSaveable { mutableIntStateOf(-1) }
    var currentPane by rememberSaveable { mutableStateOf(Pane.PRODUCT_LIST) }

    Row(modifier = modifier) {
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
            if (currentPane == Pane.PRODUCT_LIST) {
                ProductListPane(products, selectedProduct) {
                    selectedProduct = it
                    currentPane = Pane.PRODUCT_DETAILS
                }
            } else {
                ProductDetailsPane(products, selectedProduct, true) {
                    currentPane = Pane.PRODUCT_LIST
                    selectedProduct = -1
                }
            }
            return
        }
        Box(modifier=Modifier.weight(1f)) {
            ProductListPane(products, selectedProduct) { selectedProduct = it }
        }
        Box(modifier=Modifier.weight(1f)) {
            ProductDetailsPane(products, selectedProduct)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DarienCampbellMultiPaneShoppingAppTheme {
        AppLayout()
    }
}