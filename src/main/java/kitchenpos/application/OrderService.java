package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
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

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order create(final Order order) {
        createOrderValidation(order);

        return orderRepository.save(order);
    }

    //TODO : 도메인으로 이동 방법 고민
    @Transactional(readOnly = true)
    protected void createOrderValidation(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        final List<Long> menuIds = orderLineItems.stream()
                .map(orderLineItem -> orderLineItem.getMenu().getId())
                .collect(Collectors.toList());

        orderTableRepository.findById(order.getOrderTable().getId())
                .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("메뉴가 존재하지 않습니다.");
        }
    }

    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문은 존재하지 않습니다."));

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException(String.format("%s의 상태는 변경 불가능합니다.", OrderStatus.COMPLETION.remark()));
        }

        savedOrder.chaangeOrderStatus(order.getOrderStatus());

        return savedOrder;
    }
}
