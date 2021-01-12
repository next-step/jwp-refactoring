package kitchenpos.application.creator;

import kitchenpos.domain.OrderTable;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-12
 */
public class OrderTableHelper {

    public static OrderTable create(boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(isEmpty);
        orderTable.setNumberOfGuests(0);
        return orderTable;
    }

}
