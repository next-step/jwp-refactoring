package kitchenpos.order.mapper;

import kitchenpos.domain.Quantity;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Menus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.table.dao.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderMapper(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public Order mapFrom(OrderCreateRequest request) {
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTable())
                .orElseThrow(() -> new IllegalArgumentException(request.getOrderTable() + "에 해당하는 주문 테이블이 없습니다."));
        OrderLineItems orderLineItems = convertOrderLineItemsByRequest(request);

        checkOrderTable(orderTable);

        return new Order(orderTable.getId(), orderLineItems);
    }

    private OrderLineItems convertOrderLineItemsByRequest(final OrderCreateRequest request) {
        List<Long> menuIds = request.getMenus();
        Menus menus = new Menus(menuRepository.findAllById(menuIds));

        checkAllMenuIsExist(menus, menuIds);

        List<OrderLineItem> orderLineItems = request.getOrderLineItems()
                .stream()
                .map(item -> {
                    Menu menu = menus.findMenuById(item.getMenu());

                    return new OrderLineItem(
                            item.getMenu(), new Quantity(item.getQuantity()), menu.getPrice(), menu.getName()
                    );
                }).collect(Collectors.toList());

        return new OrderLineItems(orderLineItems);
    }

    private void checkAllMenuIsExist(final Menus menus, final List<Long> menuIds) {

        if (menuIds.isEmpty()) {
            throw new IllegalArgumentException("주문에 메뉴가 포함되어 있지 않습니다.");
        }
        if (menus.isNotAllContainIds(menuIds)) {
            throw new IllegalArgumentException("주문에 저장되지 않은 메뉴가 존재합니다.");
        }
    }

    private void checkOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 자리의 테이블에 주문을 할 수 없습니다.");
        }
    }
}
