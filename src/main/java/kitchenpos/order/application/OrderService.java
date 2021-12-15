package kitchenpos.order.application;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.menu.MenuRepository;
import kitchenpos.order.domain.order.Order;
import kitchenpos.order.domain.orderLineItem.OrderLineItemRepository;
import kitchenpos.order.domain.order.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.sns.application.SnsEventHandler;
import kitchenpos.sns.domain.KakaoSender;
import kitchenpos.table.domain.table.OrderTable;
import kitchenpos.table.domain.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;
    private final SnsEventHandler snsEventHandler;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository, OrderTableRepository orderTableRepository, SnsEventHandler snsEventHandler) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
        this.snsEventHandler = snsEventHandler;
    }

    public OrderResponse saveOrder(final OrderRequest orderRequest) {

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 없습니다."));

        final Order newOrder = new Order(orderTable);
        setUpOrderLineItem(newOrder, orderRequest.getOrderLineItems());

        return OrderResponse.of(saveOrderAndSnsSend(newOrder));
    }

    private Order saveOrderAndSnsSend(Order newOrder) {
        Order savedOrder = orderRepository.save(newOrder);

        String message = savedOrder.getOrderTable().getId() + "번 테이블 주문되었습니다.";

        snsEventHandler.sns(KakaoSender.from(message));
        return savedOrder;
    }

    private void setUpOrderLineItem(Order saveOrder, List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 상품 내역이 없습니다.");
        }

        for (OrderLineItemRequest orderLineItem : orderLineItems) {
            Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다."));
            saveOrder.addOrderLineItem(menu, orderLineItem.getQuantity());
        }
    }

    public List<OrderResponse> findAllOrder() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());

        return OrderResponse.of(orderRepository.save(savedOrder));
    }
}
