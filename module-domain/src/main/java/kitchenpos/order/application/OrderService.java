package kitchenpos.order.application;

import java.util.*;

import kitchenpos.menu.domain.Menu;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;

import kitchenpos.common.*;
import kitchenpos.menu.repository.*;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.*;
import kitchenpos.order.repository.*;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.*;
import kitchenpos.table.repository.*;

@Service
public class OrderService {
    private static final String ORDER = "주문";
    private static final String ORDER_TABLE = "주문 테이블";
    private static final String ORDER_LINE_ITEM = "주문 항목";
    private static final String MENU = "메뉴";
    private static final String ORDER_IS_NOT_COMPLETED_EXCEPTION_STATEMENT = "주문 완료가 되지 않았습니다.";

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
                        OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public static OrderService of(MenuRepository menuRepository, OrderRepository orderRepository
        , OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        return new OrderService(menuRepository, orderRepository, orderTableRepository, tableGroupRepository);
    }

    public OrderResponse saveOrder(OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(() -> new NotFoundException(ORDER_TABLE));

        Order saveOrder = Order.from(orderTable);
        saveOrder = orderRepository.save(saveOrder);
        setUpOrderLineItem(saveOrder, orderRequest.getOrderLineItems());
        return OrderResponse.from(orderRepository.save(saveOrder));
    }

    private void setUpOrderLineItem(Order saveOrder, List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new NotFoundException(ORDER_LINE_ITEM);
        }

        for (OrderLineItemRequest orderLineItem : orderLineItems) {
            Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                .orElseThrow(() -> new NotFoundException(MENU));
            saveOrder.addOrderLineItem(menu, orderLineItem.getQuantity());
        }
    }

    public List<OrderResponse> findAll() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();

        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            OrderTable orderTable = order.getOrderTable();
            TableGroup tableGroup = tableGroupRepository.save(TableGroup.create());
            orderTable = new OrderTable(tableGroup.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
            orderResponses.add(new OrderResponse(order.getId(), orderTable, order.getOrderStatus(), order.getOrderLineItems(), order.getOrderedTime()));
        }

        return orderResponses;
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderRequest orderRequest) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException(ORDER));
        order.changeOrderStatus(orderRequest.getOrderStatus());

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.create());
        OrderTable orderTable = order.getOrderTable();
        orderTable = new OrderTable(tableGroup.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());

        return new OrderResponse(order.getId(), orderTable, order.getOrderStatus(), order.getOrderLineItems(), order.getOrderedTime());
    }

    @Transactional
    public OrderTableResponse cleanTable(final Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new NotFoundException(ORDER_TABLE));

        checkCompletion(savedOrderTable);
        savedOrderTable.cleanTable();

        return OrderTableResponse.of(savedOrderTable);
    }

    private void checkCompletion(OrderTable savedOrderTable) {
        final List<Order> orders = orderRepository.findAllByOrderTable(savedOrderTable);
        boolean isCompleted = orders.stream()
            .allMatch(it -> it.getOrderStatus().equals(OrderStatus.COMPLETION));
        if (!isCompleted) {
            throw new IllegalArgumentException(ORDER_IS_NOT_COMPLETED_EXCEPTION_STATEMENT);
        }
    }
}

