package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
public class OrderValidator {

    private final MenuRepository menuRepository;

    public OrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validate(OrderRequest orderRequest) {
        validateOrderLineItems(orderRequest.getOrderLineItemRequests());

    }

    private void validateOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        List<Long> menuIds = getMenuIds(orderLineItemRequests);

        if (menuRepository.countByIdIn(menuIds) != orderLineItemRequests.size()) {
            throw new IllegalArgumentException("주문항목에는 등록된 메뉴만 존재해야합니다.");
        }
    }

    private List<Long> getMenuIds(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

}
