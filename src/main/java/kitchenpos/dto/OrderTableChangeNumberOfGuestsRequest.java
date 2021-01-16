package kitchenpos.dto;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-16
 */
public class OrderTableChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    protected OrderTableChangeNumberOfGuestsRequest() {
    }

    public OrderTableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
