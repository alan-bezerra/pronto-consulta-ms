# Serviço de Pacientes - ProntoConsulta

Este é o microserviço responsável pelo gerenciamento de pacientes no sistema ProntoConsulta.

## Funcionalidades

### Cadastro de Pacientes
- **POST** `/pacientes`
- Cadastra um novo paciente no sistema
- Valida unicidade de CPF e email
- Criptografa a senha usando BCrypt
- Formata automaticamente CPF e telefone

### Listagem de Pacientes

#### Listagem Geral
- **GET** `/pacientes` - Lista todos os pacientes
- **GET** `/pacientes/{id}` - Obtém um paciente específico

### Edição de Pacientes

#### Atualização
- **PUT** `/pacientes/{id}` - Atualiza dados do paciente
- Permite atualizar: nome, telefone e endereço
- Não permite alterar CPF, email ou senha

#### Exclusão
- **DELETE** `/pacientes/{id}` - Deleta paciente (requer senha)
- Necessário fornecer a senha do paciente para confirmação
- Remove permanentemente o registro do sistema

## Estrutura de Dados

### CadastrarPacienteDTO
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "senhaSegura123",
  "cpf": "12345678901",
  "telefone": "11999999999",
  "endereco": "Rua das Flores, 123, São Paulo - SP"
}
```

### AtualizarPacienteDTO
```json
{
  "nome": "João Silva Santos",
  "telefone": "11888888888",
  "endereco": "Rua Nova, 456, São Paulo - SP"
}
```

### DeletarPacienteDTO
```json
{
  "senha": "senhaSegura123"
}
```

### PacienteDTO (Resposta)
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "11999999999",
  "cpf": "12345678901",
  "endereco": "Rua das Flores, 123, São Paulo - SP"
}
```

## Validações e Regras de Negócio

### Validações de Cadastro
- **Nome**: Obrigatório
- **Email**: Obrigatório e formato válido
- **Senha**: Obrigatória (criptografada com BCrypt)
- **CPF**: Obrigatório, exatamente 11 dígitos, único no sistema
- **Telefone**: Obrigatório
- **Endereço**: Obrigatório

### Regras de Negócio
- CPF é formatado automaticamente (remove caracteres não numéricos)
- Telefone é formatado automaticamente (remove caracteres não numéricos)
- Email é normalizado (trim e lowercase)
- Senhas são criptografadas com BCrypt
- Não é possível cadastrar pacientes com CPF ou email duplicados
- Para deletar um paciente, é necessário confirmar com a senha

## Segurança

- Senhas são criptografadas usando BCrypt
- Verificação de senha obrigatória para deleção
- Todas as rotas são públicas (sem autenticação JWT por enquanto)

## Arquitetura

### Tecnologias Utilizadas
- **Spring Boot 3.5.0**
- **Spring Data JPA**
- **Spring Security**
- **Spring Cloud Netflix Eureka Client**
- **MapStruct** (para mapeamento de DTOs)
- **Lombok** (para redução de boilerplate)
- **MySQL**
- **Docker Compose** (para subir os bancos de dados)

### Estrutura do Projeto
```
src/main/java/br/prontoconsulta/service_pacientes/
├── controller/           # Controladores REST
├── dtos/                # Data Transfer Objects
├── entity/              # Entidades JPA
├── service/             # Serviços de negócio
├── repository/          # Repositórios JPA
├── mappers/             # Mapeadores MapStruct
├── exceptions/          # Exceções customizadas
└── config/              # Configurações
```

## Integração com Microserviços

Este serviço faz parte da arquitetura de microserviços do ProntoConsulta e se integra com:
- **Eureka Server** - Para descoberta de serviços
- **service-consultas** - Fornece dados de pacientes para agendamento de consultas
- **service-medicos** - Integração indireta via service-consultas

## Configuração

### Eureka
- Registra-se no Eureka Server na porta 8761
- Nome do serviço: `service-pacientes`
- Porta: 8081

### Banco de Dados
- **MySQL** via Docker Compose

## Executar a Aplicação

### Subir os Bancos de Dados (Docker Compose)

```bash
# Subir os bancos MySQL para todos os microserviços
docker-compose up -d

# Verificar se os containers estão rodando
docker ps
```

Os bancos de dados estarão disponíveis em:
- **service-pacientes**: `localhost:3306`
- **service-medicos**: `localhost:3307` 
- **service-consultas**: `localhost:3309`

### Executar o Microserviço

```bash
# Compilar
./mvnw clean compile

# Executar aplicação (usando MySQL)
DB_URL=jdbc:mysql://localhost:3306/db_prontoconsulta_pacientes ./mvnw spring-boot:run

# OU executar com a DB_URL padrão
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8081`

## Tratamento de Erros

### Exceções Personalizadas
- **ResourceNotFoundException** (404) - Paciente não encontrado
- **IllegalArgumentException** (400) - Dados inválidos ou duplicados
- **Exception** (500) - Erro interno do servidor

### Exemplos de Respostas de Erro
```json
{
  "status": 404,
  "message": "Paciente não encontrado com o ID informado."
}
```

```json
{
  "status": 400,
  "message": "Já existe um paciente cadastrado com o CPF informado."
}
```

