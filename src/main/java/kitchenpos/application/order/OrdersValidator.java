package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import kitchenpos.application.menu.MenuService;
import kitchenpos.domain.menu.Menus;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.OrdersRepository;
import kitchenpos.dto.order.OrderDto;
import kitchenpos.dto.order.OrderLineItemDto;
import kitchenpos.event.orders.ValidateEmptyTableEvent;
import kitchenpos.exception.order.EmptyOrderLineItemOrderException;
import kitchenpos.exception.order.NotChangableOrderStatusException;
import kitchenpos.exception.order.NotFoundOrderException;
import kitchenpos.exception.order.NotRegistedMenuOrderException;
import kitchenpos.vo.MenuId;
import kitchenpos.vo.OrderTableId;

@Component
public class OrdersValidator {
    private final OrdersRepository ordersRepository;
    private final MenuService menuService;
    private final ApplicationEventPublisher eventPublisher;

    public OrdersValidator (
        final OrdersRepository ordersRepository,
        final MenuService menuService,
        final ApplicationEventPublisher eventPublisher
    ) {
        this.ordersRepository = ordersRepository;
        this.menuService = menuService;
        this.eventPublisher = eventPublisher;
    }

    public Orders getValidatedOrdersForCreate(OrderDto orderDto) {
        final List<OrderLineItemDto> orderLineItemDtos = orderDto.getOrderLineItems();

        final OrderLineItems orderLineItems = createOrderLineItems(orderLineItemDtos);

        final Orders order = Orders.of(OrderTableId.of(orderDto.getOrderTableId()), OrderStatus.COOKING, orderLineItems);

        eventPublisher.publishEvent(new ValidateEmptyTableEvent(order.getOrderTableId().value()));
        
        checkEmptyOfOrderLineItems(order.getOrderLineItems());
        checkExistOfMenu(order.getOrderLineItems());

        return order;
    }

    private OrderLineItems createOrderLineItems(final List<OrderLineItemDto> orderLineItemDtos) {
        List<OrderLineItem> orderLineItems = orderLineItemDtos.stream()
                                                                .map(orderLineItemDto -> OrderLineItem.of(MenuId.of(orderLineItemDto.getMenuId()), orderLineItemDto.getQuantity()))
                                                                .collect(Collectors.toList());

        return OrderLineItems.of(orderLineItems);
    }

    private void checkExistOfMenu(final List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                                            .map(OrderLineItem::getMenuId)
                                            .map(MenuId::value)
                                            .collect(Collectors.toList());

        Menus menus = Menus.of(menuService.findAllByIdIn(menuIds));

        if (menus.size() != menuIds.size()) {
            throw new NotRegistedMenuOrderException();
        }
    }

    private static void checkEmptyOfOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new EmptyOrderLineItemOrderException();
        }
    }

    public Orders getValidatedOrdersForChangeOrderStatus(Long orderId) {
        final Orders savedOrder = ordersRepository.findById(orderId)
                                                    .orElseThrow(NotFoundOrderException::new);

        validateionOfChageOrderStatus(savedOrder);

        return savedOrder;
    }

    private void validateionOfChageOrderStatus(Orders order) {
        if (order.isCompletion()) {
            throw new NotChangableOrderStatusException();
        }
    }
}
