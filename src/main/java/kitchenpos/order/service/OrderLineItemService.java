package kitchenpos.order.service;

import kitchenpos.menu.service.MenuService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.dto.OrderLineRequest;
import kitchenpos.order.dto.OrderLineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderLineItemService {
    private final OrderLineItemRepository orderLineItemRepository;
    private final MenuService menuService;

    public OrderLineItemService(OrderLineItemRepository orderLineItemRepository, MenuService menuService) {
        this.orderLineItemRepository = orderLineItemRepository;
        this.menuService = menuService;
    }

    public List<OrderLineResponse> saveLineItems(Order savedOrder, List<OrderLineRequest> orderLineItems) {
        List<OrderLineItem> lineItems = orderLineItems.stream()
                .map(request -> new OrderLineItem(savedOrder, menuService.findById(request.getMenuId()), request.ofQuantity()))
                .collect(Collectors.toList());
        orderLineItemRepository.saveAll(lineItems);

        return lineItems.stream().map(OrderLineResponse::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderLineResponse> list(Order findOrder) {
        return orderLineItemRepository
                .findAllByOrder(findOrder)
                .stream()
                .map(OrderLineResponse::of)
                .collect(Collectors.toList());
    }
}
