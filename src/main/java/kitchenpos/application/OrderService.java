package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderDto;
import kitchenpos.dto.OrderLineItemDto;
import kitchenpos.dto.OrderStatus;
import kitchenpos.repository.MenuDao;
import kitchenpos.repository.OrderDao;
import kitchenpos.repository.OrderTableDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            OrderTableDao orderTableDao) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    public OrderDto create(final OrderDto order) {
        final List<OrderLineItemDto> orderLineItems = order.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Order savedOrder = orderDao.save(order.toEntity());

        return OrderDto.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> list() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream().map(OrderDto::of)
                .collect(Collectors.toList());
    }

    public OrderDto changeOrderStatus(final Long orderId, final OrderDto order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        orderDao.save(savedOrder);

        return OrderDto.of(savedOrder);
    }
}
