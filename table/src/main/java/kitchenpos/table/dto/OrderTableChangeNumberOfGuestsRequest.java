package kitchenpos.table.dto;

public class OrderTableChangeNumberOfGuestsRequest {
    private Integer numberOfGuests;

    protected OrderTableChangeNumberOfGuestsRequest() {
    }

    public OrderTableChangeNumberOfGuestsRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static OrderTableChangeNumberOfGuestsRequest of(int numberOfGuests) {
        return new OrderTableChangeNumberOfGuestsRequest(numberOfGuests);
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
