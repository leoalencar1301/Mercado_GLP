package com.mercadoglp.app.ui.dashboard

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.mercadoglp.app.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

/**
 * Tela de visualização de mapa usando OpenStreetMap
 * 100% GRATUITO - Sem necessidade de API Key!
 * 
 * OpenStreetMap Features:
 * - Mapas gratuitos e open source
 * - Sem limites de uso
 * - Tiles de alta qualidade
 * - Suporte offline
 * - Marcadores personalizados
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    revendaId: String,
    onNavigateBack: () -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val revenda by viewModel.revenda.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()
    
    // Configurar OSMDroid
    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
        viewModel.loadRevenda(revendaId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = revenda?.razaoSocial ?: stringResource(R.string.map_view)
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Mapa OpenStreetMap
            if (revenda != null && revenda?.hasCoordinates() == true) {
                OpenStreetMapView(
                    latitude = revenda!!.latitude!!,
                    longitude = revenda!!.longitude!!,
                    title = revenda!!.razaoSocial ?: "Revenda",
                    description = revenda!!.getEnderecoCompleto(),
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Estado de carregamento ou sem coordenadas
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (revenda == null) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Carregando mapa...")
                    } else {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Sem coordenadas disponíveis",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Esta revenda não possui localização GPS cadastrada",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Card de informações flutuante
            revenda?.let { rev ->
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = rev.razaoSocial ?: "Sem nome",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = rev.getEnderecoCompleto(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${rev.municipio}/${rev.uf}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (rev.telefone != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "📞 ${rev.telefone}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Componente de mapa OpenStreetMap
 * Totalmente gratuito e sem necessidade de API Key
 */
@Composable
fun OpenStreetMapView(
    latitude: Double,
    longitude: Double,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    zoomLevel: Double = 15.0
) {
    val context = LocalContext.current
    
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            MapView(ctx).apply {
                // Configurações do mapa
                setTileSource(TileSourceFactory.MAPNIK) // Estilo padrão do OSM
                setMultiTouchControls(true) // Pinch to zoom
                setBuiltInZoomControls(false) // Remover botões de zoom
                
                // Posição inicial
                val startPoint = GeoPoint(latitude, longitude)
                controller.setZoom(zoomLevel)
                controller.setCenter(startPoint)
                
                // Adicionar marcador da revenda
                val marker = Marker(this).apply {
                    position = startPoint
                    this.title = title
                    snippet = description
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    
                    // Personalizar ícone (opcional)
                    // icon = ContextCompat.getDrawable(ctx, R.drawable.ic_marker)
                }
                overlays.add(marker)
                
                // Adicionar overlay de localização do usuário (opcional)
                val locationOverlay = MyLocationNewOverlay(this).apply {
                    enableMyLocation()
                }
                overlays.add(locationOverlay)
            }
        },
        update = { mapView ->
            // Atualizar mapa quando dados mudarem
            val newPoint = GeoPoint(latitude, longitude)
            mapView.controller.animateTo(newPoint)
        }
    )
}

/**
 * Componente alternativo: Mapa estático (imagem)
 * Útil para preview ou quando não há conexão
 */
@Composable
fun StaticMapView(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    // Usar serviço de tiles estáticos do OpenStreetMap
    val staticMapUrl = "https://www.openstreetmap.org/export/embed.html" +
            "?bbox=${longitude-0.01},${latitude-0.01},${longitude+0.01},${latitude+0.01}" +
            "&layer=mapnik&marker=${latitude},${longitude}"
    
    // Pode usar uma WebView ou biblioteca de imagens para carregar
    Box(modifier = modifier) {
        Text("Mapa estático em desenvolvimento")
    }
}

/**
 * Extensões úteis para OSMDroid
 */
object MapUtils {
    /**
     * Calcula distância entre dois pontos em metros
     */
    fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val point1 = GeoPoint(lat1, lon1)
        val point2 = GeoPoint(lat2, lon2)
        return point1.distanceToAsDouble(point2)
    }
    
    /**
     * Formata distância para exibição
     */
    fun formatDistance(meters: Double): String {
        return when {
            meters < 1000 -> "${meters.toInt()} m"
            meters < 10000 -> String.format("%.1f km", meters / 1000)
            else -> "${(meters / 1000).toInt()} km"
        }
    }
    
    /**
     * Cria URL para rota no OpenStreetMap
     */
    fun getRouteUrl(
        fromLat: Double, fromLon: Double,
        toLat: Double, toLon: Double
    ): String {
        return "https://www.openstreetmap.org/directions?" +
                "engine=fossgis_osrm_car&" +
                "route=${fromLat},${fromLon};${toLat},${toLon}"
    }
}

/**
 * Diferentes estilos de mapa disponíveis (tiles)
 */
enum class MapStyle(val displayName: String, val tileSource: String) {
    MAPNIK("Padrão", "Mapnik"),
    HUMANITARIAN("Humanitário", "Humanitarian"),
    TOPO("Topográfico", "USGS Topo"),
    SATELLITE("Satélite (Esri)", "ESRI World Imagery");
    
    companion object {
        fun getAll() = values().toList()
    }
}

/**
 * Configuração de cache offline para OSMDroid
 */
object MapCache {
    fun setupCache(context: Context) {
        val config = Configuration.getInstance()
        config.userAgentValue = context.packageName
        
        // Configurar diretório de cache
        val cacheDir = context.externalCacheDir ?: context.cacheDir
        config.osmdroidBasePath = cacheDir
        config.osmdroidTileCache = cacheDir.resolve("osmdroid")
        
        // Tamanho do cache (em MB)
        config.tileFileSystemCacheMaxBytes = 100L * 1024 * 1024 // 100 MB
    }
}
