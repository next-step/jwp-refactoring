package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.ChangeOrderStatusRequest;
import kitchenpos.dto.order.CreateOrderRequest;
import kitchenpos.dto.order.CreateOrderTableItemRequest;
import kitchenpos.dto.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    public static final String MENU_NOT_FOUND_ERROR_MESSAGE = "존재하지 않는 메뉴입니다.";
    public static final String EMPTY_ORDER_TABLE_ERROR_MESSAGE = "존재하지 않는 주문 테이블 입니다.";
    public static final String ORDER_NOT_FOUND_ERROR_MESSAGE = "존재하지 않는 주문 입니다.";

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final CreateOrderRequest createOrderRequest) {
        List<OrderLineItem> orderLineItems = createOrderLineItemWithMenu(createOrderRequest.getOrderLineItemRequests());
        final OrderTable orderTable = orderTableRepository.findById(createOrderRequest.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException(EMPTY_ORDER_TABLE_ERROR_MESSAGE));

        Order order = createOrderRequest.toOrder(orderTable, orderLineItems);
        return OrderResponse.from(orderRepository.save(order));
    }

    private List<OrderLineItem> createOrderLineItemWithMenu(List<CreateOrderTableItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
            .map(it -> {
                Menu menu = menuRepository.findById(it.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException(MENU_NOT_FOUND_ERROR_MESSAGE));
                return new OrderLineItem(menu, it.getQuantity());
            })
            .collect(Collectors.toList());
    }

    // TODO N+1 발생 : N개의 주문을 조회하는 경우 이므로 1:1 관계는 Fetch Join으로, 1:N 관계는 LazyLoading + Batch Size를 이용한 In절로 접근
    // JPA로 리팩토링을 진행하면서 orderLineItem이 다른 Entity와 연관관계를 가지는 경우 또 다른 N+1이 발생할 수 있음에 주의
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest changeOrderStatusRequest) {
        final Order savedOrder = findOrderById(orderId);
        savedOrder.changeOrderStatus(changeOrderStatusRequest.getOrderStatus());
        return OrderResponse.from(savedOrder);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND_ERROR_MESSAGE));
    }
}
