# Sentiment Analysis App

Spring Boot приложение с mock-анализом тональности и интеграцией с Kubernetes.

## Возможности

- REST-эндпоинт /api/sentiment?text=...

- mock-анализ тональности (positive/negative/neutral)

- контейнеризация через Docker

- развёртывание в Minikube

- Ingress по доменному имени

- горизонтальное автоскейлинг (HPA)

- метрики через Actuator + Prometheus

- дашборд в Grafana

## Стек

- Java 17 (Spring Boot)

- Docker

- Kubernetes / Minikube

- Prometheus + Grafana

- Helm

- Micrometer + Actuator

## Сборка и запуск
```
mvn clean package -DskipTests
docker build -t sentiment-app:1.0 .
docker run -p 8080:8080 sentiment-app:1.0
```


## Метрики

Приложение отдаёт метрики по адресу:

/actuator/prometheus

## Мониторинг

Установить стек:
```
helm install monitoring prometheus-community/kube-prometheus-stack -n monitoring --create-namespace
```

Grafana доступна через port-forward:
```
kubectl port-forward svc/monitoring-grafana 3000:80 -n monitoring
```
