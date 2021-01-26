package kitchenpos.support;

import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class OrderSupportDefault implements OrderSupport {
    @Override
    public boolean isUsingTable(OrderTable table) {
        return false;
    }

    @Override
    public boolean isUsingTables(Collection<OrderTable> orderTables) {
        return false;
    }
}
