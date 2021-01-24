package kitchenpos.dto;

import kitchenpos.domain.order.OrderTable;

import javax.validation.constraints.NotNull;

public class OrderTableRequest {
    @NotNull
    private int numberOfGuests;
    @NotNull
    private boolean empty;

    protected OrderTableRequest() {}

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTableRequestBuilder builder() {
        return new OrderTableRequestBuilder();
    }

    public static final class OrderTableRequestBuilder {
        private int numberOfGuests;
        private boolean empty;

        private OrderTableRequestBuilder() {}

        public OrderTableRequestBuilder numberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public OrderTableRequestBuilder empty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTableRequest build() {
            return new OrderTableRequest(numberOfGuests, empty);
        }
    }
}
