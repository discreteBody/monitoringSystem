# Distributed Observability & Monitoring Suite

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Prometheus](https://img.shields.io/badge/Prometheus-TSDB-e6522c.svg)](https://prometheus.io/)
[![Grafana](https://img.shields.io/badge/Grafana-Dashboards-f46800.svg)](https://grafana.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://www.docker.com/)

Production-oriented observability stack for Spring Boot microservices using Micrometer, Prometheus, Grafana, and Alertmanager. Collect JVM and business metrics, visualize SLO/SLA signals, and trigger proactive alerts to reduce Mean Time to Detection (MTTD).

---

## Overview

This project provides a decoupled observability pipeline:

- **Instrumentation**: JVM + application/business metrics exported via **Micrometer**
- **Collection & storage**: **Prometheus** time-series database (TSDB)
- **Visualization**: **Grafana** dashboards for service health and latency
- **Alerting**: **Alertmanager** routing with grouping/deduplication to reduce alert noise

The architecture separates metric generation, storage, and alerting to improve resilience and simplify scaling.

---

## Key Features

- **Instrumented metric collection** using Micrometer with Prometheus scraping for JVM and business metrics.
- **High-fidelity dashboards** in Grafana for CPU/Memory, request rates, error rates, and tail latency (P95/P99).
- **Proactive alerting pipeline** with Prometheus rules + Alertmanager routing for automated notifications.
- **Latency profiling** via Prometheus histogram quantiles to monitor tail behavior and worst-case user experience.

---

## Architecture

![Architecture Diagram](https://github.com/user-attachments/assets/ee81065c-87a3-44db-8d09-b2cfbdc89302)

---

## Getting Started

### Prerequisites

- Java 21
- Docker + Docker Compose
- Maven (or Gradle)

### Run the application

```bash
./mvnw spring-boot:run
```

### Start the observability stack

```bash
docker-compose up -d
```

> If your repository uses `docker compose` (newer CLI), you can also run:
>
> ```bash
> docker compose up -d
> ```

---

## Access the Services

- **Target app**: http://localhost:8080  
  Trigger metrics generation (example): `GET /api/order`

- **Prometheus**: http://localhost:9090
- **Alertmanager**: http://localhost:9093
- **Grafana**: http://localhost:3000  
  Default login: `admin / admin`

---

## Core PromQL Queries

### P99 request latency (aggregated)

```promql
histogram_quantile(0.99, sum(rate(http_server_requests_seconds_bucket[5m])) by (le))
```

### JVM heap memory used

```promql
sum(jvm_memory_used_bytes{area="heap"})
```

### Business transaction rate (orders/sec)

```promql
rate(business_orders_total[1m])
```

---

## Alerting Configuration

Prometheus evaluates alert rules (for example **HighLatency** and **InstanceDown**) and forwards firing alerts to Alertmanager.

Alertmanager is configured to:

- **Group and deduplicate** alerts (e.g., `group_by: ['alertname']`) to prevent webhook spam during noisy events
- **Route notifications** to a configured Discord/Slack webhook (or any supported receiver)

---

## Notes / Customization

- Update scrape targets in `prometheus.yml` to match your service names and ports.
- Import Grafana dashboards and set the Prometheus datasource to your Prometheus endpoint.
- Adjust alert thresholds (latency, error rate, resource saturation) to match your SLOs.

---
