# Explore with me (graduation project of Yandex  Practicum)

is a backend for an application that allows you to share information about interesting events, and helps you find a company to participate in them.

The application is implemented by using a micro service architecture: the main service contains everything necessary for the product to work, a statistics service that stores the number of views, and allows you to make various selections.

## API specification
The main service API is divided into three parts. The first, is public and available without registration to any network user. The second, is private and available only to authorized users. The third, is administrative for service administrators.

- [Main Service API](https://github.com/Nickadm7/java-explore-with-me/blob/main/ewm-main-service-spec.json)
- [API service for collecting statistics](https://github.com/Nickadm7/java-explore-with-me/blob/main/ewm-stats-service-spec.json)

Use the [Swagger editor](https://editor-next.swagger.io/)  to view the specs.

## Functionality
Registered users can create and edit events, apply for participation in other users events, approve or reject participation in their own event. It is possible to flexibly configure the created event (category, annotation, description, limit of participants, whether payment for participation is required, whether moderation of participation is required, etc.). Events have a status (pending moderation / rejected / published) that affects how users interact with it.

Admins can create, delete, or modify event categories, create, edit, and modify event collections, moderate, and edit events (only unpublished events can be moderated). Moderation can be carried out both separately for each event, and for several events at once.

For all types of users (unauthorized, authorized and administrator) there is the possibility of flexible search for events and event selections. Considering, the user's role, event status, a set of incoming search parameters with pagination (by id, event date, partially contained text, etc.). All search queries are stored in the statistics service.

## Instructions for launching the application
For the application to work correctly, Docker must be installed on the computer. Both services are started with the following command:

```Bash
mvn install
docker-compose up
```

## Technologies which I use
Java 11, Spring Boot, Sprting Data, Hibernate, Querydsl, Lombock, Docker, PostgreSQL database and H2 (for tests).