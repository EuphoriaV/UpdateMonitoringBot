# Tinkoff-java-backend

Cервис по отслеживанию обновлений вопросов со StackOverflow и репозиториев на GitHub. Этот проект написан полностью мной в рамках курса по разработке на Java. Проект представляет собой телеграм-бота, который позволяет отслеживать обновления в репозиториях на GitHub и вопросах на StackOverflow. В случае обновления бот присылает уведомление. Проект написан на Spring Boot и состоит из двух модулей: bot - телеграм-бот, scrapper - модуль, проверяющий наличие обновлений. Модули общаются между собой через REST, но также есть возможность использовать асинхронное общение с помощью RabbitMQ. Также в проекте реализовано три способа взаимодействия с БД: JDBC, jOOQ, JPA. Помимо этого, есть возможность мониторить метрики с помощью Grafana и Prometheus. Сервис доступен по ссылке https://t.me/UpdateMonitoringBot

### Обновление GitHub репозитория 

Сообщение об обновлении репозитория выглядит следующим образом:

![image](https://user-images.githubusercontent.com/78645533/232025190-983ea55d-0bfa-4edd-ae53-56e5d0b33877.png)

### Обновление StackOverflow вопроса 

Сообщение о том, что вопрос закрылся выглядит следующим образом:

![image](https://user-images.githubusercontent.com/78645533/232025416-9b98e8d4-206f-4378-b332-8746d5b5774f.png)

Сообщение о том, что на вопрос было добавлено несколько ответов выглядит следующим образом:

![image](https://user-images.githubusercontent.com/78645533/232025483-cadd8505-6184-48c3-88eb-0d09b2f99676.png)

Сообщение о каком-либо ином обновлении выглядит следующим образом:

![image](https://user-images.githubusercontent.com/78645533/232025615-a74efe0e-62ac-4c49-b4c4-fa4781292dd4.png)
