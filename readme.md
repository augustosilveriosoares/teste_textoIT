**Projeto Intervalo de prêmios** 

Este Microserviços tem por objetivo atender os requisitos informados na descrição recebida do teste prático.

**Requisitos**
JDK 19

**Rodar o projeto**

Faça o download, ou clone esse repositório, importe o pom.xml no seu editor de preferência, este projeto foi construído com  IntelliJ.
Execute a classe da aplicação do spring boot RaspberryApplication na raiz do pacote com.textoit.raspberry.
Se você usa insomina, pode utilizar o arquivo com as requisições prontas, está na raiz do projeto com o nome insominia.
Caso contrário, seguem as rotas:
http://localhost:8080/list
GET: Satisfaz o requisito da API conforme exigido no teste.
POST: Envie um file no formato CSV como parâmetro.
DELETE: Irá limpar os dados salvos no banco da última lista enviada.

**Teste**

Execute a classe de teste RaspberryApplicationTests em src/test/java.
