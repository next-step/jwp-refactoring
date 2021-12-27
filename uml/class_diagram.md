### old class diagram
```
@startuml
Entity MenuGroup

Entity Menu {
 - MenuGroup menuGroup
 - List<MenuProduct> menuProducts
}

Entity Product

Entity MenuProduct {
 - Menu menu
 - Product product
}


Entity Order {
 - OrderTable orderTable
}
Entity OrderLineItem {
 - Order order
 - Menu menu
}
Entity OrderTable {
 - TableGroup tableGroup
}
Entity TableGroup {
 - List<OrderTable> orderTables
}


MenuGroup -> Menu : menuGroup
Menu -> MenuProduct : menu
Menu <- MenuProduct : List<MenuProduct>
MenuProduct <-- Product : product
OrderTable --> Order : orderTable
TableGroup -> OrderTable : tableGroup
TableGroup <- OrderTable : List<OrderTable>
Order ---> OrderLineItem : order
Order <--- OrderLineItem : List<OrderLineItem>
Menu ---> OrderLineItem : menu
@enduml
```

### refactor class diagram
```
@startuml
Entity MenuGroup

Entity Menu {
 - Long menuGroupId
 - List<MenuProduct> menuProducts
}

Entity Product

Entity MenuProduct {
 - Menu menu
 - Long productId
}


Entity Order {
 - Long orderTableId
}
Entity OrderLineItem {
 - Order order
 - Long menuId
}
Entity OrderTable {
 - TableGroup tableGroup
}
Entity TableGroup {
 - List<OrderTable> orderTables
}

MenuGroup ..> Menu : menuGroupId
Menu -> MenuProduct : menu
Menu <- MenuProduct : List<MenuProduct>
MenuProduct <.. Product : productId
OrderTable ..> Order : orderTableId
TableGroup -> OrderTable : tableGroup
TableGroup <- OrderTable : List<OrderTable>
Order ---> OrderLineItem : order
Order <--- OrderLineItem : List<OrderLineItem>
Menu ...> OrderLineItem : menuId
@enduml
```

### menu create uml
```
@startuml
!theme toy
class Menu{
 + validateTotalPrice(List<Product>)
}

class MenuService{
 - MenuRepository
 + create()
}

class MenuValidator{
 - MenuGroupService
 - MenuProductService
 + validate(Menu)
 - validateMenuGroup(Long)
 - validateMenuProducts(Menu)
 - getValidProducts(Menu)
}

class MenuGroupService{
 - MenuGroupRepository
 + existsById(Long)
}

class ProductService{
 + findAllByIdIn(List<Long>)
}


MenuService::create -> MenuValidator::validate : menu >

MenuValidator::validateMenuGroup -> MenuGroupService::existsById : > menuGroupId
MenuValidator::getValidProducts ---> ProductService::findAllByIdIn : > productIds
MenuValidator::validateMenuProducts --> Menu::validateTotalPrice : > List<product>
@enduml
```