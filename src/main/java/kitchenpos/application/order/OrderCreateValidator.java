package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.dto.order.CreateOrderTableItemRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderCreateValidator {

    public static final String NOT_EXIST_MENU_CONTAINS_ERROR_MESSAGE = "존재하지 않는 메뉴입니다.";

    private final MenuRepository menuRepository;

    public OrderCreateValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<OrderLineItem> validateExistsMenusAndCreate(List<CreateOrderTableItemRequest> orderLineItemRequests) {
        List<Menu> findMenus = menuRepository.findAllById(extractMenuIds(orderLineItemRequests));
        if (findMenus.size() != orderLineItemRequests.size()) {
            throw new IllegalArgumentException(NOT_EXIST_MENU_CONTAINS_ERROR_MESSAGE);
        }
        return joinToMenuProduct(findMenus, orderLineItemRequests);
    }

    private List<Long> extractMenuIds(List<CreateOrderTableItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
            .map(CreateOrderTableItemRequest::getMenuId)
            .collect(Collectors.toList());
    }

    private List<OrderLineItem> joinToMenuProduct(List<Menu> findMenus, List<CreateOrderTableItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
            .flatMap(orderLineItemRequest -> findMenus.stream()
                .map(menu -> new OrderLineItem(menu, orderLineItemRequest.getQuantity())))
            .filter(mappedOrderLineItem -> mappedOrderLineItem.getMenu().getId().equals(mappedOrderLineItem.getMenuId()))
            .distinct()
            .collect(Collectors.toList());
    }
}
