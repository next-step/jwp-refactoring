package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Menus;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.order.dao.OrderLineItemRepository;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final MenuService menuService;
    private final TableService tableService;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final MenuService menuService,
            final TableService tableService
    ) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.menuService = menuService;
        this.tableService = tableService;
    }

    @Transactional
    public Order create(final OrderCreateRequest request) {
        OrderTable orderTable = tableService.getOrderTable(request.getOrderTable());
        Menus menus = menuService.findMenusInIds(request.getMenus());

        validateOrderCreate(request, orderTable, menus);


        Order order = request.of(orderTable, orderLineItemRequest -> {
            Menu menu = menus.findMenuById(orderLineItemRequest.getMenu());
            Quantity quantity = new Quantity(orderLineItemRequest.getQuantity());

            return new OrderLineItem(menu, quantity);
        });
        order.prepareForSave();

        return orderRepository.save(order);
    }

    public Orders list() {
        return new Orders(orderRepository.findAll());
    }

    public Order getOrder(final Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 에 해당하는 주문을 찾을 수 없습니다."));
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = getOrder(orderId);

        savedOrder.changeOrderStatus(orderStatus);

        return savedOrder;
    }

    public OrderStatus getOrderStatusByOrderTableId(final Long orderTableId) {
        return orderRepository.findOrderStatusByOrderTableId(orderTableId);
    }

    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTables, final List<OrderStatus> orderStatuses) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTables, orderStatuses);
    }

    private void validateOrderCreate(final OrderCreateRequest request, final OrderTable orderTable, Menus menus) {
        if (menus.isNotAllContainIds(request.getMenus())) {
            throw new IllegalArgumentException("주문에 저장되지 않은 메뉴가 존재합니다.");
        }

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 자리의 테이블에 주문을 할 수 없습니다.");
        }
    }
}
