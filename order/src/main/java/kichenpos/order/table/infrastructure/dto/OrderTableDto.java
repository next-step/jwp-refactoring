package kichenpos.order.table.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class OrderTableDto {

    private final boolean empty;

    @JsonCreator
    public OrderTableDto(@JsonProperty("empty") boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
