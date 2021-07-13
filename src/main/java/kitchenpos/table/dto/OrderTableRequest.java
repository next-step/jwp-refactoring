package kitchenpos.table.dto;

public class OrderTableRequest {
    private Long id;
    private int numberOfGuests;

    protected OrderTableRequest() {
    }

    public OrderTableRequest(Long id, int numberOfGuests) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTableRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return this.numberOfGuests;
    }

    public Long getId(){
        return this.id;
    }
}
