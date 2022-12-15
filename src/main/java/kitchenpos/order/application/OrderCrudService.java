package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderCrudService {

    public static final String ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE = "주문 항목이 비어있을 수 없다.";
    public static final String ORDERLINEITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE = "주문 항목의 수와 메뉴의 수는 같아야 한다.";
    public static final String ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE = "주문 테이블은 비어있을 수 없습니다.";
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderCrudService(final MenuRepository menuRepository, final OrderRepository orderRepository, final OrderLineItemRepository orderLineItemRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItem> orderLineItems = request.toOrderLineItems();

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.findAllById(menuIds).size()) {
            throw new IllegalArgumentException(ORDERLINEITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE);
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE);
        }

        final Order savedOrder = orderRepository.save(new Order(request.getOrderTableId(), request.toOrderLineItems()));

//        final Long orderId = savedOrder.getId();
//        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
//        for (final OrderLineItem orderLineItem : orderLineItems) {
//            orderLineItem.setOrderId(orderId);
//            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
//        }
//        savedOrder.setOrderLineItems(savedOrderLineItems);

        return new OrderResponse(savedOrder);
    }

    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();

//        for (final Order order : orders) {
//            order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
//        }

        return orders;
    }
}
