package kitchenpos.table.dto;

public class UpdateNumberOfGuestsDto {
    private Integer numberOfGuests;

    public UpdateNumberOfGuestsDto() { }

    public UpdateNumberOfGuestsDto(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
