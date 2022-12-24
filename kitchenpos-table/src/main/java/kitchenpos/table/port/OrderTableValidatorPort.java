package kitchenpos.table.port;

import kitchenpos.table.domain.OrderTable;

public interface OrderTableValidatorPort {
    void validChangeEmpty(OrderTable orderTable);
    void validChangeNumberOfGuest(int numberOfGuests);
}
