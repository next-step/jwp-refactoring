package kitchenpos.table.dto;

public class UpdateNumberOfGuestsRequest {
    private Integer numberOfGuests;

    public UpdateNumberOfGuestsRequest() { }

    public UpdateNumberOfGuestsRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
