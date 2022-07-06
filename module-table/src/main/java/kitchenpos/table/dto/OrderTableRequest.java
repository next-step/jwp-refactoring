package kitchenpos.table.dto;

import static kitchenpos.common.message.ValidationMessage.NOT_NULL;
import static kitchenpos.common.message.ValidationMessage.POSITIVE_OR_ZERO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import kitchenpos.table.domain.GuestNumber;
import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {
    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    private Integer numberOfGuests;

    @NotNull(message = NOT_NULL)
    private Boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable.Builder()
                .setGuestNumber(GuestNumber.of(numberOfGuests))
                .setEmpty(empty)
                .build();
    }
}
