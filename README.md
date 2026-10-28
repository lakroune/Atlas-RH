# Atlas-RH

Application de **gestion des ressources humaines (RH)** développée avec **Spring Boot**.

- **Packaging** : WAR (déployable sur un serveur Tomcat externe)
- **Base de données** : PostgreSQL
- **ORM / Migration** : Spring Data JPA + Liquibase
- **Java** : 17
- **Spring Boot** : 4.1.1

---

## 1. Prérequis

- **JDK 17** ou plus récent
- **Maven 3.8+**
- **PostgreSQL 14+** (ou une instance distante accessible)

---

## 2. Lancement du projet

### 2.1. Préparation de la base de données

1. Créer la base de données PostgreSQL :

   ```bash
   createdb -U postgres atlas_db
   ```

2. (Optionnel) Si Liquibase est activé, il générera les tables à partir du fichier
   `src/main/resources/db/changelog/db.changelog-master.yaml` à chaque démarrage.

### 2.2. Configuration

Le fichier de configuration est `src/main/resources/application.properties` :

| Paramètre | Valeur par défaut |
|---|---|
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/atlas_db` |
| `spring.datasource.username` | `postgres` |
| `spring.datasource.password` | `123456` |
| `spring.jpa.hibernate.ddl-auto` | `update` |
| `spring.liquibase.enabled` | `true` |
| `server.servlet.context-path` | `/atlasrh` |

> Modifiez ces valeurs selon ton environnement local.

### 2.3. Compiler et lancer en mode développement

L'application démarre alors sur :

```
http://localhost:8080/atlasrh
```

### 2.4. Compiler et lancer avec le package WAR

```bash
mvn clean package
```

Le fichier `target/ArlasRH-0.0.1-SNAPSHOT.war` est alors généré.
Déployez-le sur un serveur Tomcat (ou tout conteneur Servlet) et accédez à :

```
http://localhost:8080/atlasrh
```

### 2.5. Lancer avec Docker Compose

Docker Compose construit l'image de l'application et démarre PostgreSQL. À la racine du projet :

```bash
docker compose up --build
```

L'API est ensuite accessible sur `http://localhost:8080/atlasrh`. Pour arrêter les conteneurs :

```bash
docker compose down
```

Les données PostgreSQL restent conservées dans le volume `postgres_data`. Pour remplacer le mot de
passe local par défaut (`123456`), définissez la variable d'environnement `POSTGRES_PASSWORD`
avant de lancer Compose.



## 3. Architecture en couches

Le projet suit une architecture **en couches** (layered architecture) purement **backend**, avec séparation stricte de chaque responsabilité.

```
src/main/java/ma/youcode/ArlasRH/
├── ArlasRhApplication.java          # Point d'entrée de l'application
├── ServletInitializer.java          # Configuration de déploiement WAR
│
├── controllers/                     # Côté présentation (API REST)
│   ├── ContractController.java
│   ├── DepartmentController.java
│   ├── EmployeeController.java
│   ├── LeaveRequestController.java
│   └── LeaveTypeController.java
│
├── daos/                            # Accès aux données (repositories)
│   ├── ContractRepository.java
│   ├── DepartmentRepository.java
│   ├── EmployeeRepository.java
│   ├── LeaveRequestRepository.java
│   └── LeaveTypeRepository.java
│
├── dtos/                            # Données transférées (requêtes/réponses)
│   ├── ContractCreateRequest.java
│   ├── ContractResponse.java
│   ├── DepartmentRequest.java
│   ├── DepartmentResponse.java
│   ├── EmployeeCreateRequest.java
│   ├── EmployeeResponse.java
│   └── EmployeeUpdateRequest.java
│
├── exceptions/                      # Gestion des erreurs
│   ├── ContractAlreadyTerminatedException.java
│   ├── ContractNotFoundException.java
│   ├── DepartmentAlreadyExistsException.java
│   ├── DepartmentDeleteException.java
│   ├── DepartmentNotFoundException.java
│   ├── EmployeeAlreadyExistsException.java
│   ├── EmployeeAlreadyHasActiveContractException.java
│   ├── EmployeeNotFoundException.java
│   └── GlobalExceptionHandler.java
│
├── mappers/                         # Conversion DTO ↔ Entity
│   ├── ContractMapper.java
│   ├── DepartmentMapper.java
│   ├── EmployeeMapper.java
│   └── EmployeeUpdateMapper.java
│
├── models/                          # Modèle métier (entités JPA)
│   ├── Contract.java
│   ├── Department.java
│   ├── Employee.java
│   ├── LeaveRequest.java
│   ├── LeaveType.java
│   └── enums/
│       ├── ContratType.java
│       ├── LeaveStatus.java
│       └── Role.java
│
├── services/                        # Règles métier (interfaces)
│   ├── ContractService.java
│   ├── DepartmentService.java
│   ├── EmployeeService.java
│   ├── LeaveRequestService.java
│   └── LeaveTypeService.java
│
└── services/impls/                  # Implémentation des règles métier
    ├── ContractServiceImpl.java
    ├── DepartmentServiceImpl.java
    ├── EmployeeServiceImpl.java
    ├── LeaveRequestServiceImpl.java
    └── LeaveTypeServiceImpl.java
```

### Rôle de chaque couche

| Couches | Fichiers | Rôle |
|---|---|---|
| **Controllers** | `controllers/*Controller.java` | Reçoivent les requêtes HTTP, les valident, appellent le service et renvoient la réponse. |
| **Services** | `services/*Service.java` + `services/impls/*ServiceImpl.java` | Métier. Contiennent la logique de règle, les vérifications de cohérence (ex: matricule unique, ... des employés). |
| **Repositories (DAOs)** | `daos/*Repository.java` | Accès aux données via Spring Data JPA (`JpaRepository`). |
| **Models** | `models/*Entity.java` | Entités JPA mappées sur les tables de la base. |
| **DTOs** | `dtos/*` | Objets transmis en entrée/sortie de l'API. Séparent la base de données du client. |
| **Mappers** | `mappers/*Mapper.java` | Convertissent les DTOs en entités et inversement. |
| **Exceptions** | `exceptions/*Exception.java` + `GlobalExceptionHandler.java` | Gestion centralisée des erreurs HTTP. |
| **Config** | `resources/application.properties` | Configuration de l'application (base de données, JPA, Liquibase, serveur). |
| **Migrations** | `resources/db/changelog/*` | Migration de la base de données avec Liquibase. |
| **Ressources** | `resources/static`, `resources/templates` | Ressources statiques et vues (Thymeleaf). |
