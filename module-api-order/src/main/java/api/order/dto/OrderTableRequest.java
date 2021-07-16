package api.order.dto;

public class OrderTableRequest {
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableRequest() {
    }

    private OrderTableRequest(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableRequest of(Long id, int numberOfGuests, boolean empty) {
        return new OrderTableRequest(id, numberOfGuests, empty);
    }

    public static OrderTableRequest of(boolean empty) {
        return new OrderTableRequest(null, 0, empty);
    }

    public static OrderTableRequest of(int numberOfGuests) {
        return new OrderTableRequest(null, numberOfGuests, false);
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
