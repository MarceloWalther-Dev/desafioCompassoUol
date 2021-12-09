# DesafioCompassoUol
Para dar início a aplicação é necessário ter instalado o banco de dados mysql versão 5.7 na porta 3306.
Também é possível subir o banco utilizando uma imagem docker, estou disponibilizando o comando para pull da imagem já com as configurações como senha e porta.
docker run --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:5.7]

![image](https://user-images.githubusercontent.com/63797325/142744769-86d420b8-1029-4d40-b569-53c7f56b750a.png)

Endpoints: 

![image](https://user-images.githubusercontent.com/63797325/142744840-fcbfbb4f-19cb-4298-b550-ca0361a6fde7.png)

Utilizei o flyway para versionamento do banco de dados, interpretei o desafio como início de projeto sendo assim para ter um registro do banco.
Disponibilizo também um arquivo afterMigrations.sql para aplicação subir já com alguns registros no banco de dados, não foi aperfeiçoado pois esse não era objetivo do desafio.

Foi necessário utilizar uma biblioteca model mapper para conversão dos objetos, para evitar codigos boilerplates.
Biblioteca apache commons para facilitar a busca da causa raiz das exceptions.

Utilizei o padrão builder na model apiError para não precisar ficar gerando vários construtores.
Na implementação da query dinâmica preferi utilizar jpql do que criteria, por se tratar de poucos campos acredito que seria mais rápido de se desenvolver.

*Alterações:
Tomei a liberdade de acrescentar mais um endpoint com o verbo PATCH caso seja necessária alteração de algum atributo do product.

PATCH localhost:9999/products/{id}.

No momento de retornar as exceptions procurei por padrões utilizados pela comunidade, adotei o Padrão RFC 7807 onde retornar com mais clareza e facilitar o client com as respectivas páginas de erro.
Como o desafio foi proposto por um json simples deixei aplicação com as anotações @JsonIgnore para não serializar, caso curiosidade é somente remover as anotações.

