package kitchenpos.application.creator;

import kitchenpos.dto.OrderTableDto;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-12
 */
public class OrderTableHelper {

    public static OrderTableDto create(boolean isEmpty) {
        OrderTableDto orderTable = new OrderTableDto();
        orderTable.setEmpty(isEmpty);
        orderTable.setNumberOfGuests(0);
        return orderTable;
    }

}
