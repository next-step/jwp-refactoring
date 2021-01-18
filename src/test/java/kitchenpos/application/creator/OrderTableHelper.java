package kitchenpos.application.creator;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableRequest;

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
