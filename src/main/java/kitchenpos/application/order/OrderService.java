package kitchenpos.application.order;

import kitchenpos.application.menu.MenuService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.Menus;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.OrdersRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.OrderDto;
import kitchenpos.dto.order.OrderLineItemDto;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.exception.order.NotFoundOrderException;
import kitchenpos.exception.order.NotRegistedMenuOrderException;
import kitchenpos.exception.table.NotFoundOrderTableException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuService menuService;
    private final OrdersRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuService menuService,
            final OrdersRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderDto create(final OrderDto order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId()).orElseThrow(NotFoundOrderTableException::new);
        final List<OrderLineItemDto> orderLineItemDtos = order.getOrderLineItems();

        OrderLineItems orderLineItems = createOrderLineItems(orderLineItemDtos);

        checkExistOfMenu(orderLineItems, orderLineItemDtos);

        final Orders newOrder = Orders.of(orderTable, OrderStatus.COOKING, orderLineItems);

        return OrderDto.of(orderRepository.save(newOrder));
    }

    private void checkExistOfMenu(final OrderLineItems orderLineItems, final List<OrderLineItemDto> orderLineItemDtos) {
        if (orderLineItems.size() != orderLineItemDtos.size()) {
            throw new NotRegistedMenuOrderException();
        }
    }

    private OrderLineItems createOrderLineItems(final List<OrderLineItemDto> orderLineItemDtos) {
        List<Long> menuIds = findMenuIds(orderLineItemDtos);
        Menus menus = Menus.of(menuService.findAllByIdIn(menuIds));
        
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        
        for (OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            Menu matchingMenu = menus.findById(orderLineItemDto.getMenuId());

            orderLineItems.add(OrderLineItem.of(matchingMenu, orderLineItemDto.getQuantity()));
        }

        return OrderLineItems.of(orderLineItems);
    }

    private List<Long> findMenuIds(final List<OrderLineItemDto> orderLineItemDtos) {
        return orderLineItemDtos.stream().map(OrderLineItemDto::getMenuId).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderDto> list() {
        return orderRepository.findAll().stream()
                                .map(OrderDto::of)
                                .collect(Collectors.toList());
    }

    @Transactional
    public Orders changeOrderStatus(final Long orderId, final OrderDto order) {
        final Orders savedOrder = orderRepository.findById(orderId)
                                                .orElseThrow(NotFoundOrderException::new);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(order.getOrderStatus()));

        return savedOrder;
    }

    public Orders findByOrderTableId(Long orderTableId) {
        return orderRepository.findByOrderTableId(orderTableId);
    }

    public List<Orders> findAllByOrderTableIdIn(List<Long> orderTableIds) {
        return orderRepository.findAllByOrderTableIdIn(orderTableIds);
    }
}
