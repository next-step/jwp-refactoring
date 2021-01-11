package kitchenpos.application.creator;

import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-12
 */
public class TableGroupHelper {

    public static TableGroup create(OrderTable ...orderTable) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable));
        return tableGroup;
    }

}
