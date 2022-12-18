package kitchenpos.domain;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.dto.MenuQuantityPair;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public void add(OrderLineItem orderLineItem) {
        if (!this.orderLineItems.contains(orderLineItem)) {
            this.orderLineItems.add(orderLineItem);
        }
    }

    public List<MenuQuantityPair> getMenuQuantityPairs(List<Menu> menus) {
        return this.orderLineItems.stream()
            .map(orderLineItem -> new MenuQuantityPair(findMenuName(menus, orderLineItem),
                orderLineItem.getQuantity()))
            .collect(toList());
    }

    private Name findMenuName(List<Menu> menus, OrderLineItem orderLineItem) {
        return findMenuById(orderLineItem.getMenuId(), menus)
            .getName();
    }

    public Menu findMenuById(Long menuId, List<Menu> menus) {
        return menus.stream()
            .filter(menu -> menu.getId().equals(menuId))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }

}
