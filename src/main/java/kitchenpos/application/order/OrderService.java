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
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.exception.order.EmptyOrderLineItemOrderException;
import kitchenpos.exception.order.EmptyOrderTableOrderException;
import kitchenpos.exception.order.NotChangableOrderStatusException;
import kitchenpos.exception.order.NotFoundOrderException;
import kitchenpos.exception.order.NotRegistedMenuOrderException;
import kitchenpos.exception.table.NotFoundOrderTableException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        validationOfCreate(orderTable, orderLineItemDtos);

        final Orders newOrder = Orders.of(orderTable, OrderStatus.COOKING);

        mappingOrderLineItem(newOrder, orderLineItemDtos);

        return OrderDto.of(orderRepository.save(newOrder));
    }

    private void mappingOrderLineItem(final Orders order, final List<OrderLineItemDto> orderLineItemDtos) {
        List<Long> menuIds = orderLineItemDtos.stream().map(OrderLineItemDto::getMenuId).collect(Collectors.toList());
        Menus menus = Menus.of(menuService.findAllByIdIn(menuIds));        
        
        for (OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            Menu matchingMenu = menus.findById(orderLineItemDto.getMenuId());

            OrderLineItem orderLineItem = OrderLineItem.of(order, matchingMenu, orderLineItemDto.getQuantity());
            orderLineItem.acceptOrder(order);
        }
    }

    private void validationOfCreate(final OrderTable orderTable, final List<OrderLineItemDto> orderLineItems) {
        checkEmptyOfOrderLineItems(orderLineItems);
        checkExistOfMenu(orderLineItems);
        checkEmptyTable(orderTable);
    }

    private void checkEmptyTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableOrderException();
        }
    }

    private void checkExistOfMenu(final List<OrderLineItemDto> orderLineItemDtos) {
        final List<Long> menuIds = orderLineItemDtos.stream().map(OrderLineItemDto::getMenuId).collect(Collectors.toList());

        if (orderLineItemDtos.size() != menuService.countByIdIn(menuIds)) {
            throw new NotRegistedMenuOrderException();
        }
    }

    private void checkEmptyOfOrderLineItems(final List<OrderLineItemDto> orderLineItemDtos) {
        if (orderLineItemDtos.isEmpty()) {
            throw new EmptyOrderLineItemOrderException();
        }
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
}
