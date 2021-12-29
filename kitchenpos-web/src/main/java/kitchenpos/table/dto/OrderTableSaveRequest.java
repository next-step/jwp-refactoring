package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

/**
 * packageName : kitchenpos.dto
 * fileName : OrderTableRequest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class OrderTableSaveRequest {
    private Integer numberOfGuest;
    private boolean empty;

    private OrderTableSaveRequest() {
    }

    private OrderTableSaveRequest(Integer numberOfGuest, boolean empty) {
        this.numberOfGuest = numberOfGuest;
        this.empty = empty;
    }

    public static OrderTableSaveRequest of(Integer numberOfGuest, boolean empty) {
        return new OrderTableSaveRequest(numberOfGuest, empty);
    }

    public Integer getNumberOfGuest() {
        return numberOfGuest;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuest, empty);
    }
}
