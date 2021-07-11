package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.exception.NoSuchMemuListException;
import kitchenpos.exception.NotAvaliableTableException;
import kitchenpos.menu.Menu;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.OrderTable;

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
