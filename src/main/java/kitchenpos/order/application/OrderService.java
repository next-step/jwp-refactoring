package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.order.domain.OrderLineItems.ORDER_LINE_ITEM_IS_EMPTY;

@Service
@Transactional
public class OrderService {

    private static final String NOT_FOUND_ORDER = "찾으려는 주문이 존재하지 않습니다.";
    private static final String NOT_FOUND_ORDER_TABLE = "찾으려는 주문 테이블이 존재하지 않습니다.";
    private static final String NOT_FOUND_MENU = "찾으려는 메뉴가 존재하지 않습니다.";

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItemList;

        validateOrderLineItems(orderRequest.getOrderLineItemRequests());
        validateExistsMenus(orderRequest.getOrderLineItemRequests());

        orderLineItemList = orderRequest.getOrderLineItemRequests().stream()
                .map(orderLineItemRequest -> new OrderLineItem(orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity()))
                .collect(Collectors.toList());

        final OrderLineItems orderLineItems = new OrderLineItems(orderLineItemList);
        final List<Long> menuIds = orderLineItems.menuIds();
        orderLineItems.validateMenuDataSize(menuRepository.countByIdIn(menuIds));

        final OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());
        final Order order = orderRepository.save(new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now()));

        orderLineItems.mappingOrder(order);
        order.mappingOrderLineItems(new OrderLineItems(orderLineItemRepository.saveAll(orderLineItems.orderLineItems())));
        orderTable.changeTableStatus(TableStatus.IN_USE);
        return OrderResponse.of(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_ORDER + " Find Order Id : " + orderId));
        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderRequest.getOrderStatus()).name());
        return OrderResponse.of(orderRepository.save(savedOrder));
    }

    private OrderTable findOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_ORDER_TABLE + " Find Order Table Id : " + orderTableId));
        orderTable.validateNotEmpty();
        return orderTable;
    }

    private void validateOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException(ORDER_LINE_ITEM_IS_EMPTY);
        }
    }


    private void validateExistsMenus(List<OrderLineItemRequest> orderLineItemRequests) {
        orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .forEach(this::validateExistsMenu);
    }

    private void validateExistsMenu(Long menuId) {
        menuRepository.findById(menuId)
                      .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MENU + " Find Menu Id : " + menuId));
    }
}
