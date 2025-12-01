#!/bin/bash
# Script de Build para Produção - Mercado do GLP
# Desenvolvedor: Leonardo Alencar
# Versão: 1.0.0

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                                                                ║"
echo "║          BUILD PARA PRODUÇÃO - MERCADO DO GLP                 ║"
echo "║                                                                ║"
echo "║              Desenvolvedor: Leonardo Alencar                   ║"
echo "║              Versão: 1.0.0 (Build 1)                          ║"
echo "║                                                                ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Cores
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Verificar se keystore existe
if [ ! -f "../keystore/mercadoglp-release.keystore" ]; then
    echo -e "${RED}❌ Keystore não encontrado!${NC}"
    echo ""
    echo "Execute primeiro:"
    echo "  ./generate_keystore.sh"
    echo ""
    exit 1
fi

echo -e "${BLUE}📋 Informações do Build:${NC}"
echo "  Package: com.mercadoglp.app"
echo "  Nome: Mercado do GLP"
echo "  Versão: 1.0.0"
echo "  Version Code: 1"
echo "  Min SDK: 24 (Android 7.0)"
echo "  Target SDK: 34 (Android 14)"
echo ""

# Menu de opções
echo -e "${YELLOW}Escolha o tipo de build:${NC}"
echo ""
echo "  1) AAB (Android App Bundle) - RECOMENDADO"
echo "  2) APK (Universal)"
echo "  3) Ambos (AAB + APK)"
echo "  4) Cancelar"
echo ""
read -p "Opção [1-4]: " option

case $option in
    1)
        BUILD_TYPE="AAB"
        ;;
    2)
        BUILD_TYPE="APK"
        ;;
    3)
        BUILD_TYPE="BOTH"
        ;;
    4)
        echo -e "${YELLOW}Build cancelado.${NC}"
        exit 0
        ;;
    *)
        echo -e "${RED}Opção inválida!${NC}"
        exit 1
        ;;
esac

echo ""
echo -e "${BLUE}🧹 Limpando builds anteriores...${NC}"
./gradlew clean

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Erro na limpeza!${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Limpeza concluída!${NC}"
echo ""

# Build AAB
if [ "$BUILD_TYPE" = "AAB" ] || [ "$BUILD_TYPE" = "BOTH" ]; then
    echo -e "${BLUE}📦 Gerando Android App Bundle (AAB)...${NC}"
    echo ""
    
    ./gradlew bundleRelease
    
    if [ $? -eq 0 ]; then
        echo ""
        echo -e "${GREEN}✅ AAB gerado com sucesso!${NC}"
        
        AAB_PATH="app/build/outputs/bundle/release/app-release.aab"
        AAB_SIZE=$(du -h "$AAB_PATH" | cut -f1)
        
        echo ""
        echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        echo "  📁 Localização: $AAB_PATH"
        echo "  💾 Tamanho: $AAB_SIZE"
        echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        echo ""
        
        # Verificar assinatura
        echo -e "${BLUE}🔐 Verificando assinatura...${NC}"
        jarsigner -verify -verbose -certs "$AAB_PATH" | grep "jar verified"
        
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✅ AAB assinado corretamente!${NC}"
        else
            echo -e "${RED}❌ Erro na assinatura do AAB!${NC}"
        fi
        echo ""
    else
        echo -e "${RED}❌ Erro ao gerar AAB!${NC}"
        exit 1
    fi
fi

# Build APK
if [ "$BUILD_TYPE" = "APK" ] || [ "$BUILD_TYPE" = "BOTH" ]; then
    echo -e "${BLUE}📱 Gerando APK Universal...${NC}"
    echo ""
    
    ./gradlew assembleRelease
    
    if [ $? -eq 0 ]; then
        echo ""
        echo -e "${GREEN}✅ APK gerado com sucesso!${NC}"
        
        APK_PATH="app/build/outputs/apk/release/app-release.apk"
        APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
        
        echo ""
        echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        echo "  📁 Localização: $APK_PATH"
        echo "  💾 Tamanho: $APK_SIZE"
        echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        echo ""
        
        # Verificar assinatura
        echo -e "${BLUE}🔐 Verificando assinatura...${NC}"
        jarsigner -verify -verbose -certs "$APK_PATH" | grep "jar verified"
        
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✅ APK assinado corretamente!${NC}"
        else
            echo -e "${RED}❌ Erro na assinatura do APK!${NC}"
        fi
        echo ""
    else
        echo -e "${RED}❌ Erro ao gerar APK!${NC}"
        exit 1
    fi
fi

# Resumo final
echo ""
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                                                                ║"
echo "║                  ✅ BUILD CONCLUÍDO COM SUCESSO!              ║"
echo "║                                                                ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""
echo -e "${GREEN}🎉 Seu app está pronto para publicação!${NC}"
echo ""
echo -e "${YELLOW}📋 Próximos passos:${NC}"
echo ""
echo "  1. Acesse Google Play Console"
echo "  2. Crie ou selecione seu app"
echo "  3. Faça upload do AAB gerado"
echo "  4. Preencha as informações necessárias"
echo "  5. Envie para revisão"
echo ""
echo -e "${BLUE}📖 Consulte o GUIA_PUBLICACAO.md para mais detalhes${NC}"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  Desenvolvedor: Leonardo Alencar"
echo "  Email: leonardo.alencar@mercadoglp.com"
echo "  App: Mercado do GLP v1.0.0"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
