# 🚀 Guia de Publicação - Google Play Store

## 📱 Mercado do GLP v1.0.0
**Desenvolvedor:** Leonardo Alencar  
**Data:** 30 de Novembro de 2024  
**Package:** com.mercadoglp.app

---

## 📋 CHECKLIST PRÉ-PUBLICAÇÃO

### ✅ Informações do App
- [x] Nome: **Mercado do GLP**
- [x] Package ID: **com.mercadoglp.app**
- [x] Versão: **1.0.0** (versionCode: 1)
- [x] Desenvolvedor: **Leonardo Alencar**
- [x] Email: leonardo.alencar@mercadoglp.com
- [x] Localização: Bacabal, Maranhão, BR

### ✅ Configurações de Build
- [x] Assinatura configurada
- [x] ProGuard rules completas
- [x] Minify habilitado
- [x] Shrink resources habilitado
- [x] Otimizações de release

### ✅ Arquivos Necessários
- [x] Keystore gerado
- [x] ProGuard rules
- [x] Script de build
- [x] Ícone do app
- [x] Screenshots
- [x] Feature graphic

---

## 🔐 GERAÇÃO DO KEYSTORE

### Passo 1: Executar Script

```bash
cd MercadoGLP_Android
chmod +x generate_keystore.sh
./generate_keystore.sh
```

### Passo 2: Salvar Credenciais

**⚠️ MUITO IMPORTANTE - SALVE ESTAS INFORMAÇÕES:**

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
CREDENCIAIS DO KEYSTORE - MERCADO DO GLP
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Keystore File: ../keystore/mercadoglp-release.keystore
Key Alias: mercadoglp-key
Store Password: MercadoGLP@2024#Secure
Key Password: MercadoGLP@2024#Secure

Desenvolvedor: Leonardo Alencar
Organização: Mercado do GLP
Localização: Bacabal, Maranhão, BR
Validade: 30 anos (até 2054)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
⚠️ NUNCA PERCA ESTE ARQUIVO!
   Faça backup em múltiplos locais seguros.
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

### Passo 3: Fazer Backup

```bash
# Copiar para múltiplos locais
cp ../keystore/mercadoglp-release.keystore ~/Backup/
cp ../keystore/mercadoglp-release.keystore /seu/drive/seguro/

# Fazer upload para cloud
# - Google Drive
# - Dropbox
# - Gerenciador de senhas (1Password, LastPass, etc.)
```

---

## 📦 BUILD DO APK/AAB PARA PRODUÇÃO

### Opção 1: Android App Bundle (AAB) - RECOMENDADO

```bash
# Limpar builds anteriores
./gradlew clean

# Gerar AAB assinado
./gradlew bundleRelease

# Localização do arquivo:
# app/build/outputs/bundle/release/app-release.aab
```

**Vantagens do AAB:**
- ✅ Tamanho menor (~30% menor)
- ✅ Download otimizado por dispositivo
- ✅ Requerido pela Play Store
- ✅ Suporte automático a múltiplas arquiteturas

### Opção 2: APK Universal

```bash
# Gerar APK assinado
./gradlew assembleRelease

# Localização do arquivo:
# app/build/outputs/apk/release/app-release.aab
```

### Verificar Assinatura

```bash
# Verificar se AAB está assinado
jarsigner -verify -verbose -certs app/build/outputs/bundle/release/app-release.aab

# Deve mostrar:
# jar verified. ✅
```

---

## 🎨 ASSETS PARA PLAY STORE

### 1. Ícone do App (obrigatório)

**Especificações:**
- Formato: PNG (32-bit)
- Tamanho: 512x512 px
- Sem transparência
- Não pode ser apenas texto

**Design Sugerido:**
```
🔥 Logotipo do Mercado do GLP
Cores: Azul (#1976D2) + Laranja (#FF9800)
Estilo: Moderno, minimalista
Elementos: Fogo/Chama + Texto "GLP"
```

### 2. Feature Graphic (obrigatório)

**Especificações:**
- Formato: PNG ou JPEG
- Tamanho: 1024x500 px
- 24-bit PNG ou JPEG

**Conteúdo Sugerido:**
```
"Mercado do GLP"
"Encontre as melhores revendas de GLP no Brasil"
[Imagem de fundo com elementos do app]
```

### 3. Screenshots (obrigatório - mín. 2)

**Especificações Phone:**
- Formato: PNG ou JPEG
- Tamanho mínimo: 320px
- Tamanho máximo: 3840px
- Recomendado: 1080x1920 px (9:16)

**Screenshots Necessários:**
1. Tela de Login
2. Dashboard com lista de revendas
3. Mapa com localização
4. Detalhes da revenda
5. Tela de estatísticas
6. Tela Premium
7. Perfil do usuário
8. Filtros avançados

### 4. Vídeo Promocional (opcional)

**Especificações:**
- YouTube URL
- Duração: 30 segundos - 2 minutos
- Conteúdo: Demonstração do app

---

## 📝 INFORMAÇÕES DA LISTAGEM

### Título do App
```
Mercado do GLP - Revendas GLP Brasil
```
(Máximo: 50 caracteres)

### Descrição Curta
```
Encontre as melhores revendas de GLP perto de você. Dados oficiais da ANP, mapas gratuitos e análises completas do mercado brasileiro.
```
(Máximo: 80 caracteres)

### Descrição Completa

```
🔥 MERCADO DO GLP - Seu Guia Completo de Revendas GLP

Encontre, compare e analise revendas de GLP (Gás Liquefeito de Petróleo) em todo o Brasil com dados oficiais da Agência Nacional do Petróleo (ANP).

📊 FUNCIONALIDADES PRINCIPAIS

✅ Dados Oficiais ANP
• Mais de 20.000 revendas cadastradas
• Informações atualizadas mensalmente
• Dados oficiais e confiáveis

🗺️ Mapas Interativos (100% Gratuitos!)
• Visualize revendas no mapa
• Encontre as mais próximas de você
• Rotas e direções
• Funciona offline

🔍 Busca Avançada
• Filtre por estado, cidade, distribuidora
• Busque por classe de revenda
• Ordenação personalizada
• Favoritos

📈 Análises e Estatísticas
• Distribuição geográfica
• Ranking de distribuidoras
• Análise por município
• Gráficos interativos

💎 PLANOS DISPONÍVEIS

🆓 Plano Gratuito
• Acesso a até 50 resultados
• Filtros básicos
• Visualização em mapas

💎 Plano Premium (R$ 29,90/mês)
• Resultados ilimitados
• Filtros avançados
• Exportação de dados (CSV, Excel, PDF)
• Comparações entre revendas
• Alertas personalizados
• Histórico de pesquisas
• Suporte prioritário
• Sem anúncios

💎 Plano Premium Anual (R$ 299,90/ano)
• Todos os recursos Premium
• Economize 16%
• Prioridade no suporte

🎯 IDEAL PARA

👔 Profissionais do Setor
• Distribuidores de GLP
• Consultores energéticos
• Analistas de mercado

🏢 Empresas
• Análise de concorrência
• Expansão geográfica
• Inteligência de mercado

📚 Estudantes e Pesquisadores
• Dados para TCC
• Pesquisas acadêmicas
• Análises setoriais

👥 Consumidores
• Encontrar revendas próximas
• Comparar opções
• Informações completas

✨ DIFERENCIAIS

• Dados 100% oficiais da ANP
• Mapas gratuitos (OpenStreetMap)
• Interface moderna e intuitiva
• Funciona offline
• Suporte em português
• Atualizações constantes

🔒 PRIVACIDADE E SEGURANÇA

• Seus dados são protegidos
• Sem compartilhamento com terceiros
• Autenticação segura (Firebase)
• Conformidade com LGPD

📞 SUPORTE

Email: leonardo.alencar@mercadoglp.com
Site: www.mercadoglp.com

Desenvolvido por Leonardo Alencar
Bacabal, Maranhão - Brasil

🌟 Baixe agora e descubra o mercado de GLP!
```
(Máximo: 4000 caracteres)

### Categoria
```
Categoria Principal: Negócios
Categoria Secundária: Produtividade
```

### Tags/Palavras-chave
```
GLP, gás, revendas, ANP, petróleo, energia, distribuidoras, análise de mercado, mapas, Brasil
```

---

## 🌍 CLASSIFICAÇÃO DE CONTEÚDO

### Questionário Play Store

1. **Violência:** Não
2. **Sexo:** Não
3. **Linguagem Imprópria:** Não
4. **Drogas:** Não
5. **Jogos de Azar:** Não
6. **Compras no app:** Sim (Assinaturas)

**Classificação Resultante:** Livre (L)

---

## 💳 CONFIGURAÇÃO DE PREÇOS

### Preço do App
```
Gratuito com compras no app
```

### Produtos In-App (já configurados no código)

1. **Premium Mensal**
   - ID: `premium_monthly`
   - Preço: R$ 29,90/mês
   - Renovação: Mensal
   - Período de teste: 7 dias grátis

2. **Premium Anual**
   - ID: `premium_yearly`
   - Preço: R$ 299,90/ano
   - Renovação: Anual
   - Economia: 16%
   - Período de teste: 7 dias grátis

### Países de Disponibilidade
```
✅ Brasil (principal)
✅ Portugal
✅ Todos os países de língua portuguesa
```

---

## 🚀 PROCESSO DE PUBLICAÇÃO

### Passo 1: Play Console Setup

1. Acesse: https://play.google.com/console
2. Criar conta de desenvolvedor (US$ 25 único)
3. Preencher informações pessoais
4. Aceitar termos

### Passo 2: Criar App

```
Nome: Mercado do GLP
Idioma padrão: Português (Brasil)
Tipo: App
Gratuito/Pago: Gratuito
```

### Passo 3: Upload do AAB

```
1. Produção > Criar nova versão
2. Upload: app-release.aab
3. Nome da versão: 1.0.0
4. Notas da versão:
```

**Notas da Versão v1.0.0:**
```
🎉 Primeira versão do Mercado do GLP!

✨ Funcionalidades:
• Acesso a dados oficiais da ANP
• Mais de 20.000 revendas cadastradas
• Mapas interativos gratuitos
• Busca e filtros avançados
• Análises e estatísticas
• Sistema de assinaturas Premium
• Suporte offline
• Interface em português

🔥 Comece a explorar o mercado de GLP agora!

Desenvolvido por Leonardo Alencar
```

### Passo 4: Preencher Informações

1. **Conteúdo do app:**
   - Classificação: Livre
   - Questionário de conteúdo

2. **Público-alvo:**
   - Idade: 18+
   - Interesse: Negócios, Energia

3. **Política de privacidade:**
   - URL: https://mercadoglp.com/privacy

4. **Categoria do app:**
   - Negócios > Produtividade

5. **Informações de contato:**
   - Email: leonardo.alencar@mercadoglp.com
   - Telefone: +55 (XX) XXXXX-XXXX
   - Site: https://mercadoglp.com

### Passo 5: Configurar Assinaturas

```
Play Console > Monetização > Produtos > Assinaturas

1. Criar produto: premium_monthly
   - Preço: R$ 29,90
   - Período: 1 mês
   - Teste grátis: 7 dias

2. Criar produto: premium_yearly
   - Preço: R$ 299,90
   - Período: 1 ano
   - Teste grátis: 7 dias
```

### Passo 6: Testes

```
1. Teste interno (até 100 testadores)
2. Teste fechado (público limitado)
3. Teste aberto (público amplo)
4. Produção
```

### Passo 7: Enviar para Revisão

```
1. Verificar todos os campos
2. Confirmar assets
3. Clicar em "Enviar para revisão"
4. Aguardar aprovação (1-7 dias)
```

---

## 📊 PÓS-PUBLICAÇÃO

### Monitoramento

1. **Console de Estatísticas**
   - Downloads
   - Avaliações
   - Crashes
   - ANRs (App Not Responding)

2. **Firebase Analytics**
   - Usuários ativos
   - Sessões
   - Retenção
   - Conversões

3. **Billing Reports**
   - Assinaturas ativas
   - Receita
   - Cancelamentos
   - Trial conversions

### Atualizações

```
Frequência recomendada: Mensal

Tipos de atualização:
- Bug fixes (versionCode +1)
- Melhorias (versionCode +1)
- Novas features (versionCode +10)
- Major updates (versionCode +100)
```

---

## ✅ CHECKLIST FINAL

### Antes de Publicar
- [ ] Keystore gerado e backups feitos
- [ ] AAB/APK assinado gerado
- [ ] Todas as strings traduzidas
- [ ] Ícones em todos os tamanhos
- [ ] Screenshots preparados
- [ ] Feature graphic criado
- [ ] Descrições escritas
- [ ] Política de privacidade publicada
- [ ] Produtos in-app configurados
- [ ] Firebase configurado
- [ ] Testes completos realizados

### Durante Publicação
- [ ] Conta de desenvolvedor criada
- [ ] App criado no Console
- [ ] AAB uploaded
- [ ] Informações preenchidas
- [ ] Assets enviados
- [ ] Classificação de conteúdo
- [ ] Preços configurados
- [ ] Enviado para revisão

### Após Aprovação
- [ ] Monitorar crashes
- [ ] Responder avaliações
- [ ] Acompanhar métricas
- [ ] Planejar atualizações

---

## 📞 INFORMAÇÕES DE CONTATO

**Desenvolvedor:** Leonardo Alencar  
**Email:** leonardo.alencar@mercadoglp.com  
**Localização:** Bacabal, Maranhão, Brasil  
**Website:** www.mercadoglp.com  
**Suporte:** suporte@mercadoglp.com  

---

## 🎉 PARABÉNS!

Seu app está pronto para publicação na Google Play Store!

**Próximos passos:**
1. Gerar keystore (./generate_keystore.sh)
2. Build release (./gradlew bundleRelease)
3. Criar conta Play Console
4. Upload e configuração
5. Aguardar aprovação
6. 🚀 LANÇAMENTO!

**Boa sorte, Leonardo! 🎊**

---

📅 Guia criado em: 30 de Novembro de 2024  
📱 App: Mercado do GLP v1.0.0  
👨‍💻 Desenvolvedor: Leonardo Alencar  
🏆 Pronto para o sucesso!
