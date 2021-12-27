package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

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
    public OrderResponse create(final OrderRequest orderRequest) {

        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new IllegalArgumentException("주문 항목이 비어 있습니다.");
        }

        if (validateMenuIds(orderRequest.getMenuIds())) {
            throw new IllegalArgumentException("메뉴 ID 목록이 유효하지 않습니다. "
                + "존재하지 않는 메뉴가 있거나, 목록이 unique 하지 않습니다.");
        }

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다. tableId =" + orderRequest.getOrderTableId()));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문을 등록할 수 없습니다.");
        }

        final OrderLineItems orderLineItems = orderLineItemsFromRequest(orderRequest.getOrderLineItems());
        final Order savedOrder = orderRepository.save(orderRequest.toEntity(orderTable, orderLineItems));
        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return OrderResponse.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다. orderId =" + orderId));

        if (savedOrder.isCompleted()) {
            throw new IllegalArgumentException("이미 완료(COMPLETION) 된 주문입니다.");
        }

        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.from(savedOrder);
    }

    private OrderLineItems orderLineItemsFromRequest(List<OrderLineItemRequest> requests) {
        final OrderLineItems orderLineItems = new OrderLineItems();
        for (final OrderLineItemRequest orderLineItem : requests) {
            Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다. menuId =" + orderLineItem.getMenuId()));
            orderLineItems.add(orderLineItem.toEntity(menu));
        }
        return orderLineItems;
    }

    private boolean validateMenuIds(List<Long> menuIds) {
        return menuIds.size() != menuRepository.countByIdIn(menuIds);
    }
}
