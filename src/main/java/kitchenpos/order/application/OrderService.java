package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;

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
    public OrderResponse create(final OrderRequest request) {
        final OrderTable orderTable = tableService.findById(request.getOrderTableId());
        
        List<OrderLineItem> orderLineItems = createOrderLineItems(request.getOrderLineItems());
        
        Order order = Order.createOrder(orderTable, orderLineItems);
        
        order.received();

        return OrderResponse.from(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse onMealing(final Long orderId) {
        final Order order = findById(orderId);
        
        order.onMealing();
        
        return OrderResponse.from(orderRepository.save(order));
    }
    
    @Transactional
    public OrderResponse completed(final Long orderId) {
        final Order order = findById(orderId);
        
        order.completed();
        
        return OrderResponse.from(orderRepository.save(order));
    }
    
    @Transactional(readOnly = true)
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 없습니다"));
    }
    
    @Transactional(readOnly = true)
    private List<OrderLineItem> createOrderLineItems(List<OrderLineItemRequest> request) {
        List<OrderLineItem> result = new ArrayList<OrderLineItem>();
        
        List<Long> menuIds = request.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        
        List<Menu> menus = menuService.findAllByIds(menuIds);
        
        if (menus.size() != request.size()) {
            new IllegalArgumentException("등록된 메뉴만 주문할 수 있습니다");
        }
        
        for (int i = 0; i < menus.size(); i++) {
            result.add(OrderLineItem.of(menus.get(i), request.get(i).getQuantity()));
        }

        return result;
    }
}
