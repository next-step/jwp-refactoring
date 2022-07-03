package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderValidator {
    private static final String ORDER_ITEM_IS_ESSENTIAL = "주문 항목은 필수입니다";
    private static final String MENU_IS_NOT_EXIST = "주문 메뉴가 존재하지 않습니다";
    private static final String ORDER_TABLE_IS_NOT_EXIST = "주문 테이블이 존재하지 않습니다";
    private static final String CANNOT_ORDER_ON_EMPTY_TABLE = "빈 테이블에는 주문할 수 없습니다";
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void checkOrderLineItems(final Order order) {
        if (order.isEmptyItem()) {
            throw new IllegalArgumentException(ORDER_ITEM_IS_ESSENTIAL);
        }
        menuDetails(order);
    }

    private void menuDetails(final Order order) {
        order.getOrderLineItems().getOrderLineItems()
                .forEach(this::menuDetail);
    }

    private void menuDetail(OrderLineItem orderLineItem) {
        Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException(MENU_IS_NOT_EXIST));
        orderLineItem.setOrderMenu(new OrderMenu(menu.getName(), menu.getPrice().getValue()));
    }

    public void checkOrderTable(final Order order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException(ORDER_TABLE_IS_NOT_EXIST));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(CANNOT_ORDER_ON_EMPTY_TABLE);
        }
    }
}
