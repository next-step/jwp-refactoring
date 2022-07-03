package kitchenpos.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }


    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findByIdAndEmptyIsFalse(orderRequest.getOrderTableId())
                .orElseThrow(() -> new NoSuchElementException("주문이 가능한 테이블이 아닙니다"));

        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();
        validExistMenuId(orderLineItemRequests);

        Order saveOrder = orderRepository.save(Order.createOrder(orderTable.getId(),
                OrderLineItems.of(orderLineItemRequests)));

        return OrderResponse.of(saveOrder);
    }

    private void validExistMenuId(List<OrderLineItemRequest> orderLineItemRequests) {
        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new NoSuchElementException("요청한 메뉴가 존재하지 않습니다.");
        }
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map((OrderResponse::of))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("존재 하지 않은 주문 입니다."));
        OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());

        savedOrder.changOrderStatus(orderStatus);

        return OrderResponse.of(savedOrder);
    }
}
