### Projet Web Services
Réalisation d'un réseau social pour le partage des annonces (news). 
### Outils et langages de programmation
* Front-end : React JS
* Back-end : Java SOAP web services 
* Package manager : npm, maven
### API Docs
* News api : 
  * `addnew(username, title , content)` ajouter une annonce. 
  * `deletenew(id)` supprimer une annonce. 
  * `updatenew(id, title, content)` modifier une annonce. 
  * `getall()` retourne tous les news. 
  * `getbyuser(username)` retourne tous les annonces d'un utilisateurs. 
  * `getone(id)` retourne une annonce. 
  * `addreaction(newsid,username, score)` ajoute une reaction. 
  * `deletereaction(newsid, username)` supprime une reaction. 
  * `addcomment(newsid, username, comment)` ajoute un commentaire. 
  * `deletecomment (newsid, username, comment)` supprime un commentaire. 
  
* Users API : 
  * `signup(username, password, firstname, lastname)` pour ajouter un utilisateur. 
  * `login(username, password)` retourne s'il existe. 
  * `getinfo(username)` retourne les informations d'un utilisateurs. 
  * `deleteuser(username)` supprime l'utilisateur. 
  * `updateuser(username, firstname,lastname)` pour modifier le nom de l'utilisateur. 
  * `addfriend(username, fusername) `pour ajouter un ami. 
  * `deletefriend(username, fusername)` pouer supprimer un ami. 
### Schema de base de données
* News : 
```cql
 CREATE TABLE ks_rbnr.news (
    id uuid PRIMARY KEY,
    username text
    title text,
    content text,
    comments list<frozen<tuple<text, text>>>,
    createdat timestamp,
    updatedat timestamp,
 )
```
* Users : 
```cql
 CREATE TABLE ks_rbnr.users (
    username text PRIMARY KEY,
    password text,
    firstname text,
    lastname text,
    friends list<text>,
    createdat timestamp,
    updatedat timestamp
 ) 
```
* Reactions : 
```cql
 CREATE TABLE ks_rbnr.reactions (
    username text,
    id_news uuid,
    score int,
    PRIMARY KEY (username, id_news)
) 
```
### Calcul du score total avec MAP/REDUCE 
##### Fonction Map ( [voir le code](./server/src/main/java/com/rbnr/mapreduce/Mapper.java) )
  * Creer une liste de type `Reaction ( id, score )`
```shell
>> [ ("c0bd94cc-3c8e-43bc-a96a-94033a3aae29" , 1) , ("bd909d40-c468-4b83-b599-07e703a54bb4" , -1),("c0bd94cc-3c8e-43bc-a96a-94033a3aae29" , -1),("c0bd94cc-3c8e-43bc-a96a-94033a3aae29" , -1),("c0bd94cc-3c8e-43bc-a96a-94033a3aae29" , -1),("d94d54ec-040e-4fc1-aed9-747c77fa6aa8" , -1) ]
```
  * Trier le tableau par identifiant : 
```shell
>> [ ("bd909d40-c468-4b83-b599-07e703a54bb4" , -1), ("c0bd94cc-3c8e-43bc-a96a-94033a3aae29" , 1) ,("c0bd94cc-3c8e-43bc-a96a-94033a3aae29" , -1),("c0bd94cc-3c8e-43bc-a96a-94033a3aae29" , -1),("c0bd94cc-3c8e-43bc-a96a-94033a3aae29" , -1),("d94d54ec-040e-4fc1-aed9-747c77fa6aa8" , -1) ]
``` 

  * Regrouper les reactions par leurs id : 
```shell
>> [ ("bd909d40-c468-4b83-b599-07e703a54bb4" , [-1]), ("c0bd94cc-3c8e-43bc-a96a-94033a3aae29" , [1, -1 , -1 , -1]) ,  ("d94d54ec-040e-4fc1-aed9-747c77fa6aa8" , [-1]) ]
```
##### Fonction Reduce ( [voir le code](./server/src/main/java/com/rbnr/mapreduce/Mapper.java) )
  * Calculer le resultats : 
```shell
>> [ ("bd909d40-c468-4b83-b599-07e703a54bb4" , -1), ("c0bd94cc-3c8e-43bc-a96a-94033a3aae29" , -2]) ,  ("d94d54ec-040e-4fc1-aed9-747c77fa6aa8" , -1) ]
```

