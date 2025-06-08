# Serviço de Médicos - ProntoConsulta

Este é o microserviço responsável pelo gerenciamento de médicos no sistema ProntoConsulta.

## Funcionalidades

### Cadastro de Médicos
- **POST** `/medicos`
- Cadastra um novo médico no sistema
- Valida unicidade de CPF, email e CRM
- Criptografa a senha usando BCrypt
- Formata automaticamente CPF e telefone

### Listagem de Médicos

#### Listagem Geral
- **GET** `/medicos` - Lista todos os médicos
- **GET** `/medicos/{id}` - Obtém um médico específico

### Edição de Médicos

#### Atualização
- **PUT** `/medicos/{id}` - Atualiza dados do médico
- Permite atualizar: nome, telefone, endereço e especialidade
- Não permite alterar CPF, email, CRM ou senha

#### Exclusão
- **DELETE** `/medicos/{id}` - Deleta médico (requer senha)
- Necessário fornecer a senha do médico para confirmação
- Remove permanentemente o registro do sistema

## Estrutura de Dados

### CadastrarMedicoDTO
```json
{
  "nome": "Dr. João Silva",
  "email": "joao@email.com",
  "senha": "senhaSegura123",
  "cpf": "12345678901",
  "telefone": "11999999999",
  "endereco": "Rua das Flores, 123, São Paulo - SP",
  "crm": "123456",
  "especialidade": "Cardiologia"
}
```

### AtualizarMedicoDTO
```json
{
  "nome": "Dr. João Silva Santos",
  "telefone": "11888888888",
  "endereco": "Rua Nova, 456, São Paulo - SP",
  "especialidade": "Cardiologia Clínica"
}
```

### DeletarMedicoDTO
```json
{
  "senha": "senhaSegura123"
}
```

### MedicoDTO (Resposta)
```json
{
  "id": 1,
  "nome": "Dr. João Silva",
  "email": "joao@email.com",
  "telefone": "11999999999",
  "cpf": "12345678901",
  "endereco": "Rua das Flores, 123, São Paulo - SP",
  "crm": "123456",
  "especialidade": "Cardiologia"
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
- **CRM**: Obrigatório e único no sistema
- **Especialidade**: Obrigatória

### Regras de Negócio
- CPF é formatado automaticamente (remove caracteres não numéricos)
- Telefone é formatado automaticamente (remove caracteres não numéricos)
- Email é normalizado (trim e lowercase)
- Senhas são criptografadas com BCrypt
- Não é possível cadastrar médicos com CPF, email ou CRM duplicados
- Para deletar um médico, é necessário confirmar com a senha

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
src/main/java/br/prontoconsulta/service_medicos/
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
- **service-consultas** - Fornece dados de médicos para agendamento de consultas
- **service-pacientes** - Integração indireta via service-consultas

## Configuração

### Eureka
- Registra-se no Eureka Server na porta 8761
- Nome do serviço: `service-medicos`
- Porta: 8082

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
- **service-medicos**: `localhost:3307`
- **service-pacientes**: `localhost:3308` 
- **service-consultas**: `localhost:3309`

### Executar o Microserviço

```bash
# Compilar
./mvnw clean compile

# Executar aplicação (usando MySQL)
DB_URL=jdbc:mysql://localhost:3307/db_prontoconsulta_medicos ./mvnw spring-boot:run

# OU executar com a DB_URL padrão
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8082`

## Tratamento de Erros

### Exceções Personalizadas
- **ResourceNotFoundException** (404) - Médico não encontrado
- **IllegalArgumentException** (400) - Dados inválidos ou duplicados
- **Exception** (500) - Erro interno do servidor

### Exemplos de Respostas de Erro
```json
{
  "status": 404,
  "message": "Medico não encontrado com o ID informado."
}
```

```json
{
  "status": 400,
  "message": "Já existe um medico cadastrado com o CPF informado."
}
```
