package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
        final List<Menu> menus = menuService.findAllByIds(orderRequest.getMenuIds());

        final OrderTable orderTable = tableService.findById(orderRequest.getOrderTableId());

        final Order order = orderTable.order();

        for(Menu menu : menus) {
            final OrderLineItemRequest orderLineItemRequest = orderRequest.find(menu);
            order.addItem(menu, orderLineItemRequest.getQuantity());
        }

        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> list() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문이 존재하지 않습니다. orderId = " + orderId));

        order.nextOrderStatus();
        return OrderResponse.of(order);
    }
}
