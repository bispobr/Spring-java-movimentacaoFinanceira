# API de análise de transações financeiras

Esta API REST oferece um serviço para processamento automatizado de **arquivos de texto contendo movimentações financeiras**. O sistema realiza o **parse**, **normalização** e **persistência** dos dados em uma base de dados.

## Funcionalidade

- Recebe um arquivo `.txt` com dados de transações financeiras.
- Realiza o parse do conteúdo com base em um formato definido.
- Normaliza os dados (campos, tipos, formatos).
- Persiste as informações estruturadas em uma base de dados relacional.

## Fluxo de Processamento

1. **Upload** do arquivo `.txt` contendo as movimentações.
2. **Leitura** linha a linha do conteúdo do arquivo.
3. **Extração e validação** dos dados de cada linha (ex.: tipo da transação, valor, CPF, data, etc.).
4. **Conversão** dos dados para um formato estruturado.
5. **Armazenamento** no banco de dados.

### Requisitos Documentação do arquivo

| Descrição do campo  | Inicio | Fim | Tamanho | Comentário
| ------------- | ------------- | -----| ---- | ------
| Tipo  | 1  | 1 | 1 | Tipo da transação
| Data  | 2  | 9 | 8 | Data da ocorrência
| Valor | 10 | 19 | 10 | Valor da movimentação. *Obs.* O valor encontrado no arquivo precisa ser divido por cem(valor / 100.00) para normalizá-lo.
| CPF | 20 | 30 | 11 | CPF do beneficiário
| Cartão | 31 | 42 | 12 | Cartão utilizado na transação 
| Hora  | 43 | 48 | 6 | Hora da ocorrência atendendo ao fuso de UTC-3
| Dono da loja | 49 | 62 | 14 | Nome do representante da loja
| Nome loja | 63 | 81 | 19 | Nome da loja

### Documentação sobre os tipos das transações

| Tipo | Descrição | Natureza | Sinal |
| ---- | -------- | --------- | ----- |
| 1 | Débito | Entrada | + |
| 2 | Boleto | Saída | - |
| 3 | Financiamento | Saída | - |
| 4 | Crédito | Entrada | + |
| 5 | Recebimento Empréstimo | Entrada | + |
| 6 | Vendas | Entrada | + |
| 7 | Recebimento TED | Entrada | + |
| 8 | Recebimento DOC | Entrada | + |
| 9 | Aluguel | Saída | - |

## Tecnologias Utilizadas

- **Java + Spring Boot** – Framework principal da aplicação
- **Lombok (@Slf4j)** – Geração de logs
- **Swagger** – Documentação interativa da API
- **Tratamento de Exceções** - @RestControllerAdvice
- **Spring Boot Actuator** – Monitoramento e verificação de saúde da aplicação
- **Integração Actuator + Swagger** – Permite monitorar a saúde da API diretamente pela interface de documentação
- **PostgreSQL** – Banco de dados relacional utilizado
- **Docker** – criação, implantação e gerenciamento de aplicações dentro de contêineres.

## Requisitos

- Java 21+
- Maven


## Executando o Projeto

1. Clone o repositório:

```bash
git clone https://github.com/bispobr/Spring-java-movimentacaofinanceira.git
```

2. Altere o arquivo de configuração **application.properties** com as credenciais de login do PostgreSQL do seu ambiente.

## Como usar

1. Inicie a aplicação
2. A API está acessível através do endereço http://localhost:8080
3. A documentação da API está acessível através do Link http://localhost:8080/swagger-ui/index.html#/
4. O endpoint de saúde e métricas do Actuator está acessível através do Link http://localhost:8080/actuator/health

## Como Rodar em um Container (Opcional)

1. Construa o projeto:

```bash
mvn clean package 
```

2. Gere a Imagem Docker. Com o Docker  instalado execute:

```bash
docker-compose up --build
```

## API Endpoints
API contem os seguintes endpoints:

```http request
Post /transferencia/upload - cadastra as novas transações.
Content-Type: multipart
```

```http request
GET /transferencia/listagem - retorna todas as transações
```

```http request
GET /transferencia/{id} - retorna transação de id especificado
```

```http request
PUT /transferencia/atualizar/{id} - cadastra as novas transações.
Content-Type: application/json

{
  "tipo": "string",
  "natureza": "string",
  "sinal": "string",
  "data": "1900-05-04",
  "valor": 0,
  "cpf": "string",
  "cartao": "string",
  "hora": "string",
  "proprietarioLoja": "string",
  "nomeLoja": "string"
}
```

```http request
GET /transferencia/removerTodos - remove todas as transaçãoes
```

```http request
GET /transferencia/remover/{id} - remover transação identificada pelo id
```
