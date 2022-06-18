package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        validate(request);
        OrderEntity order = orderRepository.save(request.toOrder());
        order.addOrderLineItems(request.toOrderLineItems());
        return OrderResponse.of(order);
    }

    private void validate(OrderRequest request) {
        List<OrderLineItemEntity> orderLineItems = request.toOrderLineItems();
        validateEmptyOrderLineItems(orderLineItems);
        validateExistsAllMenus(orderLineItems);
        validateNotEmptyOrderTable(request);
    }

    private void validateEmptyOrderLineItems(List<OrderLineItemEntity> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new EmptyOrderLineItemsException();
        }
    }

    private void validateExistsAllMenus(List<OrderLineItemEntity> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                                           .map(OrderLineItemEntity::getMenuId)
                                           .collect(Collectors.toList());
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new InvalidOrderException("존재하지 않는 메뉴가 있습니다.");
        }
    }

    private void validateNotEmptyOrderTable(OrderRequest request) {
        OrderTableEntity orderTable = orderTableRepository.findById(request.getOrderTableId())
                                                          .orElseThrow(NotFoundOrderTableException::new);
        if (orderTable.isEmpty()) {
            throw new InvalidOrderException("빈 테이블 입니다.");
        }
    }

    public List<OrderResponse> list() {
        return OrderResponse.of(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusRequest request) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(NotFoundOrderException::new);
        order.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.of(orderRepository.save(order));
    }
}
