package kitchenpos.table.dto;

/**
 * packageName : kitchenpos.dto
 * fileName : ChangeNumberRequest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class ChangeGuestNumberRequest {
    private Integer numberOfGuests;

    private ChangeGuestNumberRequest() {
    }

    private ChangeGuestNumberRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static ChangeGuestNumberRequest of(Integer numberOfGuests) {
        return new ChangeGuestNumberRequest(numberOfGuests);
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
