package kitchenpos.dto.ordertable;

import com.fasterxml.jackson.annotation.JsonInclude;
import kitchenpos.domain.OrderTable;

public class OrderTableRequest {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer numberOfGuests;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean empty;

    protected OrderTableRequest() {
    }

    private OrderTableRequest(final Integer numberOfGuests, final Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableRequest of(final Integer numberOfGuests, final Boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public static OrderTableRequest from(final Boolean empty) {
        return new OrderTableRequest(null, empty);
    }

    public static OrderTableRequest from(final Integer numberOfGuests) {
        return new OrderTableRequest(numberOfGuests, null);
    }

    public OrderTable toOrderTable() {
        return OrderTable.of(numberOfGuests, empty);
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean isEmpty() {
        return empty;
    }
}
