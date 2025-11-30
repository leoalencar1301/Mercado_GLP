# 🗺️ Guia Completo - OpenStreetMap no Mercado do GLP

## 🎉 Por que OpenStreetMap?

### ✅ Vantagens

| Recurso | OpenStreetMap | Google Maps |
|---------|---------------|-------------|
| **Custo** | ✅ 100% Gratuito | ❌ US$ 200/mês após free tier |
| **Limites** | ✅ Sem limites | ❌ 28.500 carregamentos/mês |
| **API Key** | ✅ Não precisa | ❌ Necessário |
| **Offline** | ✅ Suporte nativo | ⚠️ Limitado |
| **Open Source** | ✅ Sim | ❌ Não |
| **Customização** | ✅ Total | ⚠️ Limitada |

### 💰 Economia

**Google Maps (após free tier):**
- Carregamentos: US$ 7 por 1.000
- 10.000 usuários/mês = ~US$ 280/mês
- 100.000 usuários/mês = ~US$ 2.800/mês

**OpenStreetMap:**
- Carregamentos: US$ 0
- Ilimitado: US$ 0/mês
- **Economia: 100%** 🎉

---

## 🚀 Implementação Realizada

### 📦 Bibliotecas Adicionadas

```gradle
// OpenStreetMap - GRATUITO
implementation 'org.osmdroid:osmdroid-android:6.1.17'
implementation 'com.github.MKergall:osmbonuspack:6.9.0'
```

### 🔧 Configurações

#### 1. Permissões (AndroidManifest.xml)
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" 
    android:maxSdkVersion="28" />
```

#### 2. Nenhuma API Key necessária! 🎉
```kotlin
// Antes (Google Maps):
// buildConfigField "String", "MAPS_API_KEY", "..."

// Agora (OpenStreetMap):
// Nada! Sem configuração necessária!
```

---

## 📱 Como Usar

### Exemplo Básico

```kotlin
@Composable
fun SimpleMapScreen() {
    val context = LocalContext.current
    
    // Configurar OSMDroid (fazer uma vez)
    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
    }
    
    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(15.0)
                controller.setCenter(GeoPoint(-23.5505, -46.6333)) // São Paulo
            }
        }
    )
}
```

### Adicionar Marcador

```kotlin
val marker = Marker(mapView).apply {
    position = GeoPoint(latitude, longitude)
    title = "Revenda GLP"
    snippet = "Endereço completo"
    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
}
mapView.overlays.add(marker)
```

### Localização do Usuário

```kotlin
val locationOverlay = MyLocationNewOverlay(mapView).apply {
    enableMyLocation()
    enableFollowLocation()
}
mapView.overlays.add(locationOverlay)
```

---

## 🎨 Estilos de Mapa Disponíveis

### 1. Mapnik (Padrão)
```kotlin
mapView.setTileSource(TileSourceFactory.MAPNIK)
```
- Estilo padrão do OpenStreetMap
- Boa legibilidade
- Cores suaves

### 2. Cycle Map
```kotlin
mapView.setTileSource(TileSourceFactory.CYCLEMAP)
```
- Otimizado para ciclistas
- Mostra ciclovias

### 3. Humanitarian
```kotlin
mapView.setTileSource(TileSourceFactory.OPEN_SEAMAP)
```
- Foco em infraestrutura
- Dados humanitários

### 4. Satélite (via Esri)
```kotlin
// Requer configuração adicional
val esriSatellite = object : OnlineTileSourceBase(
    "ESRI", 0, 18, 256, "",
    arrayOf("https://server.arcgisonline.com/...")
) { }
mapView.setTileSource(esriSatellite)
```

---

## 💾 Cache Offline

### Configurar Cache

```kotlin
object MapCache {
    fun setupCache(context: Context) {
        val config = Configuration.getInstance()
        config.userAgentValue = context.packageName
        
        // Diretório de cache
        val cacheDir = context.externalCacheDir ?: context.cacheDir
        config.osmdroidBasePath = cacheDir
        config.osmdroidTileCache = cacheDir.resolve("osmdroid")
        
        // Tamanho máximo: 100 MB
        config.tileFileSystemCacheMaxBytes = 100L * 1024 * 1024
    }
}

// Chamar no Application onCreate()
class MercadoGLPApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MapCache.setupCache(this)
    }
}
```

### Download de Mapas Offline

```kotlin
// Baixar região para uso offline
val cacheManager = CacheManager(mapView)
val bbox = BoundingBox(
    north = -23.0, south = -24.0,
    east = -46.0, west = -47.0
)

cacheManager.downloadAreaAsync(
    context, bbox, 
    zoomMin = 12, zoomMax = 16,
    object : CacheManager.CacheManagerCallback {
        override fun onTaskComplete() {
            // Download completo
        }
        override fun updateProgress(
            progress: Int, currentZoomLevel: Int,
            zoomMin: Int, zoomMax: Int
        ) {
            // Atualizar progresso
        }
    }
)
```

---

## 🛠️ Recursos Avançados

### 1. Rotas (com OSMBonusPack)

```kotlin
val roadManager = OSRMRoadManager(context, "MercadoGLP")

val waypoints = arrayListOf(
    GeoPoint(startLat, startLon),
    GeoPoint(endLat, endLon)
)

// Buscar rota (em background)
lifecycleScope.launch(Dispatchers.IO) {
    val road = roadManager.getRoad(waypoints)
    
    withContext(Dispatchers.Main) {
        if (road.mStatus == Road.STATUS_OK) {
            val roadOverlay = RoadManager.buildRoadOverlay(road)
            mapView.overlays.add(roadOverlay)
            mapView.invalidate()
        }
    }
}
```

### 2. Clustering de Marcadores

```kotlin
val clusterManager = RadiusMarkerClusterer(context)
clusterManager.setIcon(
    BitmapFactory.decodeResource(
        resources, 
        R.drawable.marker_cluster
    )
)

// Adicionar marcadores
revendas.forEach { revenda ->
    if (revenda.hasCoordinates()) {
        val marker = Marker(mapView).apply {
            position = GeoPoint(revenda.latitude!!, revenda.longitude!!)
            title = revenda.razaoSocial
        }
        clusterManager.add(marker)
    }
}

mapView.overlays.add(clusterManager)
```

### 3. Heatmap

```kotlin
val heatmapOverlay = HeatmapOverlay().apply {
    setData(
        revendas.mapNotNull { revenda ->
            if (revenda.hasCoordinates()) {
                HeatmapPoint(
                    revenda.latitude!!,
                    revenda.longitude!!,
                    1.0 // intensidade
                )
            } else null
        }
    )
}
mapView.overlays.add(heatmapOverlay)
```

### 4. Busca de Endereços (Geocoding)

```kotlin
val geocoder = GeocoderNominatim(Locale.getDefault(), "MercadoGLP")

lifecycleScope.launch(Dispatchers.IO) {
    val addresses = geocoder.getFromLocationName(
        "Avenida Paulista, São Paulo",
        1 // max results
    )
    
    withContext(Dispatchers.Main) {
        if (addresses.isNotEmpty()) {
            val location = addresses[0]
            val point = GeoPoint(location.latitude, location.longitude)
            mapView.controller.animateTo(point)
        }
    }
}
```

---

## 🎯 Casos de Uso no Mercado do GLP

### 1. Mapa de Revenda Individual
```kotlin
// Já implementado em MapScreen_OSM.kt
// Mostra localização exata da revenda
OpenStreetMapView(
    latitude = revenda.latitude,
    longitude = revenda.longitude,
    title = revenda.razaoSocial,
    description = revenda.getEnderecoCompleto()
)
```

### 2. Mapa com Múltiplas Revendas (Clustering)
```kotlin
@Composable
fun MultipleRevendasMap(revendas: List<Revenda>) {
    val context = LocalContext.current
    
    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                
                // Clustering
                val clusterManager = RadiusMarkerClusterer(ctx)
                
                revendas.forEach { revenda ->
                    if (revenda.hasCoordinates()) {
                        val marker = Marker(this).apply {
                            position = GeoPoint(
                                revenda.latitude!!, 
                                revenda.longitude!!
                            )
                            title = revenda.razaoSocial
                            snippet = "${revenda.classe}\n${revenda.distribuidora}"
                        }
                        clusterManager.add(marker)
                    }
                }
                
                overlays.add(clusterManager)
                
                // Ajustar zoom para mostrar todos
                val boundingBox = calculateBoundingBox(revendas)
                zoomToBoundingBox(boundingBox, true)
            }
        }
    )
}
```

### 3. Encontrar Revendas Próximas
```kotlin
fun findNearbyRevendas(
    userLat: Double,
    userLon: Double,
    revendas: List<Revenda>,
    radiusKm: Double = 5.0
): List<Pair<Revenda, Double>> {
    return revendas
        .filter { it.hasCoordinates() }
        .map { revenda ->
            val distance = MapUtils.calculateDistance(
                userLat, userLon,
                revenda.latitude!!, revenda.longitude!!
            )
            revenda to distance
        }
        .filter { (_, distance) -> distance <= radiusKm * 1000 }
        .sortedBy { (_, distance) -> distance }
}
```

### 4. Rota até Revenda
```kotlin
fun openRouteToRevenda(context: Context, revenda: Revenda) {
    if (!revenda.hasCoordinates()) return
    
    val url = MapUtils.getRouteUrl(
        fromLat = userLatitude,
        fromLon = userLongitude,
        toLat = revenda.latitude!!,
        toLon = revenda.longitude!!
    )
    
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
```

---

## 📊 Comparação com Alternativas

### OpenStreetMap vs Outras Opções

| Recurso | OSM | Google Maps | Mapbox | HERE |
|---------|-----|-------------|--------|------|
| Custo | ✅ Grátis | ❌ Pago | ⚠️ Limitado | ⚠️ Limitado |
| Limite | ✅ Ilimitado | ❌ 28.5k/mês | ⚠️ 50k/mês | ⚠️ 25k/mês |
| API Key | ✅ Não precisa | ❌ Sim | ❌ Sim | ❌ Sim |
| Offline | ✅ Excelente | ⚠️ Limitado | ✅ Bom | ✅ Bom |
| Customização | ✅ Total | ⚠️ Limitada | ✅ Boa | ✅ Boa |
| Qualidade | ✅ Ótima | ✅ Excelente | ✅ Ótima | ✅ Ótima |

---

## 🔧 Troubleshooting

### Problema 1: Tiles não carregam
```kotlin
// Solução: Verificar permissões e cache
Configuration.getInstance().userAgentValue = packageName
MapCache.setupCache(context)
```

### Problema 2: Mapa fica em branco
```kotlin
// Solução: Definir tile source
mapView.setTileSource(TileSourceFactory.MAPNIK)
```

### Problema 3: Performance ruim com muitos marcadores
```kotlin
// Solução: Usar clustering
val clusterManager = RadiusMarkerClusterer(context)
// Adicionar marcadores ao cluster, não direto no mapa
```

### Problema 4: Cache muito grande
```kotlin
// Solução: Limitar tamanho do cache
Configuration.getInstance().apply {
    tileFileSystemCacheMaxBytes = 50L * 1024 * 1024 // 50 MB
}
```

---

## 🎓 Recursos Adicionais

### Documentação Oficial
- **OSMDroid:** https://github.com/osmdroid/osmdroid/wiki
- **OSMBonusPack:** https://github.com/MKergall/osmbonuspack
- **OpenStreetMap:** https://www.openstreetmap.org/

### Exemplos de Código
- **GitHub OSMDroid:** https://github.com/osmdroid/osmdroid
- **Tutorial Completo:** https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library

### Comunidade
- **Stack Overflow:** Tag `osmdroid`
- **GitHub Issues:** Para reportar bugs
- **OSM Forum:** https://forum.openstreetmap.org/

---

## ✅ Checklist de Implementação

- [x] ✅ Dependências adicionadas
- [x] ✅ Google Maps removido
- [x] ✅ API Key removida
- [x] ✅ Permissões configuradas
- [x] ✅ MapScreen_OSM.kt criado
- [ ] 🔄 Testar em dispositivo real
- [ ] 🔄 Implementar clustering
- [ ] 🔄 Adicionar rotas
- [ ] 🔄 Cache offline
- [ ] 🔄 Customizar marcadores

---

## 🎉 Conclusão

Com OpenStreetMap você tem:

✅ **Custo ZERO** - Economia de centenas de dólares por mês  
✅ **Sem limites** - Usuários ilimitados  
✅ **Sem API Key** - Setup mais simples  
✅ **Offline first** - Melhor experiência do usuário  
✅ **Open Source** - Comunidade ativa  
✅ **Customizável** - Total controle  

**Mercado do GLP agora usa mapas 100% gratuitos! 🗺️🎉**

---

📅 Atualizado em: 28 de Novembro de 2024  
🗺️ Biblioteca: OSMDroid 6.1.17  
💚 Open Source & Free Forever
