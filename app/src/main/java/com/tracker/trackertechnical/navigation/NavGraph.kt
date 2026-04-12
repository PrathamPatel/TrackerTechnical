package com.tracker.trackertechnical.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tracker.trackertechnical.navigation.routes.ShipmentDetailRoute
import com.tracker.trackertechnical.navigation.routes.ShipmentsRoute

object NavRoutes {
    const val SHIPMENTS = "shipments"
    const val SHIPMENT_DETAIL = "shipment_detail"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavRoutes.SHIPMENTS,
    ) {
        composable(NavRoutes.SHIPMENTS) {
            ShipmentsRoute(
                onOpenShipment = { id ->
                    navController.navigate("${NavRoutes.SHIPMENT_DETAIL}/$id")
                },
            )
        }
        composable(
            route = "${NavRoutes.SHIPMENT_DETAIL}/{shipmentId}",
            arguments = listOf(
                navArgument("shipmentId") { type = NavType.StringType }
            ),
        ) {
            ShipmentDetailRoute(onBack = { navController.popBackStack() })
        }
    }
}