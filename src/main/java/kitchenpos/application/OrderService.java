package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
    public OrderResponse create(OrderRequest request) {
        validate(request);
        final Order order = orderRepository.save(request.toOrder());
        return OrderResponse.of(order);
    }

    private void validate(OrderRequest request) {
        validateOrderLineItems(request.toOrderLineItems());
        validateOrderTable(request.getOrderTableId());
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문내역이 비어있습니다.");
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 주문내역이 있습니다.");
        }
    }

    private void validateOrderTable(Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있습니다.");
        }
    }

    public List<OrderResponse> list() {
        return OrderResponse.of(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);
        order.changeOrderStatus(orderStatus);
        return OrderResponse.of(order);
    }
}
