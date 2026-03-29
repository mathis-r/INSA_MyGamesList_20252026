# Projet Programmation Mobile - INSA 3INFO (2025/2026)
# Mathis ROQUAIN & Mathys LE PROVOST

---
## Projet

Tous les TP de 1 à 6 ont été réalisé et fonctionnent.

Nous avions commencé le TP 7,
mais par manque de temps _(entre autre à cause des partiels)_ nous n'avons pas pu en faire une
version fonctionnelle. Notre avancée se résumait aux commandes cURL qui correspondent aux requêtes
vers l'API, ainsi qu'à un début d'implémentation en Kotlin avec Retrofit.

Exemple de commande cURL permettant de récupérer un fichier json équivalent à "games.json" :

`curl -X POST "https://api.igdb.com/v4/games" -H "Client-id: `_[client-id]_`" -H "Authorization: Bearer `_[client-secret]_`" -d "fields id, cover, first_release_date, genres, name, platforms, summary, total_rating; where first_release_date > 0 & total_rating > 0;"`

---
## Bonus

Nous n'avons pas vraiment eu le temps d'implémenter les bonus non plus.
On a juste changé l'icone de l'application pour ne pas avoir celle de base.

---
## Erreurs restantes

Il reste 3 _warnings_ sur le projet : 
ce sont des _warnings_ de convention de nommage des variables : nous avons suivi les noms utilisés dans les
fichiers json, avec des _underscore_ comme séparateur (_i.e._ "total_rating" au lieu de "totalRating").
Nous n'avons pas réussi à refactoriser le code pour corriger ce problème.
