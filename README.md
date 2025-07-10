# 🛒 E-commerce Backend API

> Uma API REST completa para e-commerce desenvolvida em Java com Spring Boot

## 📋 Sobre o Projeto

Este é um backend robusto para e-commerce que fornece todas as funcionalidades essenciais para uma loja online moderna. Desenvolvido com as melhores práticas de desenvolvimento Java e arquitetura REST.

## 🚀 Tecnologias Utilizadas

- **Java 17+** - Linguagem principal
- **Spring Boot 3.x** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização
- **Spring Web** - API REST
- **H2 Database** - Banco de dados para desenvolvimento
- **Maven** - Gerenciamento de dependências
- **Bean Validation** - Validação de dados
- **Swagger/OpenAPI** - Documentação da API

## 🏗️ Arquitetura

```
src/
├── main/
│   ├── java/com/project/ecommerce/
│   │   ├── controller/     # Endpoints da API
│   │   ├── service/        # Regras de negócio
│   │   ├── repository/     # Acesso a dados
│   │   ├── model/          # Entidades JPA
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── config/         # Configurações
│   │   └── exception/      # Tratamento de exceções
│   └── resources/
│       ├── application.properties
│       └── data.sql        # Dados iniciais
```

## 🔧 Funcionalidades

### 👤 Gestão de Usuários
- [x] Cadastro e autenticação de usuários
- [x] Perfis de usuário (Cliente, Admin)
- [x] Gerenciamento de endereços
- [x] Histórico de pedidos

### 🛍️ Catálogo de Produtos
- [x] CRUD completo de produtos
- [x] Categorização de produtos
- [x] Sistema de busca e filtros
- [x] Gestão de estoque
- [x] Upload de imagens

### 🛒 Carrinho de Compras
- [x] Adicionar/remover itens
- [x] Cálculo automático de totais
- [x] Persistência do carrinho
- [x] Aplicação de cupons de desconto

### 💳 Processamento de Pedidos
- [x] Criação de pedidos
- [x] Cálculo de frete
- [x] Múltiplas formas de pagamento
- [x] Rastreamento de status
- [x] Histórico completo

### 📊 Painel Administrativo
- [x] Dashboard com métricas
- [x] Gestão de produtos
- [x] Controle de pedidos
- [x] Relatórios de vendas
- [x] Gestão de usuários

## 🛠️ Instalação e Configuração

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+
- Git

### Passos para instalação

1. **Clone o repositório**
```bash
git clone https://github.com/seu-usuario/ecommerce-backend.git
cd ecommerce-backend
```

2. **Configure o banco de dados**
```properties
# application.properties
spring.datasource.url=jdbc:h2:mem:ecommerce
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
```

3. **Execute a aplicação**
```bash
mvn clean install
mvn spring-boot:run
```

4. **Acesse a aplicação**
- API: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console
- Swagger UI: http://localhost:8080/swagger-ui.html

## 📚 Documentação da API

### Principais Endpoints

#### Autenticação
```
POST /api/auth/login          # Login de usuário
POST /api/auth/register       # Cadastro de usuário
POST /api/auth/refresh-token  # Renovar token
```

#### Produtos
```
GET    /api/products         # Listar produtos
GET    /api/products/{id}    # Buscar produto por ID
POST   /api/products         # Criar produto (Admin)
PUT    /api/products/{id}    # Atualizar produto (Admin)
DELETE /api/products/{id}    # Deletar produto (Admin)
```

#### Carrinho
```
GET    /api/cart             # Ver carrinho
POST   /api/cart/items       # Adicionar item
PUT    /api/cart/items/{id}  # Atualizar quantidade
DELETE /api/cart/items/{id}  # Remover item
```

#### Pedidos
```
GET  /api/orders           # Listar pedidos do usuário
POST /api/orders           # Criar pedido
GET  /api/orders/{id}      # Detalhes do pedido
PUT  /api/orders/{id}/cancel # Cancelar pedido
```

## 🔐 Segurança

- **JWT Authentication** - Tokens seguros para autenticação
- **BCrypt** - Hash de senhas
- **CORS** - Configuração para requisições cross-origin
- **Rate Limiting** - Proteção contra spam
- **Validação de entrada** - Sanitização de dados

## 🧪 Testes

Execute os testes com:
```bash
mvn test
```

Cobertura de testes:
```bash
mvn clean test jacoco:report
```

## 🚀 Deploy

### Docker
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/ecommerce-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Heroku
```bash
git add .
git commit -m "Deploy to Heroku"
git push heroku main
```

## 📈 Roadmap

- [ ] Integração com gateway de pagamento
- [ ] Sistema de notificações em tempo real
- [ ] API de recomendações
- [ ] Integração com transportadoras
- [ ] Sistema de cupons avançado
- [ ] Análise de dados com dashboards

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

**Seu Nome**
- GitHub: [@Flaviohmm](https://github.com/Flaviohmm)
- LinkedIn: [Flavio Macedo](https://linkedin.com/in/flavio-henrique-m2)
- Email: fhenrique609@gmail.com

## 🙏 Agradecimentos

- Spring Boot team pela excelente documentação
- Comunidade Java pela inspiração
- Todos os contribuidores do projeto

---

⭐ Se este projeto te ajudou, considere dar uma estrela!

