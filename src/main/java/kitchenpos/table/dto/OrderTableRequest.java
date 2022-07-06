package kitchenpos.table.dto;

public class OrderTableRequest {
    private Long id;
    private Integer numberOfGuests;
    private Boolean empty;

    private OrderTableRequest() {
    }

    private OrderTableRequest(Boolean empty) {
        this(null, empty);
    }

    private OrderTableRequest(Integer numberOfGuests) {
        this(numberOfGuests, null);
    }

    private OrderTableRequest(Integer numberOfGuests, Boolean empty) {
        this(null, numberOfGuests, empty);
    }

    private OrderTableRequest(Long id, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableRequest of(Boolean empty) {
        return new OrderTableRequest(empty);
    }

    public static OrderTableRequest of(Integer numberOfGuests) {
        return new OrderTableRequest(numberOfGuests);
    }

    public static OrderTableRequest of(Integer numberOfGuests, Boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public static OrderTableRequest of(Long id, Integer numberOfGuests, Boolean empty) {
        return new OrderTableRequest(id, numberOfGuests, empty);
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
