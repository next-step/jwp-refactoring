package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuQueryService;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.exception.BadOrderRequestException;
import kitchenpos.order.exception.NotCreatedMenuException;
import kitchenpos.order.exception.OrderErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class OrderValidator {
    private final MenuQueryService menuQueryService;

    public OrderValidator(MenuQueryService menuQueryService) {
        this.menuQueryService = menuQueryService;
    }

    public void validate(OrderRequest orderRequest) {
        checkEmptyOrderLineItems(orderRequest);
        checkCreatedMenu(orderRequest);
    }

    private void checkEmptyOrderLineItems(OrderRequest orderRequest) {
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new BadOrderRequestException(OrderErrorCode.NOT_EXISTS_ORDER_LINE_ITEM);
        }
    }

    private void checkCreatedMenu(OrderRequest orderRequest) {
        List<Menu> menus = menuQueryService.findAllByIdIn(orderRequest.createMenuIds());

        if (isNotCreatedMenu(orderRequest, menus)) {
            throw new NotCreatedMenuException();
        }
    }

    private boolean isNotCreatedMenu(OrderRequest orderRequest, List<Menu> menus) {
        return menus.size() != orderRequest.getOrderLineItems().size();
    }
}
