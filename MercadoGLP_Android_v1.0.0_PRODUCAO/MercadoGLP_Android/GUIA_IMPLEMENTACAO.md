# 📱 Guia Completo de Implementação - Mercado do GLP

## 🎯 Visão Geral

Este guia fornece instruções detalhadas para implementar o aplicativo Android completo do Mercado do GLP com autenticação, multi-idioma e sistema premium.

## 📋 Índice

1. [Configuração Inicial](#configuração-inicial)
2. [Estrutura do Projeto](#estrutura-do-projeto)
3. [Implementação por Módulo](#implementação-por-módulo)
4. [Sistema de Autenticação](#sistema-de-autenticação)
5. [Multi-idioma](#multi-idioma)
6. [Sistema Premium](#sistema-premium)
7. [Integração ANP](#integração-anp)
8. [Testes](#testes)
9. [Deploy](#deploy)

---

## 1. Configuração Inicial

### 1.1 Criar Projeto no Android Studio

```bash
# Criar novo projeto
- Android Studio > New Project > Empty Activity
- Nome: Mercado do GLP
- Package name: com.mercadoglp.app
- Language: Kotlin
- Minimum SDK: API 24 (Android 7.0)
- Build configuration language: Kotlin DSL
```

### 1.2 Configurar Firebase

1. Acesse [Firebase Console](https://console.firebase.google.com/)
2. Crie um novo projeto "Mercado-GLP"
3. Adicione um app Android:
   - Package name: `com.mercadoglp.app`
   - Download `google-services.json`
   - Coloque em `app/`

4. Habilite serviços:
   - Authentication (Email/Password, Google)
   - Firestore Database
   - Cloud Storage
   - Analytics
   - Crashlytics

### 1.3 Configurar Google Maps

1. Acesse [Google Cloud Console](https://console.cloud.google.com/)
2. Crie/selecione projeto
3. Ative APIs:
   - Maps SDK for Android
   - Geocoding API
   - Places API
4. Crie credenciais (API Key)
5. Adicione ao `local.properties`:

```properties
MAPS_API_KEY=sua_chave_aqui
```

### 1.4 Configurar Google Play Billing

1. Acesse [Google Play Console](https://play.google.com/console/)
2. Crie app
3. Configure produtos in-app:
   - ID: `premium_monthly`
   - Preço: R$ 29,90
   - ID: `premium_yearly`
   - Preço: R$ 299,90

---

## 2. Estrutura do Projeto

### 2.1 Estrutura de Pastas

```
app/src/main/
├── java/com/glp/anp/
│   ├── GlpApplication.kt                    # Application class
│   ├── MainActivity.kt                       # Activity principal
│   │
│   ├── data/
│   │   ├── models/
│   │   │   ├── Revenda.kt                   # ✅ Já criado
│   │   │   ├── User.kt                      # ✅ Já criado
│   │   │   ├── Subscription.kt
│   │   │   └── ApiResponse.kt
│   │   │
│   │   ├── local/
│   │   │   ├── database/
│   │   │   │   ├── AppDatabase.kt
│   │   │   │   ├── RevendaDao.kt
│   │   │   │   ├── UserDao.kt
│   │   │   │   └── FavoriteDao.kt
│   │   │   └── preferences/
│   │   │       ├── PreferencesManager.kt
│   │   │       └── UserPreferences.kt
│   │   │
│   │   ├── remote/
│   │   │   ├── api/
│   │   │   │   ├── AnpApiService.kt
│   │   │   │   └── AuthApiService.kt
│   │   │   └── interceptors/
│   │   │       ├── AuthInterceptor.kt
│   │   │       └── ErrorInterceptor.kt
│   │   │
│   │   └── repository/
│   │       ├── RevendaRepository.kt
│   │       ├── UserRepository.kt
│   │       ├── AuthRepository.kt
│   │       └── SubscriptionRepository.kt
│   │
│   ├── domain/
│   │   ├── usecases/
│   │   │   ├── auth/
│   │   │   │   ├── LoginUseCase.kt
│   │   │   │   ├── RegisterUseCase.kt
│   │   │   │   └── LogoutUseCase.kt
│   │   │   ├── revenda/
│   │   │   │   ├── GetRevendasUseCase.kt
│   │   │   │   ├── FilterRevendasUseCase.kt
│   │   │   │   └── ExportRevendasUseCase.kt
│   │   │   └── premium/
│   │   │       ├── PurchaseSubscriptionUseCase.kt
│   │   │       └── ValidateSubscriptionUseCase.kt
│   │   │
│   │   └── validators/
│   │       ├── EmailValidator.kt
│   │       └── PasswordValidator.kt
│   │
│   ├── ui/
│   │   ├── theme/
│   │   │   ├── Color.kt
│   │   │   ├── Theme.kt
│   │   │   └── Type.kt
│   │   │
│   │   ├── navigation/
│   │   │   └── AppNavigation.kt             # ✅ Já criado
│   │   │
│   │   ├── components/
│   │   │   ├── LoadingScreen.kt
│   │   │   ├── ErrorScreen.kt
│   │   │   ├── EmptyState.kt
│   │   │   ├── SearchBar.kt
│   │   │   ├── FilterDialog.kt
│   │   │   └── ConfirmDialog.kt
│   │   │
│   │   ├── splash/
│   │   │   ├── SplashScreen.kt
│   │   │   └── SplashViewModel.kt
│   │   │
│   │   ├── auth/
│   │   │   ├── LoginScreen.kt
│   │   │   ├── LoginViewModel.kt
│   │   │   ├── RegisterScreen.kt
│   │   │   ├── RegisterViewModel.kt
│   │   │   └── AuthViewModel.kt
│   │   │
│   │   ├── dashboard/
│   │   │   ├── DashboardScreen.kt           # ✅ Já criado
│   │   │   ├── DashboardViewModel.kt
│   │   │   ├── MapScreen.kt
│   │   │   ├── MapViewModel.kt
│   │   │   ├── StatisticsScreen.kt
│   │   │   └── StatisticsViewModel.kt
│   │   │
│   │   ├── premium/
│   │   │   ├── PremiumScreen.kt
│   │   │   ├── PremiumViewModel.kt
│   │   │   └── components/
│   │   │       ├── PlanCard.kt
│   │   │       └── FeatureItem.kt
│   │   │
│   │   ├── profile/
│   │   │   ├── ProfileScreen.kt
│   │   │   ├── ProfileViewModel.kt
│   │   │   ├── EditProfileScreen.kt
│   │   │   └── ChangePasswordScreen.kt
│   │   │
│   │   └── settings/
│   │       ├── SettingsScreen.kt
│   │       └── SettingsViewModel.kt
│   │
│   ├── di/
│   │   ├── AppModule.kt
│   │   ├── DatabaseModule.kt
│   │   ├── NetworkModule.kt
│   │   └── RepositoryModule.kt
│   │
│   └── utils/
│       ├── Constants.kt
│       ├── Extensions.kt
│       ├── NetworkUtils.kt
│       ├── DateUtils.kt
│       └── CsvParser.kt
│
└── res/
    ├── layout/
    ├── values/
    │   ├── strings.xml                      # ✅ Já criado (PT)
    │   ├── colors.xml
    │   ├── themes.xml
    │   └── dimens.xml
    ├── values-en/
    │   └── strings.xml                      # ✅ Já criado (EN)
    ├── values-es/
    │   └── strings.xml                      # Criar (ES)
    ├── drawable/
    ├── mipmap/
    └── xml/
```

---

## 3. Implementação por Módulo

### 3.1 Application Class

```kotlin
// GlpApplication.kt
package com.glp.anp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class GlpApplication : Application(), Configuration.Provider {
    
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    
    override fun onCreate() {
        super.onCreate()
        
        // Inicializar Firebase
        FirebaseApp.initializeApp(this)
        
        // Configurar Timber para logs
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        // Outras inicializações
        setupWorkManager()
    }
    
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
    
    private fun setupWorkManager() {
        // Configurar sincronização periódica
        // WorkManager será usado para sincronizar dados ANP
    }
}
```

### 3.2 Database (Room)

```kotlin
// AppDatabase.kt
package com.glp.anp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.glp.anp.data.models.*

@Database(
    entities = [
        Revenda::class,
        User::class,
        Favorite::class,
        SearchHistory::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun revendaDao(): RevendaDao
    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    
    companion object {
        const val DATABASE_NAME = "glp_database"
    }
}
```

```kotlin
// RevendaDao.kt
package com.glp.anp.data.local.database

import androidx.room.*
import com.glp.anp.data.models.Revenda
import kotlinx.coroutines.flow.Flow

@Dao
interface RevendaDao {
    
    @Query("SELECT * FROM revendas")
    fun getAllRevendasFlow(): Flow<List<Revenda>>
    
    @Query("SELECT * FROM revendas WHERE cnpj = :cnpj")
    suspend fun getRevendaByCnpj(cnpj: String): Revenda?
    
    @Query("SELECT * FROM revendas WHERE uf = :uf")
    suspend fun getRevendasByState(uf: String): List<Revenda>
    
    @Query("SELECT * FROM revendas WHERE municipio = :municipio")
    suspend fun getRevendasByCity(municipio: String): List<Revenda>
    
    @Query("SELECT * FROM revendas WHERE distribuidora = :distribuidora")
    suspend fun getRevendasByDistributor(distribuidora: String): List<Revenda>
    
    @Query("SELECT DISTINCT uf FROM revendas ORDER BY uf")
    suspend fun getAllStates(): List<String>
    
    @Query("SELECT DISTINCT municipio FROM revendas WHERE uf = :uf ORDER BY municipio")
    suspend fun getCitiesByState(uf: String): List<String>
    
    @Query("SELECT DISTINCT distribuidora FROM revendas ORDER BY distribuidora")
    suspend fun getAllDistributors(): List<String>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(revendas: List<Revenda>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(revenda: Revenda)
    
    @Update
    suspend fun update(revenda: Revenda)
    
    @Delete
    suspend fun delete(revenda: Revenda)
    
    @Query("DELETE FROM revendas")
    suspend fun deleteAll()
    
    @Query("SELECT COUNT(*) FROM revendas")
    suspend fun getCount(): Int
}
```

### 3.3 Repository Pattern

```kotlin
// RevendaRepository.kt
package com.glp.anp.data.repository

import com.glp.anp.data.local.database.RevendaDao
import com.glp.anp.data.models.*
import com.glp.anp.data.remote.api.AnpApiService
import com.glp.anp.utils.CsvParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RevendaRepository @Inject constructor(
    private val revendaDao: RevendaDao,
    private val anpApiService: AnpApiService,
    private val csvParser: CsvParser
) {
    
    fun getAllRevendasFlow(): Flow<List<Revenda>> {
        return revendaDao.getAllRevendasFlow()
    }
    
    suspend fun syncDataFromAnp(): Result<Int> {
        return try {
            // Download CSV da ANP
            val csvData = anpApiService.downloadCsv()
            
            // Parse CSV
            val revendas = csvParser.parseCsv(csvData)
            
            // Salvar no banco local
            revendaDao.deleteAll()
            revendaDao.insertAll(revendas)
            
            Result.success(revendas.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getRevendaByCnpj(cnpj: String): Revenda? {
        return revendaDao.getRevendaByCnpj(cnpj)
    }
    
    suspend fun filterRevendas(filter: RevendaFilter): List<Revenda> {
        var revendas = revendaDao.getAllRevendasFlow().map { it }
        
        // Aplicar filtros
        // Implementar lógica de filtro
        
        return emptyList() // Substituir por lógica real
    }
    
    suspend fun getStatistics(): RevendaStatistics {
        // Calcular estatísticas dos dados
        // Implementar lógica
        return RevendaStatistics.empty()
    }
    
    suspend fun getAllStates(): List<String> {
        return revendaDao.getAllStates()
    }
    
    suspend fun getCitiesByState(uf: String): List<String> {
        return revendaDao.getCitiesByState(uf)
    }
    
    suspend fun getAllDistributors(): List<String> {
        return revendaDao.getAllDistributors()
    }
}
```

---

## 4. Sistema de Autenticação

### 4.1 AuthRepository

```kotlin
// AuthRepository.kt
package com.glp.anp.data.repository

import com.glp.anp.data.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    
    val currentUser: Flow<User?> = flow {
        firebaseAuth.currentUser?.let { firebaseUser ->
            val user = getUserFromFirestore(firebaseUser.uid)
            emit(user)
        } ?: emit(null)
    }
    
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val authResult = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            
            val firebaseUser = authResult.user
                ?: return Result.failure(Exception("User is null"))
            
            val user = getUserFromFirestore(firebaseUser.uid)
                ?: return Result.failure(Exception("User not found in database"))
            
            // Atualizar último login
            updateLastLogin(user.uid)
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun register(request: RegisterRequest): Result<User> {
        return try {
            // Criar usuário no Firebase Auth
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(request.email, request.password)
                .await()
            
            val firebaseUser = authResult.user
                ?: return Result.failure(Exception("User is null"))
            
            // Criar perfil no Firestore
            val user = User(
                uid = firebaseUser.uid,
                email = request.email,
                displayName = request.displayName,
                photoUrl = null,
                phoneNumber = null,
                createdAt = System.currentTimeMillis()
            )
            
            saveUserToFirestore(user)
            
            // Enviar email de verificação
            firebaseUser.sendEmailVerification().await()
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun logout() {
        firebaseAuth.signOut()
    }
    
    private suspend fun getUserFromFirestore(uid: String): User? {
        return try {
            val document = firestore.collection("users")
                .document(uid)
                .get()
                .await()
            
            document.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }
    
    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users")
            .document(user.uid)
            .set(user)
            .await()
    }
    
    private suspend fun updateLastLogin(uid: String) {
        firestore.collection("users")
            .document(uid)
            .update("lastLoginAt", System.currentTimeMillis())
            .await()
    }
}
```

### 4.2 LoginViewModel

```kotlin
// LoginViewModel.kt
package com.glp.anp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glp.anp.data.models.LoginRequest
import com.glp.anp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    
    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }
    
    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }
    
    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val result = authRepository.login(
                email = _uiState.value.email,
                password = _uiState.value.password
            )
            
            result.fold(
                onSuccess = { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Erro desconhecido"
                        )
                    }
                }
            )
        }
    }
}
```

---

## 5. Multi-idioma

### 5.1 LocaleManager

```kotlin
// LocaleManager.kt
package com.glp.anp.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

object LocaleManager {
    
    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }
    
    fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
    }
}
```

### 5.2 Strings em Espanhol

Criar arquivo `res/values-es/strings.xml` com traduções para espanhol.

---

## 6. Sistema Premium

### 6.1 BillingManager

```kotlin
// BillingManager.kt
package com.glp.anp.billing

import android.app.Activity
import com.android.billingclient.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingManager @Inject constructor() : PurchasesUpdatedListener {
    
    private lateinit var billingClient: BillingClient
    
    private val _purchaseState = MutableStateFlow<PurchaseState>(PurchaseState.Idle)
    val purchaseState: StateFlow<PurchaseState> = _purchaseState
    
    fun initialize(activity: Activity) {
        billingClient = BillingClient.newBuilder(activity)
            .setListener(this)
            .enablePendingPurchases()
            .build()
        
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryPurchases()
                }
            }
            
            override fun onBillingServiceDisconnected() {
                // Reconectar
            }
        })
    }
    
    fun queryProducts(productIds: List<String>, callback: (List<ProductDetails>) -> Unit) {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                productIds.map { productId ->
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                }
            )
            .build()
        
        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                callback(productDetailsList)
            }
        }
    }
    
    fun launchBillingFlow(activity: Activity, productDetails: ProductDetails) {
        val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken
            ?: return
        
        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .setOfferToken(offerToken)
                        .build()
                )
            )
            .build()
        
        billingClient.launchBillingFlow(activity, params)
    }
    
    private fun queryPurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                handlePurchases(purchases)
            }
        }
    }
    
    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: List<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK 
            && purchases != null) {
            handlePurchases(purchases)
        }
    }
    
    private fun handlePurchases(purchases: List<Purchase>) {
        purchases.forEach { purchase ->
            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                if (!purchase.isAcknowledged) {
                    acknowledgePurchase(purchase)
                }
                _purchaseState.value = PurchaseState.Purchased(purchase)
            }
        }
    }
    
    private fun acknowledgePurchase(purchase: Purchase) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        
        billingClient.acknowledgePurchase(params) { billingResult ->
            // Handle acknowledgement
        }
    }
}

sealed class PurchaseState {
    object Idle : PurchaseState()
    data class Purchased(val purchase: Purchase) : PurchaseState()
    data class Error(val message: String) : PurchaseState()
}
```

---

## 7. Integração ANP

### 7.1 AnpApiService

```kotlin
// AnpApiService.kt
package com.glp.anp.data.remote.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming

interface AnpApiService {
    
    @Streaming
    @GET("centrais-de-conteudo/dados-abertos/arquivos/cadastro-revendas-glp.csv")
    suspend fun downloadCsv(): Response<ResponseBody>
}
```

### 7.2 CsvParser

```kotlin
// CsvParser.kt
package com.glp.anp.utils

import com.glp.anp.data.models.Revenda
import com.opencsv.CSVReader
import java.io.StringReader
import javax.inject.Inject

class CsvParser @Inject constructor() {
    
    fun parseCsv(csvData: String): List<Revenda> {
        val revendas = mutableListOf<Revenda>()
        
        try {
            val reader = CSVReader(StringReader(csvData))
            val lines = reader.readAll()
            
            // Ignorar header
            lines.drop(1).forEach { line ->
                try {
                    val revenda = parseRevendaLine(line)
                    revendas.add(revenda)
                } catch (e: Exception) {
                    // Log e continuar
                }
            }
        } catch (e: Exception) {
            // Log erro
        }
        
        return revendas
    }
    
    private fun parseRevendaLine(line: Array<String>): Revenda {
        return Revenda(
            cnpj = line[0].trim(),
            razaoSocial = line.getOrNull(1)?.trim(),
            nomeFantasia = line.getOrNull(2)?.trim(),
            logradouro = line.getOrNull(3)?.trim(),
            numero = line.getOrNull(4)?.trim(),
            complemento = line.getOrNull(5)?.trim(),
            bairro = line.getOrNull(6)?.trim(),
            cep = line.getOrNull(7)?.trim(),
            municipio = line[8].trim(),
            uf = line[9].trim(),
            distribuidora = line[10].trim(),
            classe = line[11].trim(),
            latitude = line.getOrNull(12)?.toDoubleOrNull(),
            longitude = line.getOrNull(13)?.toDoubleOrNull(),
            telefone = line.getOrNull(14)?.trim(),
            email = line.getOrNull(15)?.trim()
        )
    }
}
```

---

## 8. Testes

### 8.1 Testes Unitários

```kotlin
// RevendaRepositoryTest.kt
class RevendaRepositoryTest {
    // Implementar testes
}
```

### 8.2 Testes de UI

```kotlin
// LoginScreenTest.kt
class LoginScreenTest {
    // Implementar testes Compose
}
```

---

## 9. Deploy

### 9.1 Build Release

```bash
./gradlew assembleRelease
```

### 9.2 Gerar APK Assinado

1. Build > Generate Signed Bundle/APK
2. Selecione keystore
3. Configure build variants

### 9.3 Google Play Store

1. Prepare assets:
   - Screenshots
   - Ícone
   - Feature graphic
   - Descrições

2. Configure:
   - Versão
   - Política de privacidade
   - Classificação de conteúdo

3. Upload APK/AAB

---

## 📊 Checklist de Implementação

- [ ] Configurar Firebase
- [ ] Configurar Google Maps
- [ ] Configurar Billing
- [ ] Implementar autenticação
- [ ] Implementar banco de dados local
- [ ] Implementar sincronização ANP
- [ ] Implementar filtros
- [ ] Implementar mapas
- [ ] Implementar estatísticas
- [ ] Implementar sistema premium
- [ ] Implementar multi-idioma
- [ ] Testar em dispositivos físicos
- [ ] Otimizar performance
- [ ] Preparar para produção

---

## 🔗 Links Úteis

- [Documentação Android](https://developer.android.com/docs)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Firebase](https://firebase.google.com/docs)
- [Google Maps SDK](https://developers.google.com/maps/documentation/android-sdk)
- [Billing Library](https://developer.android.com/google/play/billing)
- [Dados ANP](https://www.gov.br/anp/pt-br/centrais-de-conteudo/dados-abertos)

---

## 📞 Suporte

Para dúvidas ou problemas, entre em contato através do email: suporte@glpapp.com
