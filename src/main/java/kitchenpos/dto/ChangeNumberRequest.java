package kitchenpos.dto;

/**
 * packageName : kitchenpos.dto
 * fileName : ChangeNumberRequest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class ChangeNumberRequest {
    private Integer numberOfGuests;

    public ChangeNumberRequest() {
    }

    public ChangeNumberRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
