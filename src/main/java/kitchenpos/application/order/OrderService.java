package kitchenpos.application.order;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItemRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.CreateOrderRequest;
import kitchenpos.dto.order.CreateOrderTableItemRequest;
import kitchenpos.dto.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    public static final String MENU_NOT_FOUND_ERROR_MESSAGE = "존재하지 않는 메뉴입니다.";
    public static final String EMPTY_ORDER_TABLE_ERROR_MESSAGE = "존재하지 않는 주문 테이블 입니다.";

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderLineItemRepository orderLineItemRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
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
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders.stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final Order order) {
        // TODO : 문제 추적 및 파악이 용이하도록 예외 처리 시 오류 문구를 포함
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        // TODO : 주문 상태 변경 로직을 주문 도메인 안쪽으로 이동 후, 해당 로직에서 주문 상태 변경 가능에 대한 유효성 검증 처리
        // TODO : 문제 추적 및 파악이 용이하도록 예외 처리 시 오류 문구를 포함
        // TODO : 주문 객체 생성 시, 초기 주문 상태를 할당하지 않은 경우 NPE 발생
        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        // TODO : 주문 상태 변경 로직을 주문 도메인 안쪽으로 이동 하여 setter가 아닌 주문 도메인이 직접 필드값 변경
        final OrderStatus orderStatus = order.getOrderStatus();
        savedOrder.changeOrderStatus(orderStatus);

        // TODO : 트랜잭션 내에서 필드 값 변경을 감지하여 갱신 쿼리 발생하도록 유도
        orderRepository.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return OrderResponse.from(savedOrder);
    }
}
