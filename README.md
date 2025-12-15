# 🥗 Calories Calculator


## 📷 Demonstração

<img width="1903" height="943" alt="image" src="https://github.com/user-attachments/assets/73b3399f-1de9-40a7-b620-8f9b7cd22170" />
<img width="1384" height="644" alt="image" src="https://github.com/user-attachments/assets/1e6d5cb4-ace7-4e60-9d08-b098f3e79917" />
<img width="891" height="505" alt="image" src="https://github.com/user-attachments/assets/93fe5739-937b-4003-9464-ebab8bb759a7" />


## 🎯 Intuito do Projeto
Manter a prática na linguagem, consolidar conhecimentos e me desafiar a aprender ainda mais no desenvolvimento backend e web.

## 🧠 Objetivo do Projeto
Criar uma plataforma web que contenha dados nutricionais da **API TACO**, oferecendo um sistema completo de **login e gerenciamento de usuários**.

A aplicação permite:
- Alocar alimentos em **fichas alimentares diárias**
- Manter o **histórico de refeições**
- Definir **metas nutricionais personalizadas** (calorias e macronutrientes)
- Realizar **cálculos automáticos** para acompanhamento do progresso
- Ajudar o usuário a alcançar um **peso objetivo** ao longo do tempo

## ⚙️ Funcionalidades
- Cadastro e autenticação de usuários
- Consumo de dados nutricionais pegas da TACO API (foi retirado o uso da API diretamente e realocado para o banco de dados do projeto nas ultimas atualizacoes)
- Montagem de ficha alimentar por dia
- Histórico de consumo alimentar
- Cálculo automático de calorias e macronutrientes
- Definição de metas diárias e acompanhamento do progresso

## 🛠️ Tecnologias Utilizadas
- Java  
- Spring Boot  
- Spring MVC  
- Thymeleaf  
- JPA / Hibernate  
- MySQL (ou outro banco relacional)  
- Git & GitHub  
- Javascript

## ▶️ Como Rodar o Projeto

### Pré-requisitos
Certifique-se de ter instalado em sua máquina:
- Java 21  
- Maven
- Git
- IDE (De sua preferência que rode Java)

### ⚙️ Passos para execução

1. Clone o repositório:
```bash
https://github.com/TiagoCostaSantos/CaloriesCalculator.git
```
2. Entre na pasta que clonou o repositório dentro de uma IDE
```bash
cd (nome da pasta)
```
3. compile e rode o projeto:
```bash
mvn spring-boot:run
```
5. acesse o navegador:
```bash
http://localhost:8080
```
6. Para acessar o banco de dados do projeto entre:
```bash
http://localhost:8080/h2-console
```


