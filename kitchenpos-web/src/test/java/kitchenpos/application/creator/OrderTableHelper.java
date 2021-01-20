package kitchenpos.application.creator;

import kitchenpos.dto.OrderTableCreateRequest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-12
 */
public class OrderTableHelper {

    public static OrderTableCreateRequest createRequest(boolean isEmpty) {
        return new OrderTableCreateRequest(null,  0, isEmpty);
    }

}
