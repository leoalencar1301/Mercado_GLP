package com.mercadoglp.app.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mercadoglp.app.R
import com.mercadoglp.app.data.models.Revenda
import com.mercadoglp.app.ui.components.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * Tela principal do Dashboard
 * Mostra lista de revendas com filtros e estatísticas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToMap: (String) -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToPremium: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val user by viewModel.currentUser.collectAsState()
    
    var showFilterDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "${stringResource(R.string.welcome_back)}, ${user?.getFullName() ?: ""}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    // Botão Premium
                    if (!user?.isPremium == true) {
                        IconButton(onClick = onNavigateToPremium) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = stringResource(R.string.premium),
                                tint = Color(0xFFFFD700)
                            )
                        }
                    }
                    
                    // Menu
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu"
                            )
                        }
                        
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.profile)) },
                                onClick = {
                                    showMenu = false
                                    onNavigateToProfile()
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, null)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.statistics)) },
                                onClick = {
                                    showMenu = false
                                    onNavigateToStatistics()
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.BarChart, null)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.settings)) },
                                onClick = {
                                    showMenu = false
                                    onNavigateToSettings()
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Settings, null)
                                }
                            )
                            Divider()
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.sign_out)) },
                                onClick = {
                                    showMenu = false
                                    onLogout()
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Logout, null)
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botão de Filtros
                FloatingActionButton(
                    onClick = { showFilterDialog = true },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.Default.FilterList, stringResource(R.string.filter))
                }
            }
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.isLoading),
            onRefresh = { viewModel.refreshData() },
            modifier = Modifier.padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Estatísticas Rápidas
                item {
                    QuickStatsCard(
                        totalRevendas = uiState.filteredRevendas.size,
                        byState = uiState.statistics.revendasPorEstado.size,
                        byDistributor = uiState.statistics.revendasPorDistribuidora.size,
                        withCoordinates = uiState.statistics.revendasComCoordenadas
                    )
                }
                
                // Barra de Busca
                item {
                    SearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = { viewModel.updateSearchQuery(it) },
                        onSearch = { viewModel.search() }
                    )
                }
                
                // Badge de Filtros Ativos
                if (!uiState.filters.isEmpty()) {
                    item {
                        ActiveFiltersChip(
                            filterCount = uiState.getActiveFilterCount(),
                            onClearFilters = { viewModel.clearFilters() }
                        )
                    }
                }
                
                // Lista de Revendas
                if (uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else if (uiState.filteredRevendas.isEmpty()) {
                    item {
                        EmptyState(
                            message = stringResource(R.string.no_results),
                            onAction = { viewModel.clearFilters() }
                        )
                    }
                } else {
                    // Verificar limite de plano gratuito
                    val displayList = if (!user?.isPremium!! && uiState.filteredRevendas.size > 50) {
                        uiState.filteredRevendas.take(50)
                    } else {
                        uiState.filteredRevendas
                    }
                    
                    items(displayList) { revenda ->
                        RevendaCard(
                            revenda = revenda,
                            onCardClick = { onNavigateToMap(revenda.cnpj) },
                            onFavoriteClick = { viewModel.toggleFavorite(revenda) }
                        )
                    }
                    
                    // Mensagem de limite para usuários gratuitos
                    if (!user?.isPremium!! && uiState.filteredRevendas.size > 50) {
                        item {
                            PremiumLimitCard(
                                showing = 50,
                                total = uiState.filteredRevendas.size,
                                onUpgradeClick = onNavigateToPremium
                            )
                        }
                    }
                }
            }
        }
        
        // Dialog de Filtros
        if (showFilterDialog) {
            FilterDialog(
                currentFilters = uiState.filters,
                availableStates = uiState.availableStates,
                availableCities = uiState.availableCities,
                availableDistributors = uiState.availableDistributors,
                availableClasses = uiState.availableClasses,
                onDismiss = { showFilterDialog = false },
                onApplyFilters = { filters ->
                    viewModel.applyFilters(filters)
                    showFilterDialog = false
                }
            )
        }
    }
}

/**
 * Card de estatísticas rápidas
 */
@Composable
fun QuickStatsCard(
    totalRevendas: Int,
    byState: Int,
    byDistributor: Int,
    withCoordinates: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.total_revendas),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    label = "Total",
                    value = totalRevendas.toString(),
                    icon = Icons.Default.Store
                )
                StatItem(
                    label = stringResource(R.string.by_state),
                    value = byState.toString(),
                    icon = Icons.Default.Map
                )
                StatItem(
                    label = stringResource(R.string.by_distributor),
                    value = byDistributor.toString(),
                    icon = Icons.Default.Business
                )
                StatItem(
                    label = "Coords",
                    value = withCoordinates.toString(),
                    icon = Icons.Default.LocationOn
                )
            }
        }
    }
}

/**
 * Item de estatística individual
 */
@Composable
fun StatItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Card de revenda individual
 */
@Composable
fun RevendaCard(
    revenda: Revenda,
    onCardClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header com nome e favorito
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = revenda.razaoSocial ?: revenda.nomeFantasia ?: "Sem nome",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = revenda.getCnpjFormatado(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (revenda.isFavorite) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = "Favorite",
                        tint = if (revenda.isFavorite) {
                            Color.Red
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Informações
            InfoRow(icon = Icons.Default.LocationOn, text = revenda.getLocalizacao())
            InfoRow(icon = Icons.Default.Business, text = revenda.distribuidora)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Badge da classe
            Surface(
                color = Color(revenda.getClasseColor()).copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "${revenda.getClasseIcon()} ${revenda.classe}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(revenda.getClasseColor())
                )
            }
        }
    }
}

/**
 * Linha de informação com ícone
 */
@Composable
fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Card de limite premium
 */
@Composable
fun PremiumLimitCard(
    showing: Int,
    total: Int,
    onUpgradeClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFD700).copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = stringResource(R.string.showing_of, showing, total),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.premium_feature_locked),
                style = MaterialTheme.typography.bodySmall
            )
            Button(
                onClick = onUpgradeClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD700)
                )
            ) {
                Icon(Icons.Default.Star, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.unlock_premium))
            }
        }
    }
}
