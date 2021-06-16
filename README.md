# Projeto Final de API

## Como rodar?

Você tem duas opções para rodar esse projeto, um via Docker (usando Docker-compose) e outro fazendo `build` via [STS](https://spring.io/guides/gs/sts/).

Se optar por rodar fazendo o build via STS, lembre-se de configurar o acesso do banco de dados no arquivo [application.yml](src/main/resources/application.yml).

### Rodando com Docker e Docker-compose

Você precisa ter o Docker instalado em sua máquina, siga as instruções disponíveis em https://docs.docker.com/get-docker/.

Após isso, siga as instruções disponíveis em https://docs.docker.com/compose/install/ para instalar o Docker-compose.

Após clonar esse repositório, inicie o ambiente com o comando 
```bash
docker-compose up -d
```

Com o uso do parâmetro `-d` garantimos que os contêineres docker rodem em background.


Após os contêineres subirem, a aplicação estará disponível em [http://localhost:8088/api](http://localhost:8088/api).

O Swagger estará disponível em [http://localhost:8088/api/swagger-ui.html](http://localhost:8088/api/swagger-ui.html)
