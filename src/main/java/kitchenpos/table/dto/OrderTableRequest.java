package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableRequest {
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }


    public static OrderTable toEntity(OrderTableRequest request) {
        return new OrderTable(request.getNumberOfGuests(), request.isEmpty());
    }

    public static List<OrderTable> toEntity(List<OrderTableRequest> request) {
        return request.stream().map(OrderTableRequest::toEntity).collect(Collectors.toList());
    }
}
