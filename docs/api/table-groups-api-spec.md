TableGroup API
===
### POST /api/table-groups

Request
```
{
  "orderTables": [
    {
      "id": 1
    },
    {
      "id": 2
    }
  ]
}
```

Response
```
{
  "id": 1,
  "createdDate": "2022-07-04T19:21:04.603",
  "orderTables": [
    {
      "id": 1,
      "tableGroupId": 1,
      "numberOfGuests": 0,
      "empty": false
    },
    {
      "id": 2,
      "tableGroupId": 1,
      "numberOfGuests": 0,
      "empty": false
    }
  ]
}
```

### DELETE /api/table-groups

---
