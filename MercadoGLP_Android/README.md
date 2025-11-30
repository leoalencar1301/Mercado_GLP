# 🚀 Mercado do GLP

Aplicativo Android completo para análise de revendas de GLP (Gás Liquefeito de Petróleo) com dados oficiais da ANP.

## 📱 Funcionalidades

### ✨ Recursos Principais
- 📊 Análise completa de dados de revendas GLP
- 🗺️ Visualização em mapa interativo
- 📈 Gráficos e estatísticas detalhadas
- 🔍 Filtros avançados por estado, cidade, distribuidora e classe
- 📍 Geolocalização de revendas
- 💾 Cache offline dos dados

### 🌍 Multi-idioma
- 🇧🇷 Português (Brasil)
- 🇺🇸 English
- 🇪🇸 Español

### 👥 Gestão de Usuários
- 📝 Cadastro de novos usuários
- 🔐 Login seguro com criptografia
- 👤 Perfil personalizável
- 🔄 Recuperação de senha
- 📧 Validação de email

### 💎 Sistema Premium
#### Plano Gratuito
- Visualização básica de revendas
- Filtros limitados
- Máximo 50 resultados por busca
- Anúncios

#### Plano Premium (R$ 29,90/mês)
- ✅ Sem anúncios
- ✅ Exportação de dados (CSV, Excel, PDF)
- ✅ Análise avançada com todos os filtros
- ✅ Resultados ilimitados
- ✅ Comparação entre distribuidoras
- ✅ Alertas personalizados
- ✅ Histórico de buscas
- ✅ Suporte prioritário
- ✅ Dados em tempo real

## 🏗️ Estrutura do Projeto

```
app/
├── src/main/
│   ├── java/com/glp/anp/
│   │   ├── MainActivity.kt
│   │   ├── ui/
│   │   │   ├── auth/
│   │   │   │   ├── LoginActivity.kt
│   │   │   │   ├── RegisterActivity.kt
│   │   │   │   └── ProfileActivity.kt
│   │   │   ├── dashboard/
│   │   │   │   ├── DashboardFragment.kt
│   │   │   │   ├── MapFragment.kt
│   │   │   │   └── StatisticsFragment.kt
│   │   │   ├── premium/
│   │   │   │   ├── PremiumActivity.kt
│   │   │   │   └── SubscriptionManager.kt
│   │   │   └── settings/
│   │   │       └── SettingsActivity.kt
│   │   ├── data/
│   │   │   ├── models/
│   │   │   │   ├── Revenda.kt
│   │   │   │   ├── User.kt
│   │   │   │   └── Subscription.kt
│   │   │   ├── repository/
│   │   │   │   ├── AnpRepository.kt
│   │   │   │   └── UserRepository.kt
│   │   │   └── database/
│   │   │       └── AppDatabase.kt
│   │   ├── network/
│   │   │   ├── AnpApiService.kt
│   │   │   └── AuthApiService.kt
│   │   └── utils/
│   │       ├── Constants.kt
│   │       ├── PreferencesManager.kt
│   │       └── Extensions.kt
│   └── res/
│       ├── layout/
│       ├── values/
│       ├── values-en/
│       └── values-es/
```

## 🛠️ Tecnologias Utilizadas

- **Kotlin** - Linguagem principal
- **Jetpack Compose** - UI moderna e declarativa
- **Room Database** - Persistência local
- **Retrofit** - Requisições HTTP
- **Coroutines & Flow** - Programação assíncrona
- **Hilt** - Injeção de dependências
- **OpenStreetMap (OSMDroid)** - Mapas 100% GRATUITOS 🎉
- **MPAndroidChart** - Gráficos
- **Firebase** - Autenticação e analytics
- **Billing Library** - Assinaturas in-app
- **WorkManager** - Sincronização em background

### 🗺️ Por que OpenStreetMap?

✅ **100% Gratuito** - Sem custos mensais  
✅ **Sem limites** - Usuários ilimitados  
✅ **Sem API Key** - Setup mais simples  
✅ **Offline first** - Cache nativo  
✅ **Open Source** - Comunidade ativa  

**Economia estimada:** US$ 200-2.000/mês comparado ao Google Maps!

## 📦 Dependências (build.gradle)

```gradle
dependencies {
    // Core
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    
    // Jetpack Compose
    implementation 'androidx.compose.ui:ui:1.5.4'
    implementation 'androidx.compose.material3:material3:1.1.2'
    implementation 'androidx.compose.ui:ui-tooling-preview:1.5.4'
    implementation 'androidx.activity:activity-compose:1.8.2'
    
    // Navigation
    implementation 'androidx.navigation:navigation-compose:2.7.6'
    
    // Lifecycle
    implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-compose:2.7.0'
    
    // Room
    implementation 'androidx.room:room-runtime:2.6.1'
    implementation 'androidx.room:room-ktx:2.6.1'
    kapt 'androidx.room:room-compiler:2.6.1'
    
    // Retrofit
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'
    
    // Hilt
    implementation 'com.google.dagger:hilt-android:2.48'
    kapt 'com.google.dagger:hilt-compiler:2.48'
    implementation 'androidx.hilt:hilt-navigation-compose:1.1.0'
    
    // Firebase
    implementation platform('com.google.firebase:firebase-bom:32.7.0')
    implementation 'com.google.firebase:firebase-auth-ktx'
    implementation 'com.google.firebase:firebase-firestore-ktx'
    implementation 'com.google.firebase:firebase-analytics-ktx'
    
    // Google Maps
    implementation 'com.google.android.gms:play-services-maps:18.2.0'
    implementation 'com.google.maps.android:maps-compose:4.3.0'
    
    // Charts
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
    
    // Billing
    implementation 'com.android.billingclient:billing-ktx:6.1.0'
    
    // CSV Parser
    implementation 'com.opencsv:opencsv:5.9'
    
    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3'
    
    // WorkManager
    implementation 'androidx.work:work-runtime-ktx:2.9.0'
}
```

## 🚀 Como Executar

### Pré-requisitos
- Android Studio Hedgehog ou superior
- JDK 17
- Android SDK 34
- Conta Firebase (para autenticação)
- Google Maps API Key

### Configuração

1. Clone o repositório
2. Abra o projeto no Android Studio
3. Configure as chaves no `local.properties`:
```properties
MAPS_API_KEY=sua_chave_google_maps
FIREBASE_API_KEY=sua_chave_firebase
```

4. Sincronize as dependências
5. Execute o app em um emulador ou dispositivo físico

## 💳 Sistema de Assinatura

### Produtos In-App
- **premium_monthly**: R$ 29,90/mês
- **premium_yearly**: R$ 299,90/ano (economize 16%)

### Integração com Google Play Billing
O app utiliza a Google Play Billing Library v6 para processar assinaturas de forma segura.

## 🔐 Segurança

- Autenticação via Firebase Authentication
- Dados criptografados localmente (SQLCipher)
- Comunicação HTTPS
- Validação de assinatura no servidor
- Token JWT para APIs

## 📱 Screenshots

*(Adicione screenshots do app aqui)*

## 🌐 API ANP

O app consome dados oficiais da ANP:
- URL: `https://www.gov.br/anp/pt-br/centrais-de-conteudo/dados-abertos`
- Formato: CSV
- Atualização: Mensal

## 📄 Licença

Este projeto está sob a licença MIT.

## 👥 Contribuidores

- Desenvolvedor Principal: [Seu Nome]

## 📞 Suporte

- Email: suporte@glpapp.com
- Website: www.glpapp.com

## 🔄 Changelog

### v1.0.0 (2024-01-XX)
- ✨ Lançamento inicial
- 🌍 Suporte para 3 idiomas
- 👥 Sistema de usuários
- 💎 Assinaturas premium
- 📊 Análise completa de dados ANP
