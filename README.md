# 💸 Pay My Buddy

C'est une application web Java permettant de transférer de l'argent entre amis de manière simple, rapide et sécurisée.

---

## 🚀 Fonctionnalités

- 🔐 Authentification utilisateur
- 👥 Gestion des connexions ("buddies")
- 💸 Transferts d'argent entre utilisateurs
- 📊 Historique des transactions
- 🧾 Solde du compte utilisateur
- 🧪 Couverture de tests avec Surefire & JaCoCo
- 🗄️ Persistance avec MySQL

---


## 🧩 Diagrammes

### 📘 Diagramme de classes (UML)

> Ce diagramme représente les entités principales (`User`, `Transaction`, `Connection`) et leurs relations.

<img width="788" alt="Capture d’écran 2025-05-23 à 11 23 06" src="https://github.com/user-attachments/assets/9eaba930-87a0-4490-9e87-9b228ce0e451" />

---

### 🗃️ Modèle Physique de Données (MPD)

> Le MPD illustre la structure des tables dans la base de données MySQL.

<img width="694" alt="Capture d’écran 2025-05-23 à 11 27 45" src="https://github.com/user-attachments/assets/91de602d-1d51-4602-a7ae-56a4bdcdb396" />


## Architecture du projet
<img width="407" alt="Capture d’écran 2025-05-23 à 11 36 10" src="https://github.com/user-attachments/assets/ae1687d7-1ca6-4c21-92c4-b92bb7349656" />

## ⚙️ Technologies utilisées

- Java 21
- Spring Boot 3
- MySQL
- Spring Security
- Thymeleaf
- Maven
- JaCoCo (tests de couverture)
- Surefire (tests unitaires)

---

## 🛠️ Installation & Lancement

### 1. Prérequis

- Java JDK 21
- Maven
- MySQL Workbench
- IntelliJ Idea

### 2. Configuration

**Clone ce dépôt :**

```bash
git clone https://github.com/Informat21/PayMyBuddy.git
```

**Crée la base de données MySQL :**

```sql
CREATE DATABASE PayMyBuddy;
```

**Vérifie le fichier** `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/PayMyBuddy
spring.datasource.username=root
spring.datasource.password=root
```

**Exécute le projet :**

```bash
mvn spring-boot:run
```

---

## 🧪 Tests

**Lance les tests :**

```bash
mvn test
```

**Génère le rapport JaCoCo :**

```bash
mvn jacoco:report
```
**Génère le rapport Surefire :**

```bash
mvn surefire-report:report
```
**Accède aux rapports :**

- **JaCoCo** : `target/site/jacoco/index.html`  
- **Surefire** : `target/reports/surefire.html`


## 👨‍💻 Auteur

- **Fethi Kharrat**  
- Projet réalisé dans le cadre de ma formation de développeur d'applications Java.

