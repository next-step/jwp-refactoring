package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.ChangeOrderStatusDto;
import kitchenpos.order.dto.CreateOrderDto;
import kitchenpos.order.dto.OrderDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static java.util.stream.Collectors.toList;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository,
                        OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository,
                        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderDto create(CreateOrderDto createOrderDto) {

        if (CollectionUtils.isEmpty(createOrderDto.getOrderLineItems())) {
            throw new IllegalArgumentException();
        }

        final List<OrderLineItem> orderLineItems =
            createOrderDto.getOrderLineItems()
                    .stream()
                    .map(dto -> {
                        Menu menu = menuRepository.findById(dto.getMenuId()).orElseThrow(IllegalArgumentException::new);
                        return new OrderLineItem(menu, dto.getQuantity());
                    })
                    .collect(toList());

        final List<Long> menuIds = orderLineItems.stream()
                                                 .map(orderLineItem -> orderLineItem.getMenu().getId())
                                                 .collect(toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(createOrderDto.getOrderTableId())
                                                          .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        for (OrderLineItem orderLineItem : orderLineItems) {
            savedOrder.addOrderLineItem(orderLineItem);
            orderLineItemRepository.save(orderLineItem);
        }

        return OrderDto.of(savedOrder);
    }

    public List<OrderDto> list() {
        return orderRepository.findAll()
                              .stream()
                              .map(OrderDto::of)
                              .collect(toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(Long orderId, ChangeOrderStatusDto changeOrderStatusDto) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (OrderStatus.COMPLETION == savedOrder.getOrderStatus()) {
            throw new IllegalArgumentException();
        }

        OrderStatus orderStatus = OrderStatus.valueOf(changeOrderStatusDto.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        orderRepository.save(savedOrder);

        return OrderDto.of(savedOrder);
    }
}
