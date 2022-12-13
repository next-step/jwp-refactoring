package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderCrudService {

    public static final String ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE = "주문 항목이 비어있을 수 없다.";
    public static final String ORDERLINEITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE = "주문 항목의 수와 메뉴의 수는 같아야 한다.";
    public static final String ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE = "주문 테이블은 비어있을 수 없습니다.";
    private final MenuRepository menuRepository;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderCrudService(final MenuRepository menuRepository, final OrderDao orderDao, final OrderLineItemDao orderLineItemDao, final OrderTableDao orderTableDao) {
        this.menuRepository = menuRepository;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItem> orderLineItems = request.toOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE);
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.findAllById(menuIds).size()) {
            throw new IllegalArgumentException(ORDERLINEITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE);
        }

        final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE);
        }

        final Order savedOrder = orderDao.save(request.toOrder());

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return new OrderResponse(savedOrder);
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }
}
