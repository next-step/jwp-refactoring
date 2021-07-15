package kitchenpos.table.util;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;

public class OrderTableMapper {
    public OrderTable mapFormToOrderTable(OrderTableRequest orderTableRequest) {
        if(orderTableRequest.getNumberOfGuests() < 1){
            throw new IllegalArgumentException("손님 수는 1명 이상이어야합니다.");
        }
        return new OrderTable(orderTableRequest.getNumberOfGuests());
    }
}
