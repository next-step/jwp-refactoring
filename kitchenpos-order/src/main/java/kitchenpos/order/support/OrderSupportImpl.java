package kitchenpos.order.support;

import kitchenpos.support.OrderSupport;
import kitchenpos.table.domain.OrderTable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Primary
@Component
public class OrderSupportImpl implements OrderSupport {
    @Override
    public boolean isUsingTable(OrderTable table) {
        return false;
    }

    @Override
    public boolean isUsingTables(Collection<OrderTable> orderTables) {
        return false;
    }
}
