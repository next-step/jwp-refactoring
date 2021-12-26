package kitchenpos.order.dto;

public class OrderTableRequest {

    private Long id;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableRequest() {
    }

    private OrderTableRequest(Long id) {
        this.id = id;
    }

    private OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableRequest from(Long id) {
        return new OrderTableRequest(id);
    }

    public static OrderTableRequest of(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
