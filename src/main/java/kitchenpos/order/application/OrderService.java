package kitchenpos.order.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.NotExistOrderException;
import kitchenpos.table.application.TableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuService menuService;
    private final OrderRepository orderRepository;
    private final TableService tableService;

    public OrderService(
            final MenuService menuService,
            final OrderRepository orderRepository,
            final TableService tableService
    ) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.tableService = tableService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = convertToOrder(orderRequest);
        menuService.validateAllMenusExist(order.menuIds());
        tableService.validateTableToMakeOrder(order.getOrderTableId());
        return OrderResponse.of(orderRepository.save(order));
    }

    private Order convertToOrder(OrderRequest orderRequest) {
        List<OrderLineItemDto> orderLineItemDtos = orderRequest.getOrderLineItemDtos();
        List<OrderLineItem> orderLineItems = orderLineItemDtos.stream()
                .map(OrderLineItemDto::toOrderLineItem)
                .collect(toList());

        return new Order(orderRequest.getOrderTableId(), orderLineItems);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(NotExistOrderException::new);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderRequest.getOrderStatus()));
        return OrderResponse.of(orderRepository.save(savedOrder));
    }
}
