package kitchenpos.table.dto;

public class OrderTableRequest {
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Boolean empty) {
        this.empty = empty;
    }

    public OrderTableRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTableRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean isEmpty() {
        return empty;
    }
}
