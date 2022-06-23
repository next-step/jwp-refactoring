package kitchenpos.order.domain;

import kitchenpos.core.domain.DomainService;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.exception.EmptyOrderLineItemsException;
import kitchenpos.order.exception.InvalidOrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableDomainService;
import org.springframework.util.CollectionUtils;

import java.util.List;

@DomainService
public class OrderCreatingValidator {

    private final MenuRepository menuRepository;
    private final TableDomainService tableDomainService;

    public OrderCreatingValidator(MenuRepository menuRepository,
                                  TableDomainService tableDomainService) {
        this.menuRepository = menuRepository;
        this.tableDomainService = tableDomainService;
    }

    public void validate(OrderRequest request) {
        List<OrderLineItemRequest> orderLineItems = request.getOrderLineItems();
        List<Long> menuIds = request.toMenuIds();
        validateNotEmptyOrderLineItems(orderLineItems);
        validateExistsAllMenus(menuIds);
        validateNotEmptyOrderTable(request.getOrderTableId());
    }

    private void validateNotEmptyOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new EmptyOrderLineItemsException();
        }
    }

    private void validateExistsAllMenus(List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new InvalidOrderException("존재하지 않는 메뉴가 있습니다.");
        }
    }

    private void validateNotEmptyOrderTable(Long orderTableId) {
        OrderTable orderTable = tableDomainService.findById(orderTableId);
        if (orderTable.isEmpty()) {
            throw new InvalidOrderException("빈 테이블 입니다.");
        }
    }
}
