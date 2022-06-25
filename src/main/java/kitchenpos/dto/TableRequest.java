package kitchenpos.dto;

public class TableRequest {
    private Integer numberOfGuests;
    private Boolean empty;

    protected TableRequest() {
    }

    public TableRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
