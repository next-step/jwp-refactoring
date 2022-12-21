# Kitchenpos Aggregate

> - 애그리거트와 해당 애그리거트에 속한 엔티티, VO 정의
> - 기존에는 없으나 추가가 필요해보이는 VO 정의 (이름이 같아도 애그리거트 별로 별도 클래스 생성)

## Product
- `Entity` Product `Root`
  - `VO` Price (추가)


## Menu
- `Entity` Menu `Root`
  - `VO` Price (추가)


- `Entity` MenuProduct
  - `VO` Quantity (추가)


## MenuGroup
- `Entity` MenuGroup `Root`


## Order
- `Entity` Order `Root`
  - `VO` OrderStatus


- `Entity` OrderLineItem
  - `VO` Quantity (추가)


## OrderTable
- `Entity` OrderTable `Root`
  - `VO` NumberOfGuests (추가)



## TableGroup
- `Entity` TableGroup `Root`
