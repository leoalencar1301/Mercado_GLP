# ProGuard Rules - Mercado do GLP
# Desenvolvedor: Leonardo Alencar
# Versão: 1.0.0

# ════════════════════════════════════════════════════════════════════
# CONFIGURAÇÕES GERAIS
# ════════════════════════════════════════════════════════════════════

# Otimizações
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Preservar informações de linha para stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ════════════════════════════════════════════════════════════════════
# ANDROID
# ════════════════════════════════════════════════════════════════════

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.view.View

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# ════════════════════════════════════════════════════════════════════
# KOTLIN
# ════════════════════════════════════════════════════════════════════

-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# ════════════════════════════════════════════════════════════════════
# JETPACK COMPOSE
# ════════════════════════════════════════════════════════════════════

-keep class androidx.compose.** { *; }
-keep @androidx.compose.runtime.Composable class * { *; }
-keep @androidx.compose.runtime.Stable class * { *; }
-dontwarn androidx.compose.**

# ════════════════════════════════════════════════════════════════════
# ROOM DATABASE
# ════════════════════════════════════════════════════════════════════

-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# ════════════════════════════════════════════════════════════════════
# RETROFIT & GSON
# ════════════════════════════════════════════════════════════════════

# Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**

-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Modelos de dados (altere para seus packages)
-keep class com.mercadoglp.app.data.models.** { *; }
-keepclassmembers class com.mercadoglp.app.data.models.** { *; }

# ════════════════════════════════════════════════════════════════════
# HILT (Dagger)
# ════════════════════════════════════════════════════════════════════

-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

-dontwarn com.google.errorprone.annotations.**

# ════════════════════════════════════════════════════════════════════
# FIREBASE
# ════════════════════════════════════════════════════════════════════

-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# ════════════════════════════════════════════════════════════════════
# OPENSTREETMAP (OSMDroid)
# ════════════════════════════════════════════════════════════════════

-keep class org.osmdroid.** { *; }
-dontwarn org.osmdroid.**

# OSMBonusPack
-keep class org.osmdroid.bonuspack.** { *; }
-dontwarn org.osmdroid.bonuspack.**

# ════════════════════════════════════════════════════════════════════
# BILLING LIBRARY
# ════════════════════════════════════════════════════════════════════

-keep class com.android.billingclient.** { *; }
-dontwarn com.android.billingclient.**

# ════════════════════════════════════════════════════════════════════
# MPANDROIDCHART
# ════════════════════════════════════════════════════════════════════

-keep class com.github.mikephil.charting.** { *; }
-dontwarn com.github.mikephil.charting.**

# ════════════════════════════════════════════════════════════════════
# OKHTTP
# ════════════════════════════════════════════════════════════════════

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# ════════════════════════════════════════════════════════════════════
# OPENCSV
# ════════════════════════════════════════════════════════════════════

-keep class com.opencsv.** { *; }
-dontwarn com.opencsv.**

# ════════════════════════════════════════════════════════════════════
# COIL (Image Loading)
# ════════════════════════════════════════════════════════════════════

-keep class coil.** { *; }
-dontwarn coil.**

# ════════════════════════════════════════════════════════════════════
# ACCOMPANIST
# ════════════════════════════════════════════════════════════════════

-keep class com.google.accompanist.** { *; }
-dontwarn com.google.accompanist.**

# ════════════════════════════════════════════════════════════════════
# CLASSES ESPECÍFICAS DO APP
# ════════════════════════════════════════════════════════════════════

# Preservar Application class
-keep class com.mercadoglp.app.MercadoGLPApplication { *; }

# Preservar ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Preservar Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Preservar Parcelable
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# ════════════════════════════════════════════════════════════════════
# ENUMS
# ════════════════════════════════════════════════════════════════════

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ════════════════════════════════════════════════════════════════════
# REMOVER LOGS EM PRODUÇÃO
# ════════════════════════════════════════════════════════════════════

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# ════════════════════════════════════════════════════════════════════
# WARNINGS CONHECIDOS
# ════════════════════════════════════════════════════════════════════

-dontwarn org.bouncycastle.**
-dontwarn org.conscrypt.**
-dontwarn org.openjsse.**
-dontwarn java.awt.**
-dontwarn javax.swing.**

# ════════════════════════════════════════════════════════════════════
# FIM DAS REGRAS
# ════════════════════════════════════════════════════════════════════
