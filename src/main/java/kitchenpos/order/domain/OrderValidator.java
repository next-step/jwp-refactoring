package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static java.util.stream.Collectors.toList;

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

    public List<OrderMenu> checkItems(final OrderRequest order) {
        List<OrderLineItemRequest> orderLineItems = order.getOrderLineItems();
        checkItemsNotEmpty(orderLineItems);
        List<Menu> menus = checkMenuExist(orderLineItems);
        return menus.stream()
                .map(OrderMenu::new)
                .collect(toList());
    }

    private void checkItemsNotEmpty(final List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(ORDER_ITEM_IS_ESSENTIAL);
        }
    }

    private List<Menu> checkMenuExist(final List<OrderLineItemRequest> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(toList());
        List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menus.size() != menuIds.size()) {
            throw new IllegalArgumentException(MENU_IS_NOT_EXIST);
        }
        return menus;
    }

    public void checkOrderTable(final OrderRequest order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException(ORDER_TABLE_IS_NOT_EXIST));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(CANNOT_ORDER_ON_EMPTY_TABLE);
        }
    }
}
