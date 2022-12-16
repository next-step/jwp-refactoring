package kitchenpos.order.validator;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.persistence.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.persistence.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository,MenuRepository menuRepository){
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validateOrderCreate(OrderRequest orderRequest) {
        validateOrderTable(orderRequest.getOrderTableId());
        validateMenus(orderRequest.findMenuIds());
        validateOrderLineItems(orderRequest.getOrderLineItems());
    }

    private void validateOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        if(CollectionUtils.isEmpty(orderLineItems)){
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if(orderTable.isEmpty()){
            throw new IllegalArgumentException();
        }
    }

    private void validateMenus(List<Long> menuIds) {
        List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException();
        }
    }
}
