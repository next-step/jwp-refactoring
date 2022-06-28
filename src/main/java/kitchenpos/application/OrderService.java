package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.domainService.MenuOrderLineDomainService;
import kitchenpos.dto.dto.OrderLineItemDTO;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.OrderException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuOrderLineDomainService menuOrderLineDomainService;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuOrderLineDomainService menuOrderLineDomainService,
        OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.menuOrderLineDomainService = menuOrderLineDomainService;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {

        menuOrderLineDomainService.validateComponentForCreateOrder(orderRequest);

        final List<OrderLineItemDTO> orderLineItems = orderRequest.getOrderLineItems();

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new OrderException("TABLE IS NOT EMPTY");
        }

        Order order = new Order();
        order.mapToTable(orderTable.getId());
        order.startCooking();

        for (final OrderLineItemDTO orderLineItem : orderLineItems) {
            order.mapOrderLineItem(
                new OrderLineItem(order, orderLineItem.getMenuId(), orderLineItem.getQuantity()));
        }

        return OrderResponse.of(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
            .stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());

        return OrderResponse.of(orderRepository.save(savedOrder));
    }
}
