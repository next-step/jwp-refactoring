package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.exception.NoSuchMemuListException;
import kitchenpos.order.exception.NotAvaliableTableException;
import kitchenpos.table.domain.OrderTable;

@Component
public class OrderValidator {
    public void createValidator(OrderRequest orderRequest, OrderTable orderTable, List<Menu> menuList) {
        if (orderRequest.getOrderLineItemsMenuIds().size() != menuList.size()) {
            throw new NoSuchMemuListException();
        }

        if (orderTable.isEmpty()) {
            throw new NotAvaliableTableException();
        }
    }
}
