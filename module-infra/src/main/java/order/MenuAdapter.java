package kitchenpos.infra.order;

import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.SafeMenu;
import kitchenpos.domain.order.exceptions.MenuEntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuAdapter implements SafeMenu {
    private final MenuRepository menuRepository;

    public MenuAdapter(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void isMenuExists(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new MenuEntityNotFoundException("존재하지 않는 메뉴가 있습니다.");
        }
    }
}
