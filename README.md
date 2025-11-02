# 🚗 Sistema de Locação de Carros — **ClienteMovCar**

Aplicação completa (**frontend + backend**) para gerenciamento de uma **locadora de veículos**, permitindo o cadastro de clientes, controle de carros disponíveis, registro de reservas, manutenções e pagamentos.  
O projeto é dividido em duas partes: **Frontend (React + Vite)** e **Backend (Spring Boot + PostgreSQL)**.

---

## 🧩 Tecnologias Utilizadas

### 💻 **Frontend**
- ⚡ **React + Vite** — estrutura moderna e leve para desenvolvimento SPA  
- 🔗 **Axios** — comunicação com a API REST  
- 🧭 **React Router DOM** — navegação entre páginas  
- 🎨 **CSS Modules / Styled Components** — estilização isolada e responsiva  

### ⚙️ **Backend**
- ☕ **Spring Boot (Java 21)** — criação da API REST  
- 🗃️ **Spring Data JPA / Hibernate** — persistência de dados  
- ✅ **Spring Validation** — validação com anotações  
- 🧩 **Lombok** — geração automática de getters, setters e construtores  
- 🔄 **MapStruct** — mapeamento entre entidades e DTOs  
- 📘 **Swagger / OpenAPI** — documentação automática dos endpoints  
- 📊 **JaCoCo + SonarCloud** — cobertura e análise de qualidade de código  

### 🗄️ **Banco de Dados**
- 🐘 **PostgreSQL** — banco relacional para armazenamento das entidades  

---

## 📚 Estrutura do Projeto

### 🧠 **Entidades Principais**

| Entidade | Descrição | Relacionamentos |
|-----------|------------|-----------------|
| **Cliente** | Representa o usuário que realiza reservas e pagamentos. | 1:N com `Reserva` |
| **Carro** | Armazena dados de veículos disponíveis para locação. | 1:N com `Reserva` e `Manutenção` |
| **Reserva** | Relaciona `Cliente` e `Carro`, registrando o período de locação. | FK → `Cliente`, `Carro` |
| **Pagamento** | Guarda informações sobre o pagamento de uma reserva. | FK → `Reserva` |
| **Manutenção** | Controla revisões e serviços realizados nos veículos. | FK → `Carro` |

---

## 🔁 Fluxo de Funcionamento

1. 🧍‍♂️ Cadastro de **Cliente** e **Carro**  
2. 📅 Criação de **Reserva** (validação de disponibilidade automática)  
3. 💳 Registro de **Pagamento** vinculado à reserva  
4. 🛠️ Controle de **Manutenções** (impede locação de carros indisponíveis)  
5. 📋 **Consultas** e **listagens** via tabelas dinâmicas no front-end  

---

## 🚀 Endpoints da API (Backend)

### 👥 **Clientes**
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/clientes` | Cria um novo cliente |
| `GET` | `/api/clientes` | Lista todos os clientes |
| `GET` | `/api/clientes/{id}` | Busca cliente por ID |
| `PUT` | `/api/clientes/{id}` | Atualiza cliente |
| `DELETE` | `/api/clientes/{id}` | Remove cliente |

---

### 🚘 **Carros**
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/carros` | Cadastra novo carro |
| `GET` | `/api/carros` | Lista todos os carros |
| `GET` | `/api/carros/{id}` | Busca carro por ID |
| `GET` | `/api/carros/disponiveis` | Retorna carros disponíveis |
| `PUT` | `/api/carros/{id}` | Atualiza informações do carro |
| `DELETE` | `/api/carros/{id}` | Remove carro |

---

### 📅 **Reservas**
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/reservas` | Cria nova reserva |
| `GET` | `/api/reservas` | Lista todas as reservas |
| `GET` | `/api/reservas/{id}` | Busca reserva por ID |
| `PATCH` | `/api/reservas/{id}/status` | Atualiza status da reserva |
| `DELETE` | `/api/reservas/{id}` | Cancela reserva |

---

### 💳 **Pagamentos**
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/pagamentos` | Registra novo pagamento |
| `GET` | `/api/pagamentos` | Lista todos os pagamentos |
| `GET` | `/api/pagamentos/{id}` | Busca pagamento por ID |

---

### 🛠️ **Manutenções**
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/manutencoes` | Cria nova manutenção |
| `GET` | `/api/manutencoes` | Lista todas as manutenções |
| `GET` | `/api/manutencoes/{id}` | Busca manutenção por ID |
| `PUT` | `/api/manutencoes/{id}` | Atualiza dados da manutenção |
| `DELETE` | `/api/manutencoes/{id}` | Remove manutenção |

---

## 🧪 Testes e Qualidade de Código

O projeto utiliza **JaCoCo** para medir a cobertura dos testes e **SonarCloud** para análise contínua de qualidade.

- 📄 **Relatório de cobertura:** `target/site/jacoco/jacoco.xml`  
- ☁️ **Integração com SonarCloud:**
  - Organização: `devjoaocarneiro`
  - Projeto: `DevJoaoCarneiro_API-Rest-Springboot`
  - Dashboard: [🔗 SonarCloud - ClienteMovCar](https://sonarcloud.io/dashboard?id=DevJoaoCarneiro_API-Rest-Springboot)

### 🔧 **Comandos principais**

```bash
# Executa todos os testes e gera relatório JaCoCo
mvn clean verify

# Envia a análise de qualidade e cobertura ao SonarCloud
mvn sonar:sonar
