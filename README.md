  ## Teste pratico Voto

##### Documentação da aplicação:
Local: http://localhost:8080/swagger-ui.html

Arquitetura simples e funcional
Controller para receber as requisições
Service para aplicar regras de negócios 
Repository para persistências/regaste de informações

Separando responsabilidades e ao mesmo tempo facilitando a mantenabilidade.

##### Validações:
Utilizei bean Validations para os objetos de reqeusts, validando assim algumas regras simples e especificas logo no controller (tudo automático e implícito).
Algumas validações que envolvem consulta em banco de dados estão sendo feitas nos services.

##### Log:
Usei "LoggerFactory" por ser mais simples e funcional.

##### Testes:
Utilizei alguns exemplos de testes unitários usando Mockito e Junit.

##### Versionamento de API:
Utilizei uma forma muito conhecida para versionamento, acrescentando na URL "/v1" no path da aplicação.

##### Tratamento de erros:
Criei uma exceptions especifica e com ela consigo setar mensagens e status code, 
também foi criado um @ControllerAdvice para gerenciamento e tratamento adequado dos erros lançados.

##### Banco de dados:
Optei pelo banco de dados mongoDB, o mesmo está salvando os dados (não se perde quando se reinicia),
Após iniciar o docker-compose.yml, que esta em /src/env/docker-compose.yml o monogDB será iniciado e ficara disponivel na URL
Local: mongodb://localhost:27017

#### Utilizando a aplicação:
Java 11
Spring 2.7.3
maven 3.8.4
docker-compose version 1.29.2  

1- Executar o arquivo **docker-compose.yml** que esta em  **/vote/src/env/** para executar o mongoDB
> `meira@:~/vote/src/env$ docker-compose up -d`

1.1-  Para visualização dos dados no  mongoDB, eu recomento o Compass, aqui nesse [link](https://www.alura.com.br/artigos/como-instalar-mongodb-compass-shell-sistema-linux#conectando-ao-mongodb-com-o-mongodb-compass) tem um pequeno guia de instalação caso não o tenha.

2- Executar a aplicação Spring boot via IDE ou como desejar.

#### Documentação da API
http://localhost:8080/swagger-ui/index.html

![image](https://github.com/Emanuelmeira/vote-test/assets/11339451/3b089931-743d-405a-8113-815a192e634c)

dicionário:
 Pauta = Agenda
 Sessão de votação = Voting session
 Associado = associate
 Voting = Votação
 

#### Fluxo da aplicação!

1. Cadastar uma nova Agenda.
2. Abrir uma Voting session para essa Agenda
   - validações: verifica se a data informada é uma data valida (datas passados não são aceitas). 
   - verifica se o id da Agenda informado pertence a uma Agenda cadastrada.
3. Realizar uma votação para uma Voting session aberta. 
   - verifica se o id da Voting session informado pertence a um dado cadastrado.
   - verifica se a Voting session esta aberta para votação, lembrando que existe apenas um perido pré determinado onde é permitido votar.
   - verifica se o Associate pode votar, checando se o mesmo já votou para essa Agenda
4. Encerrar uma votação informado informando o id da voting session.
   - verifica se a Voting session informada existe
   - verifica se a Voting session informada ainda esta aberta para votação, caso não esteja mais, a mesma tem seus votos contabilizados e um resultado é devolvido.

