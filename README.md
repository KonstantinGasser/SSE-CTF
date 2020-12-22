# SSE-CTF
SSE CTF Project: Notenverwaltungssystem

# Postgres (DB) with Docker
## Building Postgres Docker image
```docker build -t sse_postgres .```
!Wichtig! dieses Command muss im Directory, in dem das `Dockerfile` liegt ausgeführt werden! <br>
```docker run --publish 5432:5432 --name upgrade_database sse_postgres```
- --name upgrade_database -> Name, den wir dem Container gebene, sonst container hash..
- sse_postgres -> Das ist die Referenze zu dem Image das wir zuvor gebaut haben. Das haben wir mit `-t sse_postgres` dem Namen `sse_postgres` gegeben.

```docker exec -it upgrade_database bash``` -> zugreifen auf den Shell des Containers
```psql -U db_user_name -d db_name``` -> damit kommt man in die postgres prompt und kann dort SQL-Commands direkt auf der Datenbank ausgeführt
