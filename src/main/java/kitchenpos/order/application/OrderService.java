package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final TableService tableService;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final TableService tableService,
            final OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.tableService = tableService;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final OrderTable orderTable = tableService.findById(request.getOrderTableId());
        
        List<OrderLineItem> orderLineItems = createOrderLineItems(request.getOrderLineItems());
        
        Order order = Order.createOrder(orderTable, orderLineItems);
        
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
        
        return OrderResponse.from(order);
    }
    
    @Transactional
    public OrderResponse completed(final Long orderId) {
        final Order order = findById(orderId);
        
        order.completed();
        
        return OrderResponse.from(order);
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
        orderValidator.checkMenu(menuIds);
        
        for (OrderLineItemRequest orderLineItemRequest : request) {
            result.add(OrderLineItem.of(orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity()));
        }

        return result;
    }
}
