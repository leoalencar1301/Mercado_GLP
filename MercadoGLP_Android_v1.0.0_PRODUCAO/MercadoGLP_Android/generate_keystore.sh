#!/bin/bash
# Script para gerar Keystore de Assinatura - Mercado do GLP
# Desenvolvedor: Leonardo Alencar
# Data: 30 de Novembro de 2024

echo "╔═══════════════════════════════════════════════════════════════╗"
echo "║                                                               ║"
echo "║          GERADOR DE KEYSTORE - MERCADO DO GLP                ║"
echo "║                                                               ║"
echo "║              Desenvolvedor: Leonardo Alencar                  ║"
echo "║                                                               ║"
echo "╚═══════════════════════════════════════════════════════════════╝"
echo ""

# Criar diretório keystore se não existir
mkdir -p ../keystore

# Informações do keystore
KEYSTORE_FILE="../keystore/mercadoglp-release.keystore"
KEY_ALIAS="mercadoglp-key"
KEYSTORE_PASSWORD="MercadoGLP@2024#Secure"
KEY_PASSWORD="MercadoGLP@2024#Secure"
VALIDITY_DAYS=10950  # 30 anos

# Informações do desenvolvedor
DEVELOPER_NAME="Leonardo Alencar"
ORGANIZATIONAL_UNIT="Desenvolvimento Mobile"
ORGANIZATION="Mercado do GLP"
CITY="Bacabal"
STATE="Maranhao"
COUNTRY="BR"

echo "📝 Gerando keystore de assinatura..."
echo ""
echo "Informações:"
echo "  - Desenvolvedor: $DEVELOPER_NAME"
echo "  - Organização: $ORGANIZATION"
echo "  - Localização: $CITY, $STATE, $COUNTRY"
echo "  - Validade: $VALIDITY_DAYS dias (30 anos)"
echo ""

# Gerar keystore
keytool -genkeypair \
    -keystore "$KEYSTORE_FILE" \
    -alias "$KEY_ALIAS" \
    -keyalg RSA \
    -keysize 2048 \
    -validity $VALIDITY_DAYS \
    -storepass "$KEYSTORE_PASSWORD" \
    -keypass "$KEY_PASSWORD" \
    -dname "CN=$DEVELOPER_NAME, OU=$ORGANIZATIONAL_UNIT, O=$ORGANIZATION, L=$CITY, ST=$STATE, C=$COUNTRY"

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Keystore gerado com sucesso!"
    echo ""
    echo "📁 Localização: $KEYSTORE_FILE"
    echo "🔑 Alias: $KEY_ALIAS"
    echo ""
    echo "⚠️  IMPORTANTE - GUARDE ESTAS INFORMAÇÕES COM SEGURANÇA:"
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "  Keystore Password: $KEYSTORE_PASSWORD"
    echo "  Key Password: $KEY_PASSWORD"
    echo "  Key Alias: $KEY_ALIAS"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    echo "⚠️  NUNCA PERCA ESTE KEYSTORE!"
    echo "   Sem ele, você não poderá atualizar o app na Play Store."
    echo ""
    echo "💾 Faça backup em local seguro:"
    echo "   - Cloud storage (Google Drive, Dropbox, etc.)"
    echo "   - Disco externo"
    echo "   - Gerenciador de senhas"
    echo ""
    
    # Exibir informações do keystore
    echo "📋 Informações do certificado:"
    echo ""
    keytool -list -v -keystore "$KEYSTORE_FILE" -storepass "$KEYSTORE_PASSWORD" | grep -A 5 "Alias name"
    
else
    echo ""
    echo "❌ Erro ao gerar keystore!"
    echo "   Verifique se o Java/keytool está instalado."
    exit 1
fi

echo ""
echo "✅ Processo concluído!"
echo ""
