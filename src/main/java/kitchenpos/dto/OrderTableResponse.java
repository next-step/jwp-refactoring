package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableResponse {
    private Long id;
    private Integer numberOfGuests;
    private Boolean empty;

    public static OrderTableResponse of(OrderTable orderTable) {
        OrderTableResponse response = new OrderTableResponse();
        response.id = orderTable.getId();
        response.numberOfGuests = orderTable.getNumberOfGuests();
        response.empty = orderTable.isEmpty();
        return response;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
