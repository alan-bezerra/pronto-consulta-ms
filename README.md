# ProntoConsulta - Plataforma de Agendamento de Consultas Médicas

## Introdução

O **ProntoConsulta** é uma plataforma completa para agendamento e gerenciamento de consultas médicas, desenvolvida com arquitetura de microserviços. O sistema permite o cadastro e gerenciamento de pacientes e médicos, além do agendamento, acompanhamento e controle de consultas médicas de forma integrada e eficiente.

A plataforma foi projetada para atender às necessidades de clínicas médicas, hospitais e profissionais da saúde, oferecendo uma solução robusta, escalável e segura para a gestão de consultas médicas.

## Sumário

- [Arquitetura do Sistema](#arquitetura-do-sistema)
- [Microserviços](#microserviços)
  - [Service Pacientes](#service-pacientes)
  - [Service Médicos](#service-médicos)
  - [Service Consultas](#service-consultas)
  - [Eureka Server](#eureka-server)
  - [API Gateway](#api-gateway)
- [Funcionalidades por Serviço](#funcionalidades-por-serviço)
- [Estruturas de Dados](#estruturas-de-dados)
- [Regras de Negócio](#regras-de-negócio)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Configuração e Instalação](#configuração-e-instalação)
- [Como Executar](#como-executar)
- [Integração entre Serviços](#integração-entre-serviços)

## Arquitetura do Sistema

O ProntoConsulta segue uma arquitetura de microserviços distribuída, garantindo alta disponibilidade, escalabilidade e manutenibilidade. A arquitetura é composta por:

```
┌─────────────────┐    ┌─────────────────┐
│   API Gateway   │    │  Eureka Server  │
│    (Entrada)    │    │   (Discovery)   │
└─────────────────┘    └─────────────────┘
         │                       │
         └───────────┬───────────┘
                     │
         ┌───────────┴───────────┐
         │                       │
┌─────────────────┐    ┌─────────────────┐
│ Service         │    │ Service         │
│ Pacientes       │    │ Médicos         │
│ (Port: 8081)    │    │ (Port: 8082)    │
└─────────────────┘    └─────────────────┘
         │                       │
         └───────────┬───────────┘
                     │
         ┌─────────────────┐
         │ Service         │
         │ Consultas       │
         │ (Port: 8083)    │
         └─────────────────┘
```

### Componentes da Arquitetura

- **API Gateway**: Ponto de entrada único para todas as requisições
- **Eureka Server**: Servidor de descoberta de serviços
- **Service Pacientes**: Gerenciamento de pacientes
- **Service Médicos**: Gerenciamento de médicos
- **Service Consultas**: Gerenciamento de consultas (orquestra os demais serviços)

## Microserviços

### Service Pacientes
**Porta**: 8081  
**Responsabilidade**: Gerenciamento completo de pacientes

- Cadastro, listagem, atualização e exclusão de pacientes
- Validação de unicidade de CPF e email
- Criptografia de senhas com BCrypt
- Formatação automática de CPF e telefone

### Service Médicos
**Porta**: 8082  
**Responsabilidade**: Gerenciamento completo de médicos

- Cadastro, listagem, atualização e exclusão de médicos
- Validação de unicidade de CPF, email e CRM
- Criptografia de senhas com BCrypt
- Gerenciamento de especialidades médicas

### Service Consultas
**Porta**: 8083  
**Responsabilidade**: Orquestração e gerenciamento de consultas

- Agendamento de consultas integrando pacientes e médicos
- Controle de status (AGENDADA, REALIZADA, CANCELADA)
- Filtragem por paciente, médico e status
- Validação em tempo real com os serviços de pacientes e médicos

### Eureka Server
**Porta**: 8761  
**Responsabilidade**: Descoberta e registro de serviços

### API Gateway
**Responsabilidade**: Ponto de entrada e roteamento de requisições

## Funcionalidades por Serviço

### 📋 Gestão de Pacientes

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/pacientes` | Cadastrar novo paciente |
| GET | `/pacientes` | Listar todos os pacientes |
| GET | `/pacientes/{id}` | Obter paciente específico |
| PUT | `/pacientes/{id}` | Atualizar dados do paciente |
| DELETE | `/pacientes/{id}` | Deletar paciente (com confirmação de senha) |

### 👨‍⚕️ Gestão de Médicos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/medicos` | Cadastrar novo médico |
| GET | `/medicos` | Listar todos os médicos |
| GET | `/medicos/{id}` | Obter médico específico |
| PUT | `/medicos/{id}` | Atualizar dados do médico |
| DELETE | `/medicos/{id}` | Deletar médico (com confirmação de senha) |

### 🗓️ Gestão de Consultas

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/consultas` | Agendar nova consulta |
| GET | `/consultas` | Listar todas as consultas |
| GET | `/consultas/{id}` | Obter consulta específica |
| GET | `/consultas/paciente/{id}` | Consultas por paciente |
| GET | `/consultas/medico/{id}` | Consultas por médico |
| GET | `/consultas/status/{status}` | Consultas por status |
| PUT | `/consultas/{id}` | Atualizar consulta |
| PATCH | `/consultas/{id}/cancelar` | Cancelar consulta |
| PATCH | `/consultas/{id}/realizar` | Marcar como realizada |
| DELETE | `/consultas/{id}` | Deletar consulta (apenas médicos) |

## Estruturas de Dados

### Paciente

#### Cadastro
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

#### Resposta
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "(11) 99999-9999",
  "cpf": "123.456.789-01",
  "endereco": "Rua das Flores, 123, São Paulo - SP"
}
```

### Médico

#### Cadastro
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

#### Resposta
```json
{
  "id": 1,
  "nome": "Dr. João Silva",
  "email": "joao@email.com",
  "telefone": "(11) 99999-9999",
  "cpf": "123.456.789-01",
  "endereco": "Rua das Flores, 123, São Paulo - SP",
  "crm": "123456",
  "especialidade": "Cardiologia"
}
```

### Consulta

#### Agendamento
```json
{
  "medicoId": "1",
  "pacienteId": "1",
  "datetime": "2024-12-25T14:30:00",
  "status": "AGENDADA"
}
```

#### Resposta Completa
```json
{
  "id": 1,
  "datetime": "2024-12-25T14:30:00",
  "status": "AGENDADA",
  "canceladaPor": null,
  "paciente": {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@email.com",
    "cpf": "123.456.789-01",
    "telefone": "(11) 99999-9999"
  },
  "medico": {
    "id": 1,
    "nome": "Dr. Maria Santos",
    "email": "maria@email.com",
    "crm": "123456",
    "especialidade": "Cardiologia",
    "telefone": "(11) 88888-8888"
  }
}
```

## Regras de Negócio

### 👤 Pacientes e Médicos
- **Unicidade**: CPF e email devem ser únicos no sistema
- **CRM**: Apenas para médicos, deve ser único
- **Formatação**: CPF e telefone são formatados automaticamente
- **Senhas**: Criptografadas com BCrypt
- **Exclusão**: Requer confirmação com senha do usuário
- **Atualização**: CPF, email e senha não podem ser alterados

### 🗓️ Consultas
- **Status Válidos**: AGENDADA, REALIZADA, CANCELADA
- **Status Padrão**: Consultas são criadas como AGENDADA
- **Validação**: Paciente e médico devem existir no sistema
- **Atualização**: Apenas consultas AGENDADAS podem ser modificadas
- **Cancelamento**: Deve especificar se foi por MEDICO ou PACIENTE
- **Realização**: Consultas realizadas não podem ser canceladas
- **Exclusão**: Apenas médicos podem deletar consultas
- **Integridade**: Sistema valida existência em tempo real

### 📝 Cancelamento de Consultas
- **Quem pode cancelar**: Médicos e pacientes
- **Restrições**: 
  - Consultas REALIZADAS não podem ser canceladas
  - Consultas CANCELADAS não podem ser canceladas novamente
- **Rastreabilidade**: Sistema registra quem cancelou

## Tecnologias Utilizadas

### 🚀 Framework Principal
- **Spring Boot 3.5.0** - Framework base da aplicação
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Criptografia de senhas com BCrypt
- **Spring Web** - APIs REST
- **Spring Validation** - Validação de dados

### ☁️ Microserviços e Cloud
- **Spring Cloud Netflix Eureka** - Service Discovery
- **Spring Cloud Gateway** - API Gateway

### 🗄️ Banco de Dados
- **MySQL** - Banco de dados relacional
- **Docker Compose** - Orquestração de containers de banco

### 🛠️ Ferramentas de Desenvolvimento
- **MapStruct** - Mapeamento entre DTOs e entidades
- **Lombok** - Redução de código boilerplate
- **Maven** - Gerenciamento de dependências

## Configuração e Instalação

### 📋 Pré-requisitos
- **Java 17+**
- **Maven 3.6+**
- **Docker e Docker Compose**
- **MySQL** (via Docker)

### 🗄️ Configuração dos Bancos de Dados

Cada microserviço possui seu próprio banco de dados MySQL:

| Serviço | Porta | Database |
|---------|-------|-------------|
| service-pacientes | 3306 | db_prontoconsulta_pacientes |
| service-medicos | 3307 | db_prontoconsulta_medicos |
| service-consultas | 3309 | db_prontoconsulta_consultas |

### ⚙️ Configuração do Eureka
- **Servidor**: `http://localhost:8761`
- **Registro automático**: Todos os serviços se registram automaticamente

## Como Executar

### 🚀 Execução Completa do Sistema

#### 1. Subir os Bancos de Dados
```bash
# Na raiz de cada microserviço
docker-compose up -d

# Verificar se os containers estão rodando
docker ps
```

#### 2. Iniciar Eureka Server
```bash
cd eureka-server
./mvnw spring-boot:run
```

#### 3. Iniciar os Microserviços

**Service Pacientes:**
```bash
cd service-pacientes
DB_URL=jdbc:mysql://localhost:3306/db_prontoconsulta_pacientes ./mvnw spring-boot:run
```

**Service Médicos:**
```bash
cd service-medicos
DB_URL=jdbc:mysql://localhost:3307/db_prontoconsulta_medicos ./mvnw spring-boot:run
```

**Service Consultas:**
```bash
cd service-consultas
DB_URL=jdbc:mysql://localhost:3309/db_prontoconsulta_consultas ./mvnw spring-boot:run
```

#### 4. Iniciar API Gateway (opcional)
```bash
cd api-gateway
./mvnw spring-boot:run
```

### 🔍 Verificação do Sistema

Após inicializar todos os serviços:

- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **Service Pacientes**: http://localhost:8081
- **Service Médicos**: http://localhost:8082
- **Service Consultas**: http://localhost:8083

### 📊 Ordem de Inicialização Recomendada

1. **Bancos de Dados** (Docker Compose)
2. **Eureka Server**
3. **Service Pacientes**
4. **Service Médicos**
5. **Service Consultas**
6. **API Gateway**

## Tratamento de Erros

### 🚨 Exceções Lançadas

#### Service Pacientes e Médicos
- **ResourceNotFoundException (404)**: Entidade não encontrada
- **IllegalArgumentException (400)**: Dados inválidos ou duplicados
- **Exception (500)**: Erro interno do servidor

#### Service Consultas
- **ResourceNotFoundException (404)**: Consulta, paciente ou médico não encontrado
- **UnauthorizedException (401)**: Operação não autorizada
- **ServiceUnavailableException (503)**: Serviço externo indisponível
- **IllegalArgumentException (400)**: Regra de negócio violada

### 📝 Exemplos de Respostas de Erro

#### Entidade Não Encontrada
```json
{
  "status": 404,
  "message": "Paciente não encontrado com o ID informado."
}
```

#### Dados Duplicados
```json
{
  "status": 400,
  "message": "Já existe um paciente cadastrado com o CPF informado."
}
```

#### Operação Não Autorizada
```json
{
  "status": 401,
  "message": "Apenas um médico pode deletar uma consulta agendada."
}
```

#### Serviço Indisponível
```json
{
  "status": 503,
  "message": "Serviço temporariamente indisponível. Dados limitados podem estar sendo exibidos."
}
```

#### Regra de Negócio Violada
```json
{
  "status": 400,
  "message": "Não é possível cancelar uma consulta já realizada."
}
```

## Integração entre Serviços

### 🔄 Padrões de Integração

#### Service Discovery
- **Eureka Client**: Todos os serviços se registram automaticamente
- **Load Balancing**: RestTemplate com @LoadBalanced

#### Comunicação Síncrona
- **REST APIs**: Comunicação via HTTP/REST
- **DTO Mapping**: MapStruct para conversão de dados
- **Validation**: Validação em tempo real de integridade

### 📊 Fluxo de Integração para Consultas

```
Cliente
  ↓ POST /consultas
Service Consultas
  ↓ GET /pacientes/{id}
Service Pacientes
  ↓ Validação
Service Consultas
  ↓ GET /medicos/{id}  
Service Médicos
  ↓ Validação
Service Consultas
  ↓ Criação da Consulta
Database
  ↓ Resposta Completa
Cliente
```
