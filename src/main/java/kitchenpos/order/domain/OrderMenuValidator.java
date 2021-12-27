package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.exception.DuplicateMenuInOrderLineItems;
import org.springframework.stereotype.Component;

@Component
public class OrderMenuValidator {

    private final MenuRepository menuRepository;

    public OrderMenuValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validateOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        List<Long> menuIds = getMenuIds(orderLineItemRequests);
        long countOfMatchMenuIds = menuRepository.countByIdIn(menuIds);
        if (menuIds.size() != countOfMatchMenuIds) {
            throw new DuplicateMenuInOrderLineItems();
        }
    }

    private List<Long> getMenuIds(List<OrderLineItemRequest> requestOrderLineItems) {
        return requestOrderLineItems.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());
    }
}
