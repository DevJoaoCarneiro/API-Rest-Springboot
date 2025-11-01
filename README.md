# 🚗 Sistema de Locação de Carros — ClienteMovCar

Aplicação completa (frontend + backend) para gerenciamento de uma locadora de veículos, permitindo o cadastro de clientes, controle de carros disponíveis, registro de reservas, manutenções e pagamentos.  
O projeto é dividido em duas partes: **frontend (React + Vite)** e **backend (Spring Boot + PostgreSQL)**.

---

## 🧩 Tecnologias Utilizadas

### 💻 **Frontend**
- **React + Vite** — estrutura leve e rápida para desenvolvimento SPA.  
- **JavaScript** — tipagem e manutenção de código.  
- **Axios** — comunicação com a API REST do backend.  
- **React Router DOM** — controle de rotas e navegação entre páginas.  
- **CSS Modules / Styled Components** — estilização isolada e responsiva.  

### ⚙️ **Backend**
- **Spring Boot (Java)** — desenvolvimento de API REST robusta e escalável.  
- **Spring Data JPA / Hibernate** — acesso e persistência de dados.  
- **Lombok** — simplificação de getters/setters e construtores.  
- **Spring Validation** — validação de campos com anotações.  
- **Springdoc OpenAPI / Swagger UI** — documentação automática da API.  

### 🗄️ **Banco de Dados**
- **PostgreSQL** — banco relacional usado para armazenar as informações da locadora.  

---

## 📚 Estrutura do Projeto

### 🧠 **Entidades principais**

| Entidade | Descrição | Relacionamentos |
|-----------|------------|-----------------|
| **Cliente** | Representa o usuário que realiza reservas e pagamentos. | 1:N com `Reserva` |
| **Carro** | Armazena dados de veículos disponíveis para locação. | 1:N com `Reserva` e `Manutenção` |
| **Reserva** | Conecta `Cliente` e `Carro`, registrando o período de locação. | FK → `Cliente`, `Carro` |
| **Pagamento** | Armazena informações sobre pagamentos de reservas. | FK → `Reserva` |
| **Manutenção** | Controla revisões e serviços realizados nos veículos. | FK → `Carro` |

---

## 🔁 Fluxo de Funcionamento

1. **Cadastro de Cliente e Carro** — Feito pelo painel administrativo.  
2. **Criação de Reserva** — O sistema verifica se o carro está disponível antes de confirmar.  
3. **Registro de Pagamento** — Associado a uma reserva.  
4. **Controle de Manutenções** — Impede locações de veículos em manutenção.  
5. **Consultas e Listagens** — Através de tabelas com busca e paginação no front-end.

---

## 🚀 Endpoints Principais (Backend)

### 👥 Clientes
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/clientes` | Cria um novo cliente. |
| `GET` | `/api/clientes` | Lista todos os clientes. |
| `GET` | `/api/clientes/{id}` | Busca cliente por ID. |
| `PUT` | `/api/clientes/{id}` | Atualiza um cliente. |
| `DELETE` | `/api/clientes/{id}` | Remove cliente. |

### 🚘 Carros
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/carros` | Cadastra um novo carro. |
| `GET` | `/api/carros` | Lista todos os carros. |
| `GET` | `/api/carros/disponiveis` | Retorna apenas os disponíveis. |

### 📅 Reservas
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/reservas` | Cria uma reserva. |
| `GET` | `/api/reservas` | Lista todas as reservas. |
| `PATCH` | `/api/reservas/{id}/status` | Atualiza o status da reserva. |

### 💳 Pagamentos
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/pagamentos` | Registra pagamento. |
| `GET` | `/api/pagamentos` | Lista pagamentos. |

### 🛠️ Manutenções
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/manutencoes` | Registra nova manutenção. |
| `GET` | `/api/manutencoes` | Lista todas as manutenções. |

---

## 🧭 Interface Frontend — ClienteMovCar

A interface web foi desenvolvida com React + Vite, oferecendo uma experiência fluida e intuitiva.  
As principais funcionalidades incluem:

- 📋 **Cadastro e edição de clientes e carros**  
- 🔍 **Consulta rápida com busca e filtros**  
- ⏱️ **Gerenciamento de reservas e devoluções**  
- 💰 **Controle de pagamentos**  
- 🧾 **Visualização de histórico de manutenções**

---



