package kitchenpos.table.dto;

/**
 * packageName : kitchenpos.dto
 * fileName : ChangeNumberRequest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
//FIXME 생성자 제한하기
public class ChangeGuestNumberRequest {
    private Integer numberOfGuests;

    public ChangeGuestNumberRequest() {
    }

    public ChangeGuestNumberRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
