package kitchenpos.validation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menus;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.order.domain.OrdersValidator;
import kitchenpos.order.dto.OrderDto;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.event.ValidateEmptyTableEvent;
import kitchenpos.order.exception.EmptyOrderLineItemOrderException;
import kitchenpos.order.exception.NotChangableOrderStatusException;
import kitchenpos.order.exception.NotFoundOrderException;
import kitchenpos.common.vo.MenuId;
import kitchenpos.menu.exception.NotRegistedMenuOrderException;
import kitchenpos.common.vo.OrderTableId;

@Component
public class OrdersValidatorImpl implements OrdersValidator {
    private final OrdersRepository ordersRepository;
    private final MenuService menuService;
    private final ApplicationEventPublisher eventPublisher;

    public OrdersValidatorImpl (
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
            throw new NotRegistedMenuOrderException("요청된 메뉴와 조회된 메뉴의 수가 일치하지 않습니다.");
        }
    }

    private static void checkEmptyOfOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new EmptyOrderLineItemOrderException("주문상품이 없습니다.");
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
            throw new NotChangableOrderStatusException("주문이 계산완료된 상태입니다.");
        }
    }
}
