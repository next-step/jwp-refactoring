package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;

    public OrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validate(Order order) {
        validate(order, getMenus(order));
    }

    private void validate(Order order, List<Menu> menus) {
        if (getMenuIds(order).size() != menus.size()) {
            throw new IllegalArgumentException("메뉴가 변경되었습니다.");
        }

        if (order.getOrderTable().isEmpty()) {
            throw new IllegalArgumentException("주문테이블이 빈테이블입니다.");
        }

        if (CollectionUtils.isEmpty(order.getOrderLineItems())) {
            throw new IllegalArgumentException("주문항목이 존재하지 않습니다.");
        }
    }

    private List<Menu> getMenus(Order order) {
        List<Long> menuIds = getMenuIds(order);
        List<Menu> menus = menuRepository.findAllById(menuIds);
        return menus;
    }

    private List<Long> getMenuIds(Order order) {
        return order.getOrderLineItems().stream()
                .map(orderLineItem -> orderLineItem.getMenuId())
                .collect(Collectors.toList());
    }
}
