package kitchenpos.order.application;

import kitchenpos.exception.NotExistEntityException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuService menuService;
    private final OrderRepository orderRepository;
    private final TableService tableService;
    private final OrderValidator orderValidator;

    public OrderService(
            final MenuService menuService,
            final OrderRepository orderRepository,
            final TableService tableService,
            final OrderValidator orderValidator
    ) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.tableService = tableService;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<Menu> menus = menuService.findAllByIds(orderRequest.getMenuIds());
        final OrderTable orderTable = tableService.findById(orderRequest.getOrderTableId());
        final Order order = orderTable.placeOrder();

        for(Menu menu : menus) {
            final OrderLineItemRequest orderLineItemRequest = orderRequest.find(menu);
            order.addItem(menu.getId(), orderLineItemRequest.getMenuName(), orderLineItemRequest.getMenuPrice(), orderLineItemRequest.getQuantity());
        }
        order.validate(orderValidator);
        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> list() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotExistEntityException("주문이 존재하지 않습니다. orderId = " + orderId));

        order.nextOrderStatus();
        return OrderResponse.of(order);
    }
}
