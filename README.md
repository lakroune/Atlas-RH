# Atlas-RH

API REST de gestion des ressources humaines : départements, employés, équipes, contrats et demandes de congé. Le projet est un **backend Spring Boot**, sans interface utilisateur métier ; Swagger UI permet d'explorer ses endpoints.

Le nom technique du projet Maven et du package Java est `ArlasRH`.

## Sommaire

- [Fonctionnalités et rôles](#fonctionnalités-et-rôles)
- [Technologies](#technologies)
- [Installation et lancement](#installation-et-lancement)
- [Architecture et données](#architecture-et-données)
- [API REST](#api-rest)
- [Exemples de requêtes](#exemples-de-requêtes)
- [Règles métier](#règles-métier)
- [Erreurs et validation](#erreurs-et-validation)
- [Tests](#tests)
- [Limites actuelles](#limites-actuelles)

## Fonctionnalités et rôles

| Module | Fonctionnalités implémentées |
|---|---|
| Départements | Créer, lister, consulter, modifier et supprimer |
| Employés | Créer, consulter, modifier le poste et le rôle, supprimer, rechercher par prénom, paginer et trier |
| Équipes | Affecter des employés à un manager par matricule et consulter son équipe |
| Contrats | Créer un contrat, consulter l'historique et terminer un contrat |
| Types de congé | Créer, lister, consulter et supprimer un type avec son quota annuel |
| Demandes de congé | Créer, lister les demandes d'un employé, annuler, approuver ou refuser |
| Solde | Calculer les jours restants pour chaque type de congé |
| Ancienneté | Retourner les années, mois et jours depuis la date d'embauche |

Les rôles définis sont `RESPONSABLE_RH`, `MANAGER` et `EMPLOYE`. Le manager est lui-même un employé. Ces valeurs servent aux règles métier ; aucune authentification ni protection des endpoints par Spring Security n'est configurée dans le code actuel.

## Technologies

Versions déclarées dans [pom.xml](pom.xml), sans présumer de leur validation à l'exécution :

| Élément | Configuration |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.1 |
| Web | Spring Web MVC |
| Persistance | Spring Data JPA / Hibernate |
| Base de données | PostgreSQL ; image 17 dans Compose |
| Migrations | Liquibase |
| Documentation | springdoc OpenAPI UI 2.8.9 |
| Validation | Jakarta Bean Validation |
| Utilitaires | Lombok |
| Construction | Maven, wrapper fourni |
| Distribution | WAR exécutable, Dockerfile multi-stage |

## Installation et lancement

### 1. Prérequis

Pour le lancement local : JDK 17, PostgreSQL et un accès réseau pour télécharger les dépendances Maven. Le wrapper évite d'installer Maven séparément.

Pour le lancement conteneurisé : Docker avec Docker Compose. Les ports exposés par défaut sont `8080` pour l'application et `5432` pour PostgreSQL.

### 2. Configurer PostgreSQL

Créer une base `atlas_db` et un utilisateur disposant des droits nécessaires à la création des tables. Les propriétés sont dans [application.properties](src/main/resources/application.properties).

| Propriété | Configuration actuelle |
|---|---|
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/atlas_db` |
| `spring.datasource.username` | `ismail` |
| `spring.datasource.password` | À adapter à votre base |
| `spring.jpa.hibernate.ddl-auto` | `update` |
| `spring.jpa.show-sql` | `true` |
| `spring.jpa.open-in-view` | `false` |
| `spring.liquibase.enabled` | `true` |
| `spring.docker.compose.enabled` | `false` |
| `server.servlet.context-path` | `/atlasrh` |

Exemple de surcharge dans PowerShell, dans le même terminal que le lancement :

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/atlas_db"
$env:SPRING_DATASOURCE_USERNAME = "ismail"
$env:SPRING_DATASOURCE_PASSWORD = "votre-mot-de-passe"
```

Le [changelog Liquibase](src/main/resources/db/changelog/db.changelog-master.yaml) contient actuellement un changeset vide. La création et l'évolution des tables reposent donc sur Hibernate `ddl-auto=update`, pas sur des migrations métier déjà écrites.

### 3. Démarrer en local

Windows / PowerShell :

```powershell
.\mvnw.cmd spring-boot:run
```

Linux / macOS :

```bash
./mvnw spring-boot:run
```

Base URL : `http://localhost:8080/atlasrh`.

### 4. Démarrer avec Docker Compose

Le fichier [compose.yaml](compose.yaml) définit PostgreSQL et l'application. L'application attend que le contrôle de santé de PostgreSQL réussisse et utilise le nom réseau `postgres` pour joindre la base.

```bash
docker compose up --build
docker compose logs -f app
```

Arrêter les services :

```bash
docker compose down
```

Le volume `postgres_data` conserve la base après cet arrêt. Le démarrage automatique de Compose par Spring Boot est désactivé : ces commandes sont à exécuter explicitement.

**Configuration du mot de passe :** le service PostgreSQL contient actuellement une valeur fixe, tandis que l'application utilise `${POSTGRES_PASSWORD:-123456}`. Si vous personnalisez le mot de passe, alignez les deux services ; définir seulement la variable externe ne modifie pas le mot de passe du service PostgreSQL. Un volume déjà initialisé conserve ses identifiants.

Le [Dockerfile](Dockerfile) compile le WAR avec Maven et Java 17, puis l'exécute dans une image JRE 17. Son étape de packaging utilise `-DskipTests`.

### 5. Construire un WAR

```powershell
.\mvnw.cmd clean package
java -jar target/ArlasRH-0.0.1-SNAPSHOT.war
```

Sur Linux/macOS, remplacer `.\mvnw.cmd` par `./mvnw`. La construction normale lance les tests, dont le chargement du contexte nécessite une base accessible.

### 6. Swagger / OpenAPI

Après démarrage réussi :

- [Swagger UI](http://localhost:8080/atlasrh/swagger-ui/index.html)
- [Document OpenAPI JSON](http://localhost:8080/atlasrh/v3/api-docs)

Les exemples ci-dessous décrivent le code actuel. Certaines annotations Swagger ne reflètent pas encore tous les formats réels, notamment l'ancienneté.

## Architecture et données

Une requête suit le chemin : contrôleur REST → service métier → repository JPA → PostgreSQL. Les mappers convertissent les entités en DTO et inversement.

| Dossier | Responsabilité |
|---|---|
| [controllers](src/main/java/ma/youcode/ArlasRH/controllers/) | Routes HTTP, entrées et réponses |
| [services](src/main/java/ma/youcode/ArlasRH/services/) | Contrats des services |
| [services/impls](src/main/java/ma/youcode/ArlasRH/services/impls/) | Calculs, contrôles et orchestration |
| [daos](src/main/java/ma/youcode/ArlasRH/daos/) | Repositories Spring Data JPA |
| [models](src/main/java/ma/youcode/ArlasRH/models/) | Entités et énumérations |
| [dtos](src/main/java/ma/youcode/ArlasRH/dtos/) | Objets d'entrée et de sortie |
| [mappers](src/main/java/ma/youcode/ArlasRH/mappers/) | Conversions DTO / entité |
| [exceptions](src/main/java/ma/youcode/ArlasRH/exceptions/) | Exceptions métier et gestion HTTP |

### Modèle métier

| Entité | Données principales | Relations |
|---|---|---|
| Department | Nom, description | Plusieurs employés |
| Employee | Matricule, prénom, nom, email, poste, date d'embauche, rôle | Département, manager, contrats et demandes |
| Contract | Type, dates, salaire brut, état actif | Un employé |
| LeaveType | Nom, `daysPerYear` | Plusieurs demandes |
| LeaveRequest | Dates, `workingDay`, motif, statut, commentaire et date de décision | Un employé et un type de congé |

Types de contrat : `CDI`, `CDD`, `STAGE`.

Statuts de demande : `EN_ATTENTE`, `APPROUVEE`, `REFUSEE`, `ANNULEE`.

## API REST

Tous les chemins ci-dessous sont relatifs à `http://localhost:8080/atlasrh`. Les identifiants sont ceux de la base : `idD` = département, `idE` = employé, `idC` = contrat.

### Départements

| HTTP | Chemin | Action |
|---|---|---|
| GET | `/api/v1/departments` | Lister |
| GET | `/api/v1/departments/{id}` | Consulter |
| POST | `/api/v1/departments` | Créer |
| PATCH | `/api/v1/departments/{id}` | Modifier nom et description |
| DELETE | `/api/v1/departments/{id}` | Supprimer si aucun employé n'y est rattaché |

### Employés

Préfixe : `/api/v1/departments/{idD}/employes`.

| HTTP | Suffixe | Action |
|---|---|---|
| GET | Aucun | Liste paginée |
| POST | Aucun | Créer |
| GET | `/{idE}` | Consulter |
| PATCH | `/{idE}` | Modifier poste et rôle |
| DELETE | `/{idE}` | Supprimer |
| GET | `/{idE}/solde` | Solde restant par type |
| GET | `/{idE}/ancien` | Ancienneté |

Paramètres de la liste :

| Paramètre | Défaut | Description |
|---|---|---|
| `page` | `0` | Numéro de page à partir de zéro |
| `size` | `10` | Taille de page |
| `sortBy` | `id` | Champ de tri |
| `direction` | `asc` | `asc` ou `desc` |
| `nom` | Absent | Recherche partielle sur le **prénom**, sans tenir compte de la casse |

Exemple : `GET /api/v1/departments/1/employes?page=0&size=10&sortBy=firstName&direction=asc&nom=sara`.

La réponse est une page Spring Data, pas une simple liste.

### Contrats

Préfixe : `/api/v1/employes/{idE}/contracts`.

| HTTP | Suffixe | Action |
|---|---|---|
| GET | Aucun | Historique des contrats |
| POST | Aucun | Créer |
| GET | `/{idC}` | Consulter |
| PATCH | `/{idC}` | Terminer aujourd'hui, sans corps de requête |

### Managers

Préfixe : `/api/v1/managers/{managerId}`.

| HTTP | Suffixe | Action |
|---|---|---|
| GET | `/team` | Consulter l'équipe |
| POST | `/employees` | Affecter une liste de matricules |
| GET | `/demands` | Demandes en attente de l'équipe |
| PATCH | `/demands/{leaveRequestId}/approve` | Approuver avec commentaire |
| PATCH | `/demands/{leaveRequestId}/reject` | Refuser, sans corps de requête |

### Demandes de congé

Respecter la majuscule dans `Employees`.

| HTTP | Chemin | Action |
|---|---|---|
| POST | `/Employees/{idE}/demandes` | Créer une demande |
| GET | `/Employees/{idE}/demandes` | Historique de l'employé |
| PATCH | `/Employees/{idE}/demandes/{idLeave}/cancel` | Annuler |

### Types de congé

Le contrôleur n'a actuellement **aucun préfixe de classe** : ses opérations sont à la racine du contexte, et non sous `/api/v1/leave-types`.

| HTTP | Chemin relatif | Action |
|---|---|---|
| GET | Racine du contexte | Lister |
| POST | Racine du contexte | Créer |
| GET | `/{id}` | Consulter |
| DELETE | `/{id}` | Supprimer |

Utiliser le chemin exact exposé par Swagger pour les opérations à la racine, selon la gestion du slash final.

## Exemples de requêtes

Envoyer les corps avec `Content-Type: application/json`. Les identifiants sont illustratifs : les remplacer par ceux de votre base.

### Créer un département

`POST /api/v1/departments`

```json
{
  "name": "Informatique",
  "description": "Développement et maintenance des applications"
}
```

### Créer un employé

`POST /api/v1/departments/1/employes`

```json
{
  "matricule": "EMP001",
  "firstName": "Sara",
  "lastName": "Amrani",
  "email": "sara.amrani@example.com",
  "position": "Développeuse Java",
  "dateDeRejoindre": "2024-03-15",
  "role": "EMPLOYE"
}
```

Le matricule et l'email doivent être uniques. La date d'embauche ne doit pas être future. Pour créer un manager, utiliser `MANAGER` avec un matricule et un email distincts.

Modification via `PATCH /api/v1/departments/1/employes/2` :

```json
{
  "position": "Développeuse senior",
  "role": "EMPLOYE"
}
```

### Créer un contrat

`POST /api/v1/employes/2/contracts`

```json
{
  "type": "CDI",
  "startDate": "2024-03-15",
  "endDate": null,
  "grossSalary": 12000
}
```

Le salaire brut doit être positif. La terminaison renseigne la date du jour et passe `active` à `false`.

### Définir un type de congé

Corps de création à la racine du contexte :

```json
{
  "name": "Congé annuel",
  "daysPerYear": 18
}
```

La validation de `daysPerYear` présente actuellement une anomalie décrite dans les limites ci-dessous.

### Affecter des employés à un manager

`POST /api/v1/managers/3/employees`

```json
["EMP001", "EMP002"]
```

Le corps contient des **matricules**, pas des identifiants numériques. Tous les employés doivent exister et avoir le rôle `EMPLOYE`.

### Créer puis approuver une demande

`POST /Employees/2/demandes`

```json
{
  "startDate": "2026-11-02",
  "endDate": "2026-11-04",
  "reason": "Congé personnel",
  "leaveTypeId": 1
}
```

Adapter les dates au jour du test. Le nombre de jours est calculé par le service ; ne pas envoyer `workingDay`.

`PATCH /api/v1/managers/3/demands/5/approve`

```json
{
  "managerComment": "Demande validée"
}
```

Parcours type : créer un département → créer manager et employé → affecter l'employé → disposer d'un type de congé → créer une demande → consulter les demandes en attente → approuver ou refuser → consulter le solde.

## Règles métier

### Départements et équipes

- La création d'un département refuse un nom déjà existant.
- La suppression d'un département contenant des employés est refusée.
- L'affectation vérifie le rôle du manager et les matricules des employés.
- L'approbation et le refus vérifient que la demande appartient à l'équipe du manager.

### Demandes de congé

- Une nouvelle demande reçoit le statut `EN_ATTENTE`.
- Un employé ne peut pas créer une autre demande tant qu'il en possède une en attente.
- Le nombre de jours enregistré est `dateFin - dateDébut + 1` : ce sont des **jours calendaires**, malgré le nom `workingDay`. Les weekends et jours fériés ne sont pas exclus.
- L'approbation exige une demande en attente et enregistre le commentaire et la date de décision.
- L'annulation refuse les demandes déjà approuvées, refusées ou annulées.
- Le refus affecte le statut `REFUSEE` ; il ne vérifie pas actuellement que la demande est encore en attente.

### Solde par type

`GET /api/v1/departments/{idD}/employes/{idE}/solde`

Le service vérifie l'appartenance de l'employé au département, puis parcourt tous les types de congé :

1. Sélectionner les demandes de cet employé avec le statut `APPROUVEE`.
2. Retenir le même type de congé.
3. Retenir une date de début comprise entre aujourd'hui moins un an et aujourd'hui, bornes incluses.
4. Additionner les valeurs `workingDay`.
5. Calculer `max(0, daysPerYear - somme)`.

Exemple : `18 - (3 + 8 + 3) = 4 jours restants`.

Réponse illustrative :

```json
[
  "Congé annuel: 4 jours restants",
  "Congé maladie: 10 jours restants"
]
```

Il s'agit d'une **fenêtre glissante d'un an**, pas de l'année civile. Les demandes approuvées dont le début est futur ne sont pas déduites. Le calcul prend la durée totale d'une demande sélectionnée, sans découper les journées aux limites de la période. Un type sans demande éligible conserve son quota entier. Aucun report ni prorata d'acquisition n'est calculé.

### Ancienneté

`GET /api/v1/departments/{idD}/employes/{idE}/ancien`

Calcul avec `Period.between(dateDeRejoindre, LocalDate.now())`.

```json
{
  "years": 2,
  "months": 5,
  "days": 12
}
```

Les mois et jours sont les composantes restantes après les années complètes, pas des totaux. Les valeurs sont numériques et l'ordre des clés du `HashMap` n'est pas garanti. Cet endpoint retourne une ancienneté, pas un quota supplémentaire de congés.

## Erreurs et validation

Le [gestionnaire global](src/main/java/ma/youcode/ArlasRH/exceptions/GlobalExceptionHandler.java) traduit plusieurs exceptions métier en réponses JSON :

```json
{
  "message": "Employee not found."
}
```

| Statut | Exemples |
|---|---|
| 200 | Lecture ou modification réussie |
| 201 | Création réussie |
| 204 | Suppression réussie, sans contenu |
| 400 | Certaines erreurs métier ou de validation |
| 404 | Employé, département, manager, contrat, type ou demande introuvable |
| 409 | Doublon, contrat déjà terminé, demande déjà en attente ou annulation interdite |

Les erreurs ne sont pas encore uniformes : la suppression de département intercepte toute exception et retourne `404` avec un texte ; certaines méthodes de contrat lèvent une `RuntimeException` générique.

Les DTO des employés et contrats sont validés via `@Valid`. Pour un employé : matricule de 3 à 20 caractères, prénom et nom de 2 à 50 caractères, email valide, date d'embauche passée ou présente et rôle obligatoire.

## Tests

Un test de chargement du contexte Spring est présent dans [ArlasRhApplicationTests.java](src/test/java/ma/youcode/ArlasRH/ArlasRhApplicationTests.java).

```powershell
.\mvnw.cmd test
```

Il utilise la configuration de l'application et nécessite une base accessible. Il ne constitue pas une couverture des calculs de solde, de l'ancienneté ou des transitions de statut. Les commandes de lancement et les endpoints n'ont pas été exécutés dans le cadre de cette mise à jour documentaire.

## Limites actuelles

Cette section distingue l'implémentation existante des fonctionnalités à compléter :

- Le type de congé utilise `@NotBlank` sur un `Integer daysPerYear`, une contrainte inadaptée pouvant faire échouer la validation de création.
- Les contrôleurs de demande et d'approbation n'activent pas `@Valid` sur leurs corps, même si les DTO déclarent des contraintes. L'ordre des dates de congé n'est pas explicitement vérifié dans le service.
- La consultation et la terminaison d'un contrat ne vérifient pas encore son appartenance à l'employé de l'URL. L'annulation d'une demande et la consultation d'ancienneté présentent aussi des contrôles d'appartenance incomplets.
- Aucune vérification du quota restant ni des chevauchements n'est appliquée avant l'approbation.
- Certaines méthodes de service restent des squelettes : `terminateContract`, `updateLeaveType`, `getLeaveRequestsByEmployee` avec statut et `getLeaveRequestById`. Les endpoints documentés utilisent les autres méthodes implémentées.
- Les annotations Swagger de l'ancienneté décrivent encore une liste de chaînes alors que la réponse réelle est un objet avec `years`, `months` et `days`.
- Les migrations métier Liquibase restent à écrire. Les versions de dépendances indiquées sont celles du dépôt ; leur compatibilité doit être vérifiée lors d'une construction et d'un démarrage.
