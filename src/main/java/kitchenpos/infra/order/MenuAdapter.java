package kitchenpos.infra.order;

import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.SafeMenu;
import kitchenpos.domain.order.exceptions.MenuEntityNotFoundException;
import kitchenpos.infra.menu.MenuDao;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuAdapter implements SafeMenu {
    private final MenuDao menuDao;

    public MenuAdapter(final MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    public void isMenuExists(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new MenuEntityNotFoundException("존재하지 않는 메뉴가 있습니다.");
        }
    }
}
