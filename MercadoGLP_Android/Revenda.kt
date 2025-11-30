package com.mercadoglp.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Modelo de dados para Revenda de GLP
 * Baseado nos dados oficiais da ANP
 */
@Entity(tableName = "revendas")
data class Revenda(
    @PrimaryKey
    @SerializedName("cnpj")
    val cnpj: String,
    
    @SerializedName("razao_social")
    val razaoSocial: String?,
    
    @SerializedName("nome_fantasia")
    val nomeFantasia: String?,
    
    @SerializedName("logradouro")
    val logradouro: String?,
    
    @SerializedName("numero")
    val numero: String?,
    
    @SerializedName("complemento")
    val complemento: String?,
    
    @SerializedName("bairro")
    val bairro: String?,
    
    @SerializedName("cep")
    val cep: String?,
    
    @SerializedName("municipio")
    val municipio: String,
    
    @SerializedName("uf")
    val uf: String,
    
    @SerializedName("distribuidora")
    val distribuidora: String,
    
    @SerializedName("classe")
    val classe: String,
    
    @SerializedName("latitude")
    val latitude: Double?,
    
    @SerializedName("longitude")
    val longitude: Double?,
    
    @SerializedName("telefone")
    val telefone: String?,
    
    @SerializedName("email")
    val email: String?,
    
    // Campos adicionais calculados
    val populacaoMunicipio: Int? = null,
    val revendasPorHabitante: Double? = null,
    val dataAtualizacao: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
) {
    /**
     * Retorna o endereço completo formatado
     */
    fun getEnderecoCompleto(): String {
        val partes = mutableListOf<String>()
        
        logradouro?.let { partes.add(it) }
        numero?.let { partes.add("nº $it") }
        complemento?.let { if (it.isNotBlank()) partes.add(it) }
        
        return partes.joinToString(", ")
    }
    
    /**
     * Retorna a localização completa (cidade/estado)
     */
    fun getLocalizacao(): String = "$municipio/$uf"
    
    /**
     * Verifica se a revenda possui coordenadas geográficas
     */
    fun hasCoordinates(): Boolean = latitude != null && longitude != null
    
    /**
     * Retorna o CNPJ formatado (00.000.000/0000-00)
     */
    fun getCnpjFormatado(): String {
        if (cnpj.length != 14) return cnpj
        
        return "${cnpj.substring(0, 2)}.${cnpj.substring(2, 5)}.${cnpj.substring(5, 8)}/" +
                "${cnpj.substring(8, 12)}-${cnpj.substring(12, 14)}"
    }
    
    /**
     * Retorna o CEP formatado (00000-000)
     */
    fun getCepFormatado(): String {
        if (cep == null || cep.length != 8) return cep ?: ""
        return "${cep.substring(0, 5)}-${cep.substring(5, 8)}"
    }
    
    /**
     * Retorna uma cor baseada na classe da revenda
     */
    fun getClasseColor(): Int {
        return when (classe.uppercase()) {
            "GRANEL" -> 0xFF1E88E5.toInt() // Azul
            "ENVASILHAMENTO" -> 0xFF43A047.toInt() // Verde
            "GRANEL E ENVASILHAMENTO" -> 0xFFFB8C00.toInt() // Laranja
            else -> 0xFF757575.toInt() // Cinza
        }
    }
    
    /**
     * Retorna um ícone emoji baseado na classe
     */
    fun getClasseIcon(): String {
        return when (classe.uppercase()) {
            "GRANEL" -> "🚚"
            "ENVASILHAMENTO" -> "🏭"
            "GRANEL E ENVASILHAMENTO" -> "🔄"
            else -> "📦"
        }
    }
    
    /**
     * Verifica se corresponde a um termo de busca
     */
    fun matchesSearchQuery(query: String): Boolean {
        if (query.isBlank()) return true
        
        val searchTerm = query.lowercase()
        return razaoSocial?.lowercase()?.contains(searchTerm) == true ||
                nomeFantasia?.lowercase()?.contains(searchTerm) == true ||
                cnpj.contains(searchTerm) ||
                municipio.lowercase().contains(searchTerm) ||
                uf.lowercase().contains(searchTerm) ||
                distribuidora.lowercase().contains(searchTerm) ||
                logradouro?.lowercase()?.contains(searchTerm) == true ||
                bairro?.lowercase()?.contains(searchTerm) == true
    }
}

/**
 * Filtros para busca de revendas
 */
data class RevendaFilter(
    val estados: List<String> = emptyList(),
    val municipios: List<String> = emptyList(),
    val distribuidoras: List<String> = emptyList(),
    val classes: List<String> = emptyList(),
    val searchQuery: String = "",
    val onlyWithCoordinates: Boolean = false,
    val onlyFavorites: Boolean = false
) {
    fun isEmpty(): Boolean {
        return estados.isEmpty() && 
               municipios.isEmpty() && 
               distribuidoras.isEmpty() && 
               classes.isEmpty() && 
               searchQuery.isBlank() && 
               !onlyWithCoordinates && 
               !onlyFavorites
    }
}

/**
 * Estatísticas agregadas de revendas
 */
data class RevendaStatistics(
    val totalRevendas: Int,
    val revendasPorEstado: Map<String, Int>,
    val revendasPorDistribuidora: Map<String, Int>,
    val revendasPorClasse: Map<String, Int>,
    val revendasComCoordenadas: Int,
    val percentualComCoordenadas: Double,
    val top10Municipios: List<Pair<String, Int>>,
    val mediaRevendasPorMunicipio: Double
) {
    companion object {
        fun empty() = RevendaStatistics(
            totalRevendas = 0,
            revendasPorEstado = emptyMap(),
            revendasPorDistribuidora = emptyMap(),
            revendasPorClasse = emptyMap(),
            revendasComCoordenadas = 0,
            percentualComCoordenadas = 0.0,
            top10Municipios = emptyList(),
            mediaRevendasPorMunicipio = 0.0
        )
    }
}

/**
 * Resultado de análise geográfica
 */
data class AnaliseGeografica(
    val municipio: String,
    val uf: String,
    val totalRevendas: Int,
    val populacao: Int?,
    val revendasPor10kHabitantes: Double?,
    val distribuidoras: List<String>,
    val classesPredominantes: List<String>
)
