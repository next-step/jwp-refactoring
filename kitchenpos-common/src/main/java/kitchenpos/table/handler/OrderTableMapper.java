package kitchenpos.table.handler;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderTableMapper {
    public OrderTable mapFormToOrderTable(OrderTableRequest orderTableRequest) {
        if(orderTableRequest.getNumberOfGuests() < 1){
            throw new IllegalArgumentException("손님 수는 1명 이상이어야합니다.");
        }
        return new OrderTable(orderTableRequest.getNumberOfGuests());
    }
}
