package kitchenpos.support;

import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public interface OrderSupport {
    default boolean isUsingTable(OrderTable table) {
        return false;
    }

    default boolean isUsingTables(Collection<OrderTable> orderTables) {
        return false;
    }
}
