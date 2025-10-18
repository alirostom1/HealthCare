# MedExpert – Système de Télé-Expertise Médicale

## Description du projet

**MedExpert** est une application web Java destinée aux établissements de santé pour optimiser le parcours patient en facilitant la coordination entre médecins généralistes et spécialistes. Ce système permet une prise en charge efficace et rapide grâce à la télé-expertise médicale.

## Objectifs

- Centraliser la gestion des patients et leurs données médicales
- Faciliter la collaboration entre médecins généralistes et spécialistes
- Optimiser le parcours de soins des patients
- Gérer efficacement les files d'attente et les créneaux de consultation
- Assurer une traçabilité complète des consultations et expertises

## Technologies utilisées

- **Backend**: Java, Jakarta EE, Maven
- **Frontend**: JSP, JSTL, Servlets
- **Base de données**: JPA/Hibernate
- **Sécurité**: Bcrypt, CSRF Protection, Authentication Stateful (Sessions)
- **Serveur**: Tomcat
- **Tests**: JUnit, Mockito
- **API**: Stream API, Lambda Expressions

## Architecture du projet

- `src/main/java/io/github/alirostom1/servlets/` - Servlets
- `src/main/java/io/github/alirostom1/service/` - Logique métier
- `src/main/java/io/github/alirostom1/repository/` - Couche d'accès aux données
- `src/main/java/io/github/alirostom1/model/` - Entités JPA
- `src/main/java/io/github/alirostom1/config/` - Gestion de la sécurité
- `src/main/webapp/WEB-INF/pa/` ges- Pages JSP
- `src/test/java/` - Tests unitaires et d'intégration

## Fonctionnalités principales

### Module Authentification
- Login/Logout sécurisé
- 3 rôles : Généraliste, Spécialiste et Infirmier
- Sessions stateful avec protection CSRF

### Module Infirmier
- **US1**: Accueil et enregistrement des patients
    - Recherche de patient existant ou création nouveau patient
    - Saisie des données administratives et médicales
    - Mesure des signes vitaux (tension, fréquence cardiaque, température, etc.)
    - Ajout automatique à la file d'attente

- **US2**: Consultation de la liste des patients enregistrés
    - Affichage trié par heure d'arrivée
    - Filtrage par date d'enregistrement avec Stream API

### Module Médecin Généraliste
- **US1**: Création de consultation
    - Sélection du patient
    - Saisie du motif et observations
    - Coût fixe de 150 DH

- **US3**: Demande d'expertise spécialisée
    - Sélection de la spécialité requise
    - Filtrage des spécialistes disponibles par spécialité et tarif (Stream API)
    - Consultation des créneaux disponibles
    - Envoi de demande avec question médicale

- **US4**: Calcul du coût total
    - Consultation + Expertise + Actes techniques
    - Calcul avec expressions lambda (map().sum())

### Module Médecin Spécialiste
- **US5**: Configuration du profil
    - Définition du tarif de consultation
    - Spécialisation médicale
    - Durée de consultation (30 minutes par défaut)

- **US6**: Gestion des créneaux
    - Créneaux fixes prédéfinis de 30 minutes
    - Mise à jour automatique de la disponibilité
    - Gestion des réservations et annulations

- **US7**: Consultation des demandes d'expertise
    - Liste des demandes reçues
    - Filtrage par statut et priorité (Stream API)
    - Accès aux détails patients et questions

- **US8**: Réponse aux expertises
    - Saisie de l'avis médical
    - Recommandations de traitement
    - Clôture de la demande

## Processus métier

### 1. Arrivée et enregistrement du patient
- Saisie des données administratives et médicales
- Mesure des signes vitaux
- Intégration automatique dans la file d'attente

### 2. Consultation avec le médecin généraliste
- Accès au dossier patient complet
- Examen clinique et analyse des symptômes
- Décision de prise en charge

### 3. Deux scénarios possibles
- **Scénario A**: Prise en charge directe (diagnostic et prescription)
- **Scénario B**: Demande de télé-expertise spécialisée

### 4. Télé-expertise
- **Mode synchrone**: Visioconférence ou téléphone en temps réel
- **Mode asynchrone**: Transmission de dossier avec réponse sous 24-48h

## Actes techniques médicaux supportés

- Radiographie
- Échographie
- IRM
- Électrocardiogramme
- Actes dermatologiques (Laser)
- Fond d'œil
- Analyses de sang
- Analyses d'urine

## Prérequis

- JDK 11+
- Apache Maven 3.6+
- Serveur d'application (Tomcat 9+)
- Base de données compatible JPA
- Git

## Class Diagram

![class diagram](https://github.com/alirostom1/HealthCare/blob/main/docs/HealthCare.png)

## Installation 

1. Cloner le repository
```bash
git clone https://github.com/alirostom1/HealthCare
cd healthcare