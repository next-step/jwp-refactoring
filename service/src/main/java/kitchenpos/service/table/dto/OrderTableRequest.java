package kitchenpos.service.table.dto;

public class OrderTableRequest {
    private Long id;
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Long id, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableRequest(long l) {
        this.id = l;
    }

    public Integer getNumberOfGuests() {
        return this.numberOfGuests;
    }

    public Boolean isEmpty() {
        return this.empty;
    }

    public Long getId() {
        return this.id;
    }
}
