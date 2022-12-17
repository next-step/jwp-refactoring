package kitchenpos.dto;

public class ChaneNumberOfGuestRequest {
    private int numberOfRequest;

    public ChaneNumberOfGuestRequest(int numberOfRequest) {
        this.numberOfRequest = numberOfRequest;
    }

    public int getNumberOfRequest() {
        return numberOfRequest;
    }
}
