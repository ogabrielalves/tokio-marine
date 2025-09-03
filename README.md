# Teste Técnico Tokio Marine 
Este projeto consiste em um sistema de agendamento de transferências financeiras, desenvolvido como parte de um teste técnico para a Tokio Marine. O sistema é composto por um backend em Java/Spring Boot e um frontend em Node.js/Vue.js.

## Tecnologias Utilizadas

### Backend

- **Java 11**
- **Spring Boot 2.7.18**
- **H2 Database**

### Frontend

- **Node.js 24**
- **Vue 3**
- **Axios**
- **Naive UI**
- **Vite**

## Funcionalidades

O sistema permite o agendamento de transferências financeiras com as seguintes informações:

- Conta de origem (padrão XXXXXXXXXX)
- Conta de destino (padrão XXXXXXXXXX)
- Valor da transferência
- Taxa (calculada conforme tabela abaixo)
- Data da transferência (data em que será realizada a transferência)
- Data de agendamento (hoje)

### Cálculo da Taxa

A taxa sobre o valor a ser transferido é calculada com base na data da transferência, conforme a seguinte tabela:

| Dias para Transferência | Taxa Fixa (R$) | Percentual sobre o Valor (%) |
|-------------------------|----------------|------------------------------|
| 0                       | 3,00           | 2,5                          |
| 1 a 10                  | 12,00          | 0,0                          |
| 11 a 20                 | 0,00           | 8,2                          |
| 21 a 30                 | 0,00           | 6,9                          |
| 31 a 40                 | 0,00           | 4,7                          |
| 41 a 50                 | 0,00           | 1,7                          |

Além disso, o sistema permite visualizar o extrato de todos os agendamentos cadastrados.

## Como Executar o Projeto

Para executar o projeto, siga os passos abaixo para o backend e o frontend separadamente.

### Backend (Java/Spring Boot)

1.  **Pré-requisitos**:
    - JDK 11 ou superior.
    - Maven.

2.  **Clonar o repositório**:
    ```bash
    git clone https://github.com/ogabrielalves/tokio-marine.git
    cd tokio-api
    ```

3.  **Compilar e Executar**:
    - **Com Maven**:
        ```bash
        ./mvnw clean install
        ./mvnw spring-boot:run
        ```

    O backend estará disponível em `http://localhost:8080` 

### Frontend (Node.js/Vue.js)

1.  **Pré-requisitos**:
    - Node.js 24 ou superior.

2.  **Clonar o repositório**:
    ```bash
    git clone https://github.com/ogabrielalves/tokio-marine.git
    cd tokio-client
    ```

3.  **Instalar as dependências do projeto**:
    ```bash
    npm install
    ```

4.  **Executar o client**:
    ```bash
    npm run dev
    ```

    O frontend estará disponível em `http://localhost:5173` 

## Endpoints da API (Backend)

O backend expõe os seguintes endpoints:

-   **`POST /transfers`**:
    -   **Descrição**: Cria um novo agendamento de transferência.
    -   **Corpo da Requisição (Exemplo)**:
        ```json
        {
            "sourceAccount": "12345678",
            "destinationAccount": "87654231",
            "amount": 100.00,
            "transferDate": "2025-09-05"
        }
        ```

-   **`GET /transfers?page=0&size=10`**:
    -   **Descrição**: Retorna todos os agendamentos de transferências cadastrados.

