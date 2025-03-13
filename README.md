# AutoContrato API

## Descrição
AutoContrato é uma API desenvolvida em **Java 23** com **Spring Boot 3.4.3**, projetada para automatizar a gestão de contratos de locação. Com essa API, é possível cadastrar e gerenciar contratos, locatários, proprietários, propriedades e usuários.

## Tecnologias Utilizadas
- **Java 23**
- **Spring Boot 3.4.3**
- **PostgreSQL**
- **JWT (JSON Web Token) para autenticação**
- **Hibernate/JPA**
- **Swagger para documentação**

## Funcionalidades
- Cadastro e gerenciamento de contratos
- Cadastro e gerenciamento de locatários
- Cadastro e gerenciamento de proprietários
- Cadastro e gerenciamento de propriedades
- Cadastro e gerenciamento de usuários
- Autenticação via JWT
- Documentação interativa com Swagger

## Configuração do Ambiente
### Requisitos
Antes de iniciar, certifique-se de ter instalado:
- **Java 23**
- **Maven**
- **PostgreSQL**

### Passo a Passo para Configuração

```sh
# Clone o repositório
git clone https://github.com/seu-usuario/auto-contrato.git
cd auto-contrato

# Configure o banco de dados PostgreSQL
# Crie um banco de dados chamado autoContrato
# Atualize o arquivo application.properties caso necessário

# application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/autoContrato
spring.datasource.username=Seu Usuario
spring.datasource.password=Sua Senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.show-sql=true
api.security.token.secret=${JWT_SECRET:sua senha secreta}

# Instale as dependências
mvn clean install

# Execute a aplicação
mvn spring-boot:run
```

### Acesse a documentação Swagger
```sh
http://localhost:8080/swagger-ui.html
```

## Exemplos de Uso
### Autenticação (Login)
```http
POST /auth/login
Content-Type: application/json
```
```json
{
  "email": "usuario@example.com",
  "senha": "123456"
}
```
**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

