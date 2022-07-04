Products API
===
### POST /api/products

Request
```
{
  "name": "강정치킨",
  "price": 17000
}
```

Response
```
{
  "id": 7,
  "name": "강정치킨",
  "price": 17000.00
}
```

### GET /api/products

Response
```
[
  {
    "id": 1,
    "name": "후라이드",
    "price": 16000.00
  },
  {
    "id": 2,
    "name": "양념치킨",
    "price": 16000.00
  }
]
```
---
