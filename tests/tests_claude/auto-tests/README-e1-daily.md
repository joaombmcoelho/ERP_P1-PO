# Menu Principal

* A-01-01-M-ok - Abrir aplicação ver menu inicial
* A-01-02-M-ok - Abrir aplicação ver restantes menus
* A-01-03-M-ok - Abrir aplicação com Produto Nacional em import e ver os Produtos  
* A-01-04-M-ok - Abrir aplicação com Produto Importado em import e ver os Produtos  
* A-01-08-M-ok - Abrir aplicação com import e ver data atual 
* A-01-09-M-ok - Avançar data válida e Mostrar data
* A-01-11-M-ok - Avançar data duas vezes e Mostrar data 

# Serialização/Desserialização (Abrir/Guardar em Ficheiro)

* A-02-01-M-ok - Import com produtos e guarda em "produtos.dat"
* A-02-02-M-ok - começar vazio e abrir "produtos.dat", vê Produtos
* A-02-03-M-ok - Serializar Cliente e Produto  e guarda em "cliente.dat"
* A-02-04-M-ok - Começar vazio, abrir "cliente.dat" vê clientes e produtos
* A-02-09-M-ok - Abre ficheiro não existente

# Registar Cliente

* A-03-01-M-ok - Registrar cliente sem estado inicial 
* A-03-02-M-ok - Registrar cliente com estado inicial via import 
* A-03-05-M-ok - Registrar cliente com nome inválido

# Mostrar Clientes

* A-04-01-M-ok - Ver utentes sem nenhum clientes
* A-04-02-M-ok - Abrir aplicação com utentes em import e ver os clientes

# Mostrar Cliente

* A-05-02-M-ok - Mostrar cliente existente
* A-05-04-M-ok - Mostrar cliente não existente com  clientes 

# Registar Empresa

* A-06-01-M-ok  - Registar empresa com import  
* A-06-02-M-ok  - Registar empresa sem import
* A-06-03-M-ok  - Registar empresa duas vezes sem import  

# Mostrar Empresa

* A-07-01-M-ok - Mostrar empresa não existente quando não há empresas 
* A-07-03-M-ok - Mostrar empresa existente (no início)
* A-07-04-M-ok  - Mostrar empresa existente no meio, fim de empresas 

# Mostrar Empresas

* A-08-02-M-ok - Abrir aplicação com uma empresa em import e ver as empresas  
* A-08-02-M-ok - Abrir aplicação com várias empresas em import e ver as empresas  

# Registar Produto

* A-09-01-M-ok - Registrar Produto nacional sem estado inicial 
* A-09-02-M-ok - Registrar Produto importado sem estado inicial 
* A-09-03-M-ok - Registrar Produto Nacional com estado inicial via import
* A-09-04-M-ok - Registrar Produto Nacional e Importado com estado inicial via import

# Mostrar Produto

* A-10-01-M-ok - Mostrar produto não existente quando não há produtos 
* A-10-03-M-ok - Mostrar produto no início, meio e fim com produtos

# Mostrar Produtos

* A-11-01-M-ok - Ver produtos sem nenhum produto, criar um produto e ver produtos todos
* A-11-02-M-ok - Abrir aplicação com vários produtos nacionais em import e ver os produtos 

# Mostrar produtos de uma empresa

* A-12-02-M-ok - Apresentar produtos de uma empresa com um produto Importado com factor 1
* A-12-03-M-ok - Apresentar produtos de uma empresa com um produto Nacional com factor 1
* A-12-04-M-ok - Apresentar produtos de uma empresa com um produto Nacional com factor < 1 e >1
* A-12-05-M-ok - Apresentar produtos de uma empresa com vários produtos com factor 1
* A-12-06-M-ok - Apresentar produtos de uma empresa com vários produtos  nacionais e importados
* A-12-07-M-ok - Apresentar produtos de uma empresa com vários produtos mas inseridos desordenadamente

# Alterar inventário de um produto

* A-13-01-M-ok - Alterar inventário de produto/empresa não existente 
* A-13-02-M-ok - Alterar inventário de produto existente mas não disponibilizado ainda pela empresa (sem produtos)
* A-13-03-M-ok - Alterar inventário de produto existente mas não disponibilizado ainda pela empresa (com produtos)
* A-13-05-M-ok - Alterar inventário de produto/empresa existente mas com fator inválido (< 0)  
* A-13-09-M-ok - Aumentar inventário de produto em empresa  
* A-13-10-M-ok - Aumentar inventário de produto em empresa (com fator diferente) 
* A-13-11-M-ok - Diminuir inventário de produto em empresa   

# Efectuar pesquisa

* A-14-01-M-ok - Encontra um produto com nome igual   
* A-14-02-M-ok - Encontra um produto com nome igual e verifica validade de critério 
* A-14-03-M-ok - Encontra vários produto com nome igual
* A-14-06-M-ok - Não encontra produtos com nome igual ao termo
* A-14-08-M-ok - Encontra um produto com categoria igual   
* A-14-09-M-ok - Encontra vários produto com mesma categoria
* A-14-12-M-ok - Encontra um produto importado com país igual e 2ª procura vazia

