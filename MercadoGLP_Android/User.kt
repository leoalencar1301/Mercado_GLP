package com.mercadoglp.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * Modelo de dados do usuário
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val uid: String,
    
    val email: String,
    val displayName: String?,
    val photoUrl: String?,
    val phoneNumber: String?,
    
    // Informações de perfil
    val firstName: String? = null,
    val lastName: String? = null,
    val company: String? = null,
    val position: String? = null,
    val bio: String? = null,
    
    // Preferências
    val language: String = "pt", // pt, en, es
    val theme: String = "system", // light, dark, system
    val notificationsEnabled: Boolean = true,
    
    // Assinatura
    val isPremium: Boolean = false,
    val subscriptionType: SubscriptionType = SubscriptionType.FREE,
    val subscriptionStartDate: Long? = null,
    val subscriptionEndDate: Long? = null,
    
    // Metadata
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis(),
    val emailVerified: Boolean = false
) {
    fun getFullName(): String {
        return when {
            !firstName.isNullOrBlank() && !lastName.isNullOrBlank() -> "$firstName $lastName"
            !displayName.isNullOrBlank() -> displayName
            else -> email.substringBefore("@")
        }
    }
    
    fun isSubscriptionActive(): Boolean {
        if (!isPremium) return false
        val now = System.currentTimeMillis()
        return subscriptionEndDate?.let { it > now } ?: false
    }
    
    fun getSubscriptionDaysRemaining(): Int {
        if (!isPremium || subscriptionEndDate == null) return 0
        val now = System.currentTimeMillis()
        val diff = subscriptionEndDate - now
        return (diff / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(0)
    }
}

/**
 * Tipos de assinatura
 */
enum class SubscriptionType {
    FREE,
    PREMIUM_MONTHLY,
    PREMIUM_YEARLY;
    
    fun getDisplayName(): String {
        return when (this) {
            FREE -> "Gratuito"
            PREMIUM_MONTHLY -> "Premium Mensal"
            PREMIUM_YEARLY -> "Premium Anual"
        }
    }
    
    fun getPrice(): String {
        return when (this) {
            FREE -> "R$ 0,00"
            PREMIUM_MONTHLY -> "R$ 29,90/mês"
            PREMIUM_YEARLY -> "R$ 299,90/ano"
        }
    }
    
    fun getSavings(): String? {
        return when (this) {
            PREMIUM_YEARLY -> "Economize 16%"
            else -> null
        }
    }
}

/**
 * Recursos premium disponíveis
 */
enum class PremiumFeature(
    val title: String,
    val description: String,
    val icon: String
) {
    NO_ADS(
        title = "Sem Anúncios",
        description = "Experiência livre de anúncios",
        icon = "🚫"
    ),
    UNLIMITED_RESULTS(
        title = "Resultados Ilimitados",
        description = "Busque quantas revendas quiser",
        icon = "♾️"
    ),
    EXPORT_DATA(
        title = "Exportação de Dados",
        description = "Exporte para CSV, Excel e PDF",
        icon = "📊"
    ),
    ADVANCED_FILTERS(
        title = "Filtros Avançados",
        description = "Acesso a todos os filtros disponíveis",
        icon = "🔍"
    ),
    COMPARISONS(
        title = "Comparações",
        description = "Compare distribuidoras e regiões",
        icon = "📈"
    ),
    ALERTS(
        title = "Alertas Personalizados",
        description = "Receba notificações sobre mudanças",
        icon = "🔔"
    ),
    HISTORY(
        title = "Histórico de Buscas",
        description = "Acesse suas pesquisas anteriores",
        icon = "📜"
    ),
    PRIORITY_SUPPORT(
        title = "Suporte Prioritário",
        description = "Atendimento preferencial",
        icon = "⭐"
    ),
    REALTIME_DATA(
        title = "Dados em Tempo Real",
        description = "Atualizações automáticas",
        icon = "⚡"
    ),
    OFFLINE_MODE(
        title = "Modo Offline Avançado",
        description = "Cache completo de dados",
        icon = "📱"
    );
    
    companion object {
        fun getFreeFeatures(): List<PremiumFeature> {
            return listOf(
                NO_ADS,
                UNLIMITED_RESULTS,
                EXPORT_DATA,
                ADVANCED_FILTERS
            )
        }
        
        fun getAllFeatures(): List<PremiumFeature> {
            return values().toList()
        }
    }
}

/**
 * Request de login
 */
data class LoginRequest(
    val email: String,
    val password: String,
    val rememberMe: Boolean = false
)

/**
 * Request de registro
 */
data class RegisterRequest(
    val email: String,
    val password: String,
    val displayName: String,
    val acceptTerms: Boolean
)

/**
 * Response de autenticação
 */
data class AuthResponse(
    @SerializedName("user")
    val user: User,
    
    @SerializedName("token")
    val token: String,
    
    @SerializedName("refreshToken")
    val refreshToken: String?,
    
    @SerializedName("expiresIn")
    val expiresIn: Long
)

/**
 * Request de atualização de perfil
 */
data class UpdateProfileRequest(
    val displayName: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val company: String? = null,
    val position: String? = null,
    val bio: String? = null,
    val phoneNumber: String? = null
)

/**
 * Request de alteração de senha
 */
data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)

/**
 * Estatísticas do usuário
 */
data class UserStatistics(
    val totalSearches: Int,
    val favoritesCount: Int,
    val lastSearchDate: Long?,
    val mostSearchedState: String?,
    val mostSearchedDistributor: String?,
    val daysAsUser: Int,
    val exportCount: Int
)

/**
 * Configurações de notificação
 */
data class NotificationSettings(
    val enabled: Boolean = true,
    val newDataAvailable: Boolean = true,
    val priceChanges: Boolean = false,
    val weeklyReport: Boolean = false,
    val marketingEmails: Boolean = false
)

/**
 * Histórico de busca
 */
@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val userId: String,
    val query: String,
    val filters: String, // JSON serializado
    val resultsCount: Int,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Favoritos do usuário
 */
@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val userId: String,
    val revendaCnpj: String,
    val createdAt: Long = System.currentTimeMillis()
)
