Tables API
===
### POST /api/tables

Request
```
{
  "numberOfGuests": 0,
  "empty": true
}
```

Response
```
{
  "id": 9,
  "tableGroupId": null,
  "numberOfGuests": 0,
  "empty": true
}
```

---
### GET /api/tables

Response
```
[
  {
    "id": 1,
    "tableGroupId": null,
    "numberOfGuests": 0,
    "empty": true
  },
  {
    "id": 2,
    "tableGroupId": null,
    "numberOfGuests": 0,
    "empty": true
  }
]
```

---
### PUT /api/tables/1/empty

Request
```
{
  "empty": false
}
```

Response
```
{
  "id": 1,
  "tableGroupId": null,
  "numberOfGuests": 0,
  "empty": false
}
```

---
### PUT /api/tables/1/number-of-guests

Request
```
{
  "numberOfGuests": 4
}
```

Response
```
{
  "id": 1,
  "tableGroupId": null,
  "numberOfGuests": 4,
  "empty": false
}
```

---

