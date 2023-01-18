# Приложение Explore With Me

link on pull request:
https://github.com/BolonkinAleksandr/java-explore-with-me/pull/2#issue-1534025450

## Используемые технологии
Java, Spring Boot, Lombok, Hibernate, PostgreSQL, Maven, Docker

## Описание приложения
Это приложение — афиша, где можно предложить какое-либо событие от выставки до похода в кино и набрать компанию для участия в нём. Состоит из двух сервисов: основного и сервиса статистики.

### Основной сервис
содержит возможности для просмотра и добавления мероприятий; подачи, подтверждения или отклонения заявок на участие в мероприятиях; инструменты для администрирования сервиса. Спецификация API

### Сервис статистики
обеспечивает возможность хранения и получения статистики просмотров, позволяет делать различные выборки для анализа работы приложения. Спецификация API

## Запуск приложения
Для запуска приложения используйте следующие команды:
1) mvn clean package
2) docker-compose up