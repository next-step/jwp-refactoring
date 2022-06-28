package kitchenpos.order.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import kitchenpos.table.domain.GuestNumber;
import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {
    @PositiveOrZero
    private Integer numberOfGuests;

    @NotNull
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
