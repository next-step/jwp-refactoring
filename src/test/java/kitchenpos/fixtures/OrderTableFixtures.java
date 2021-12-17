package kitchenpos.fixtures;

import kitchenpos.domain.OrderTable;
import org.assertj.core.util.Lists;

import java.util.List;

/**
 * packageName : kitchenpos.fixtures
 * fileName : TableFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class OrderTableFixtures {
    public static OrderTable createOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static List<OrderTable> createOrderTables(OrderTable... orderTables) {
        return Lists.newArrayList(orderTables);
    }
}
