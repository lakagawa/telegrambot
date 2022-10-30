# FIAP / 1SCJR / TelegramBot : FiapBot

---

## Enunciado

Desenvolver um chatbot integrado ao Telegram que consiga responder
a pelo menos cinco mensagens distintas enviadas pelo usuário.
- Bônus 1: Desenvolver as cinco mensagens como se fosse um diálogo
(utilizando uma máquina de estados).
- Bônus 2: Reconhecer padrões de mensagens (via regular
expressions).
- Bônus 3: Integrar esse bot para consultar informações como clima ou
trânsito e devolver para o usuário.
- **Bônus 4:** Entregar a documentação feita em javadoc ou em Markdown
- **Bônus 5:** Construir todo o projeto utilizando o github

---

## Grupo
- RM346958: Thiago de Souza Zanella
- <del>RM346125: Allan Rocha</del> (Desistiu do curso)
- RM346315: Lais Kagawa
- RM346511: Jônatha Lacerda Gonzaga

---

## Repositório Github
- https://github.com/lakagawa/telegrambot

## Javadoc

Disponível no diretório: [docs\javadoc](docs\javadoc)

```mvn clean javadoc:javadoc```

## Configuração do Token

<del>Registre o token no arquivo `src/test/resources/telegramBot.token`</del>

Cadastre a variável de ambiente `TOKEN_BOT` antes de executar.

### Executar o projeto:
  ![EXEC](docs/image/img_intellij_exec_project.png)

### Executar os testes unitários:
  #### JUnit
  ![JUNIT](docs/image/img_intellij_junit_test.PNG)
  #### Mvn
  ![MVN](docs/image/img_intellij_mvn_test.PNG)

---

## Funcionalidades

### Multi linguagens (I18N)

- <del>pt_BR</del> pt
- <del>en_US</del> en
- <del>es_ES</del> es


### Interações

```
/start - Inicia a interação com o bot
/help - Exibe o que o bot é capaz de fazer
/language - O usuário tem a possibilidade de alterar o idioma
```
Mensagens das quais o bot espera uma resposta 'específica' do usuário é exibido alguns botões na qual este pode escolher para facilitar a interação.

### Fluxos

##### Principal
  > /start -> seleciona idioma -> inicia o contexto da piada -> usuário responde 'bora' -> buscar piada API pública -> usuário tenta adivinhar -> feedback das tentativas de usuário (acertou ou não)
##### Desiste
  > /start -> seleciona idioma -> inicia o contexto da piada -> usuário responde 'bora' -> buscar piada API pública -> usuário tenta adivinhar -> feedback das tentativas de usuário (acertou ou não) ->  usuário desiste -> bot retorna com a resposta
##### Talvez mais tarde
  > /start -> seleciona idioma -> inicia o contexto da piada -> usuário responde 'talvez mais tarde' -> bot responde até logo
##### Nope
  > /start -> seleciona idioma -> inicia o contexto da piada -> usuário responde 'nope' -> bot responde até logo


---

#### Fontes

- https://sv443.net/jokeapi/v2/#response-formats
- https://sendpulse.com/knowledge-base/chatbot/regular-expressions
- https://www.regextester.com/97249
- https://www.ometrics.com/support/regular-expression/
- https://www.rexegg.com/regex-quickstart.html
- https://howtodoinjava.com/java/basics/java-naming-conventions/
- https://refactoring.guru/design-patterns/chain-of-responsibility
- https://www.baeldung.com/java-case-insensitive-string-matching
- https://www.unicode.org/emoji/charts/full-emoji-list.html
- https://core.telegram.org/bots/features#keyboards
- https://embarcados.com.br/maquina-de-estado/
