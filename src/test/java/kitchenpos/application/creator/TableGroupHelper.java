package kitchenpos.application.creator;

import java.util.Arrays;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.dto.TableGroupDto;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-12
 */
public class TableGroupHelper {

    public static TableGroupDto create(OrderTableDto...orderTable) {
        TableGroupDto tableGroup = new TableGroupDto();
        tableGroup.setOrderTables(Arrays.asList(orderTable));
        return tableGroup;
    }

}
