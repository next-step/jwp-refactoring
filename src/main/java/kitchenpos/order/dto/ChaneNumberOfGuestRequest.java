package kitchenpos.order.dto;

public class ChaneNumberOfGuestRequest {
    private int numberOfRequest;

    protected ChaneNumberOfGuestRequest() {
    }

    public ChaneNumberOfGuestRequest(int numberOfRequest) {
        this.numberOfRequest = numberOfRequest;
    }

    public int getNumberOfRequest() {
        return numberOfRequest;
    }
}
