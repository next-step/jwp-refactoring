package kitchenpos.dto;

public class TableRequest {
    private Boolean empty;
    private Integer numberOfGuests;

    protected TableRequest() {
    }

    public TableRequest(Boolean empty, Integer numberOfGuests) {
        this.empty = empty;
        this.numberOfGuests = numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
