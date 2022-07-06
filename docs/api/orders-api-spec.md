Orders API
===
### POST /api/orders

Request
```
{
  "orderTableId": 1,
  "orderLineItems": [
    {
      "menuId": 1,
      "quantity": 1
    }
  ]
}
```

Response
```
{
  "id": 1,
  "orderTableId": 1,
  "orderStatus": "COOKING",
  "orderedTime": "2022-07-04T19:16:58.659",
  "orderLineItems": [
    {
      "seq": 1,
      "orderId": 1,
      "menuId": 1,
      "quantity": 1
    }
  ]
}
```

---

### GET /api/orders

Response
```
[
  {
    "id": 1,
    "orderTableId": 1,
    "orderStatus": "COOKING",
    "orderedTime": "2022-07-04T19:16:58.659",
    "orderLineItems": [
      {
        "seq": 1,
        "orderId": 1,
        "menuId": 1,
        "quantity": 1
      }
    ]
  }
]
```

---

### PUT /api/orders/1/order-status
Request
```
{
  "orderStatus": "MEAL"
}
```

Response
```
{
  "id": 1,
  "orderTableId": 1,
  "orderStatus": "MEAL",
  "orderedTime": "2022-07-04T19:16:58.659",
  "orderLineItems": [
    {
      "seq": 1,
      "orderId": 1,
      "menuId": 1,
      "quantity": 1
    }
  ]
}
```


---

### PUT /api/orders/1/order-status

Request
```
{
  "orderStatus": "COMPLETION"
}
```

Response
```
{
  "id": 1,
  "orderTableId": 1,
  "orderStatus": "COMPLETION",
  "orderedTime": "2022-07-04T19:16:58.659",
  "orderLineItems": [
    {
      "seq": 1,
      "orderId": 1,
      "menuId": 1,
      "quantity": 1
    }
  ]
}
```

---
