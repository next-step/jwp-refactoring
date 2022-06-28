package kitchenpos.fixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;

public class TestOrderTableRequestFactory {
    public static OrderTableRequest create(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }
}
