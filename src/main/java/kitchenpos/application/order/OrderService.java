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
import kitchenpos.exception.order.NotChangableOrderStatusException;
import kitchenpos.exception.order.NotFoundOrderException;
import kitchenpos.exception.order.NotRegistedMenuOrderException;
import kitchenpos.exception.table.NotFoundOrderTableException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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

        if (orderLineItems.size() != orderLineItemDtos.size()) {
            throw new NotRegistedMenuOrderException();
        }

        final Orders newOrder = Orders.of(orderTable, OrderStatus.COOKING, orderLineItems);

        return OrderDto.of(orderRepository.save(newOrder));
    }


    private OrderLineItems createOrderLineItems(final List<OrderLineItemDto> orderLineItemDtos) {
        List<Long> menuIds = orderLineItemDtos.stream().map(OrderLineItemDto::getMenuId).collect(Collectors.toList());
        Menus menus = Menus.of(menuService.findAllByIdIn(menuIds));
        
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        
        for (OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            Menu matchingMenu = menus.findById(orderLineItemDto.getMenuId());

            orderLineItems.add(OrderLineItem.of(matchingMenu, orderLineItemDto.getQuantity()));
        }
        return OrderLineItems.of(orderLineItems);
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

        validateionOfChageOrderStatus(savedOrder);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(order.getOrderStatus()));

        return savedOrder;
    }

    private void validateionOfChageOrderStatus(final Orders savedOrder) {
        if (savedOrder.isCompletion()) {
            throw new NotChangableOrderStatusException();
        }
    }
    
    public boolean isExistNotCompletionOrder(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }

    public boolean isNotCompletionOrder(Long orderTableId) {
        return !orderRepository.existsByOrderTableIdAndOrderStatus(orderTableId, OrderStatus.COMPLETION) 
                && orderRepository.existsByOrderTableId(orderTableId);
    }

    public Orders findByOrderTableId(Long orderTableId) {
        return orderRepository.findByOrderTableId(orderTableId);
    }
}
