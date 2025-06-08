# Serviço de Consultas - ProntoConsulta

Este é o microserviço responsável pelo gerenciamento de consultas médicas no sistema ProntoConsulta.

## Funcionalidades

### Cadastro de Consultas
- **POST** `/consultas`
- Cadastra uma nova consulta com paciente, médico, data e hora
- Valida a existência do paciente e médico nos microserviços correspondentes
- Status padrão é AGENDADA se não especificado
- Retorna confirmação com ID da consulta criada

### Listagem de Consultas

#### Listagem Geral
- **GET** `/consultas` - Lista todas as consultas
- **GET** `/consultas/{id}` - Obtém uma consulta específica

#### Listagem por Filtros
- **GET** `/consultas/paciente/{pacienteId}` - Consultas por paciente
- **GET** `/consultas/medico/{medicoId}` - Consultas por médico
- **GET** `/consultas/status/{status}` - Consultas por status (AGENDADA, REALIZADA, CANCELADA)

#### Listagem Combinada
- **GET** `/consultas/paciente/{pacienteId}/status/{status}` - Por paciente e status
- **GET** `/consultas/medico/{medicoId}/status/{status}` - Por médico e status

### Edição e Cancelamento

#### Atualização
- **PUT** `/consultas/{id}` - Atualiza médico, paciente e data/hora da consulta
- Valida a existência do novo paciente e médico nos microserviços
- Apenas consultas com status AGENDADA podem ser atualizadas

#### Cancelamento
- **PATCH** `/consultas/{id}/cancelar?canceladaPor={MEDICO|PACIENTE}` - Cancela consulta
- Registra quem cancelou (médico ou paciente)
- Não permite cancelar consultas já realizadas ou canceladas

#### Realização
- **PATCH** `/consultas/{id}/realizar` - Marca consulta como realizada
- Não permite alterar consultas já marcadas como realizadas

#### Exclusão
- **DELETE** `/consultas/{id}` - Deleta consulta (requer ID do médico)
- Apenas médicos podem deletar consultas
- Não permite deletar consultas já realizadas

## Estrutura de Dados

### CadastrarConsultaDTO
```json
{
  "medicoId": "med456",
  "pacienteId": "pac123",
  "status": "AGENDADA",
  "datetime": "2024-12-25T14:30:00"
}
```

### AtualizarConsultaDTO
```json
{
  "medicoId": "med456",
  "pacienteId": "pac123",
  "datetime": "2024-12-25T15:00:00"
}
```

### DeletarConsultaDTO
```json
{
  "medicoId": "med456"
}
```

### ConsultaCriadaDTO (Resposta de Criação)
```json
{
  "id": 1,
  "message": "Consulta agendada com sucesso!"
}
```

### ConsultaDTO (Resposta)
```json
{
  "id": 1,
  "datetime": "2024-12-25T14:30:00",
  "status": "AGENDADA",
  "canceladaPor": null,
  "paciente": {
    "id": "pac123",
    "nome": "João Silva",
    "email": "joao@email.com",
    "cpf": "12345678901",
    "telefone": "(11) 99999-9999"
  },
  "medico": {
    "id": "med456",
    "nome": "Dr. Maria Santos",
    "email": "maria@email.com",
    "crm": "123456",
    "especialidade": "Cardiologia",
    "telefone": "(11) 88888-8888"
  }
}
```

## Validações e Regras de Negócio

### Validações de Cadastro
- **medicoId**: Obrigatório e deve existir no service-medicos
- **pacienteId**: Obrigatório e deve existir no service-pacientes
- **datetime**: Obrigatório e não pode ser null
- **status**: Opcional, padrão é AGENDADA

### Regras de Negócio
- Consultas são criadas com status AGENDADA por padrão
- Apenas consultas AGENDADAS podem ser atualizadas
- Consultas REALIZADAS não podem ser canceladas ou deletadas
- Consultas CANCELADAS não podem ser canceladas novamente
- Apenas médicos podem deletar consultas
- Cancelamento deve especificar se foi feito por MEDICO ou PACIENTE
- Sistema valida existência de paciente e médico antes de criar/atualizar
- Integração com microserviços externos é obrigatória para operações

## Status de Consulta
- **AGENDADA** - Consulta marcada, aguardando realização
- **REALIZADA** - Consulta já aconteceu
- **CANCELADA** - Consulta foi cancelada

## Entidades de Cancelamento
- **MEDICO** - Cancelamento feito pelo médico
- **PACIENTE** - Cancelamento feito pelo paciente

## Segurança

### Validação de Integridade
- Verificação de existência de médico e paciente em tempo real
- Tratamento robusto de indisponibilidade de serviços externos

## Arquitetura

### Tecnologias Utilizadas
- **Spring Boot 3.5.0**
- **Spring Data JPA**
- **Spring Web**
- **Spring Validation**
- **Spring Cloud Netflix Eureka Client**
- **MapStruct** (para mapeamento de DTOs)
- **Lombok** (para redução de boilerplate)
- **MySQL** (produção)

### Estrutura do Projeto
```
src/main/java/br/prontoconsulta/service_consultas/
├── controller/           # Controladores REST
├── dtos/                # Data Transfer Objects
├── entity/              # Entidades JPA
├── service/             # Serviços de negócio
├── repository/          # Repositórios JPA
├── mappers/             # Mapeadores MapStruct
├── exceptions/          # Exceções customizadas
├── config/              # Configurações
└── validators/          # Validadores customizados
```

## Integração com Microserviços

Este serviço faz parte da arquitetura de microserviços do ProntoConsulta e se integra com:
- **service-pacientes** - Para validar e obter dados de pacientes
- **service-medicos** - Para validar e obter dados de médicos  
- **Eureka Server** - Para descoberta de serviços

## Configuração

### Eureka
- Registra-se no Eureka Server na porta 8761
- Nome do serviço: `service-consultas`
- Porta: 8083

### Banco de Dados
- **MySQL** via Docker Compose

## Executar a Aplicação

### Pré-requisitos
1. **Eureka Server** rodando na porta 8761
2. **service-pacientes** disponível para validação de pacientes
3. **service-medicos** disponível para validação de médicos
4. Banco de dados MySQL configurado e rodando

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
DB_URL=jdbc:mysql://localhost:3308/db_prontoconsulta_consultas ./mvnw spring-boot:run

# OU executar com a DB_URL padrão
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8083`

## Tratamento de Erros

### Exceções Personalizadas
- **ResourceNotFoundException** (404) - Consulta, paciente ou médico não encontrado
- **UnauthorizedException** (401) - Operação não autorizada
- **ServiceUnavailableException** (503) - Serviço externo indisponível
- **IllegalArgumentException** (400) - Dados inválidos ou regra de negócio violada
- **Exception** (500) - Erro interno do servidor

### Exemplos de Respostas de Erro
```json
{
  "status": 404,
  "message": "Consulta não encontrado com o ID informado."
}
```

```json
{
  "status": 401,
  "message": "Apenas um médico pode deletar uma consulta agendada."
}
```

```json
{
  "status": 400,
  "message": "Não é possível cancelar uma consulta já realizada."
}
```

```json
{
  "status": 503,
  "message": "Serviço temporariamente indisponível. Dados limitados podem estar sendo exibidos."
}
```
