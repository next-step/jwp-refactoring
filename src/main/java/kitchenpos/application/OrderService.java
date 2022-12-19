package kitchenpos.application;

import static kitchenpos.exception.ErrorCode.CAN_NOT_ORDER;
import static kitchenpos.exception.ErrorCode.NOT_FOUND_ORDER;
import static kitchenpos.exception.ErrorCode.NOT_SAME_BETWEEN_ORDER_LINE_ITEMS_AND_MENU_COUNT;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.validator.OrderValidator;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.KitchenposException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final TableService tableService;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final TableService tableService
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.tableService = tableService;
    }

    public OrderResponse create(final Order order) {
        OrderValidator.validateNullOrderLineItems(order.getOrderLineItems());
        OrderValidator.validateOrderLineItems(
                order.getOrderLineItemsSize(), menuRepository.countByIdIn(order.getMenuIds()));
        OrderValidator.validateEmptyTrue(tableService.findById(order.getOrderTableId()));

        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Order findById(final Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new KitchenposException(NOT_FOUND_ORDER));
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = findById(orderId);
        savedOrder.changeOrderStatus(orderStatus);
        return OrderResponse.of(savedOrder);
    }
}
