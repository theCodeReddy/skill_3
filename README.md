# Experiment 3 – Working with HQL: Sorting, Pagination & Aggregates

## Overview
Demonstrates advanced **Hibernate Query Language (HQL)** operations including sorting, pagination, aggregate functions (COUNT, MIN, MAX, AVG, SUM), GROUP BY, WHERE, and LIKE pattern matching.

---

## Project Structure
```
src/main/java/com/experiment/hql/
├── HqlApplication.java               # Entry point + Task 2 (seed 20 products)
├── entity/
│   └── Product.java                  # Task 1 – Product entity from Experiment 2
├── dto/
│   └── CategoryStats.java            # DTO for GROUP BY aggregate results
├── repository/
│   └── ProductRepository.java        # All @Query HQL queries (Tasks 3–8)
├── service/
│   └── ProductService.java           # Pagination via EntityManager
└── controller/
    └── ProductController.java        # REST endpoints
```

---

## HQL vs SQL Quick Reference

| Concept | SQL | HQL |
|---|---|---|
| Table | `FROM products` | `FROM Product p` |
| Column | `ORDER BY price` | `ORDER BY p.price` |
| Wildcard | `LIKE '%word%'` | `LIKE '%:keyword%'` |
| Param | `WHERE price > 100` | `WHERE p.price > :price` |
| Count | `SELECT COUNT(*) FROM products` | `SELECT COUNT(p) FROM Product p` |
| Group | `GROUP BY category` | `GROUP BY p.category` |

---

## Run
```bash
mvn spring-boot:run
# Server:     http://localhost:8080
# H2 Console: http://localhost:8080/h2-console  (JDBC URL: jdbc:h2:mem:hqldb)
```

---

## Postman Test Guide

### Task 3 – Sort by Price
```
GET http://localhost:8080/products/sort/price?dir=asc
GET http://localhost:8080/products/sort/price?dir=desc
```
HQL: `SELECT p FROM Product p ORDER BY p.price ASC|DESC`

---

### Task 4 – Sort by Quantity
```
GET http://localhost:8080/products/sort/quantity?dir=asc
GET http://localhost:8080/products/sort/quantity?dir=desc
GET http://localhost:8080/products/sort/category-price
```
HQL: `SELECT p FROM Product p ORDER BY p.quantity ASC`

---

### Task 5 – Pagination
```
GET http://localhost:8080/products/page/first     ← records 1–3
GET http://localhost:8080/products/page/second    ← records 4–6
GET http://localhost:8080/products/page?page=1&size=3
GET http://localhost:8080/products/page?page=2&size=5
```
Java: `query.setFirstResult(0); query.setMaxResults(3);`

---

### Task 6 – Aggregate Functions
```
GET http://localhost:8080/products/aggregates          ← COUNT, MIN, MAX, AVG, SUM
GET http://localhost:8080/products/aggregates/min      ← MIN + cheapest product
GET http://localhost:8080/products/aggregates/max      ← MAX + most expensive
GET http://localhost:8080/products/aggregates/count    ← COUNT total + by category
```

HQL examples:
```
SELECT COUNT(p) FROM Product p
SELECT MIN(p.price) FROM Product p
SELECT MAX(p.price) FROM Product p
SELECT AVG(p.price) FROM Product p
SELECT SUM(p.quantity) FROM Product p
```

---

### Task 7 – GROUP BY and WHERE
```
GET http://localhost:8080/products/stats/category
GET http://localhost:8080/products/filter/price?min=20&max=100
GET http://localhost:8080/products/filter/active?minPrice=50
GET http://localhost:8080/products/filter/category?name=Electronics&minQty=10
```

HQL examples:
```
SELECT new CategoryStats(p.category, COUNT(p), MIN(p.price), MAX(p.price), AVG(p.price), SUM(p.quantity))
FROM Product p GROUP BY p.category ORDER BY p.category

SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max ORDER BY p.price
SELECT p FROM Product p WHERE p.active = true AND p.price > :price
```

---

### Task 8 – LIKE Pattern Matching
```
GET http://localhost:8080/products/search?keyword=pro
GET http://localhost:8080/products/search/name?keyword=laptop
GET http://localhost:8080/products/search/starts?prefix=Smart
GET http://localhost:8080/products/search/ends?suffix=Pro
GET http://localhost:8080/products/search/category?keyword=tech
```

HQL examples:
```
WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))   ← contains
WHERE LOWER(p.name) LIKE LOWER(CONCAT(:prefix, '%'))         ← starts with
WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :suffix))         ← ends with
```

---

## Sample Response – GET /products/aggregates
```json
{
  "results": {
    "totalProducts": 20,
    "activeProducts": 20,
    "minPrice": 19.99,
    "maxPrice": 1299.99,
    "avgPrice": 149.74,
    "totalStock": 1127
  }
}
```

---

## GitHub
```bash
git init
git add .
git commit -m "Experiment 3: Working with HQL – Sorting, Pagination and Aggregates"
git remote add origin <your-repo-url>
git push -u origin main
```
