# Cервис хранения данных, предназначенных для выполнения задач по взаимодействию с социальными сетями.
Данный сервис хранит группы пользователей и публикации со статусом выполнения.

Порт микросервиса: 8686

Адрес в интернете: https://storage.socshared.ml

## Endpoints
1. GET /private/groups/{groupId} - получить группу, подключенную в системе, по ее id
2. GET /private/users/{userId}/groups - получить группы, подключенные в системе, по id пользователя
3. GET /private/users/{userId}/groups/social_network/{socialNetwork}/groups - получить группы пользователя по id пользователя и типу социальной сети
4. GET /private/users/{userId}/groups/facebook/{facebookId} - получить группу по пользовательскому id и id группы в facebook
5. GET /private/users/{userId}/groups/vk/{vkId} - подключить группу по пользовательскому id и id группы в vk
6. POST /private/groups - подключить группу ({userId: <String>, vkId: <String: null>, fbId: <String: null>, name: <String>, socialNetwork: <VK:FB>}),
в случае если параметры не соответствуют типу социальной сети, то выдается сообщение об ошибке (хотя бы один из vkId и fbId не должен быть null и соответстовать типу socialNetwork)
7. DELETE /private/groups/{groupId} - убрать группу из подключенных
8. POST /private/publications - добавление публикации в очередь на обработку
9. GET /private/publications/status/not_publishing - получение неопубликованных постов

