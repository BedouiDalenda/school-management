 
# School Management - Docker Guide

## Structure des fichiers

school-management/
├── backend/
│   └── Dockerfile
├── frontend/
│   ├── Dockerfile
│   └── nginx.conf
└── docker-compose.yml


## Démarrage


# Lancer tous les services
docker-compose up --build

# En arrière-plan
docker-compose up -d --build

## Accès

- Frontend : http://localhost
- Backend : http://localhost:8080
- Swagger : http://localhost:8080/swagger-ui.html
- Database : localhost:5432

## Commandes utiles


# Voir les logs
docker-compose logs -f

# Arrêter
docker-compose down

# Redémarrer un service
docker-compose restart backend

# Nettoyer tout
docker-compose down -v