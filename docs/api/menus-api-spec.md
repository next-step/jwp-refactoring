Menus API
===
### POST /api/menus

Request
```
{
  "name": "후라이드+후라이드",
  "price": 19000,
  "menuGroupId": 1,
  "menuProducts": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

Response
```
{
  "id": 7,
  "name": "후라이드+후라이드",
  "price": 19000.00,
  "menuGroupId": 1,
  "menuProducts": [
    {
      "seq": 7,
      "menuId": 7,
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

### GET /api/menus

Response
```
[
  {
    "id": 1,
    "name": "후라이드치킨",
    "price": 16000.00,
    "menuGroupId": 2,
    "menuProducts": [
      {
        "seq": 1,
        "menuId": 1,
        "productId": 1,
        "quantity": 1
      }
    ]
  },
  {
    "id": 2,
    "name": "양념치킨",
    "price": 16000.00,
    "menuGroupId": 2,
    "menuProducts": [
      {
        "seq": 2,
        "menuId": 2,
        "productId": 2,
        "quantity": 1
      }
    ]
  }
]
```
---
