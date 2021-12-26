package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

import javax.validation.constraints.Positive;
import java.util.Objects;

public class OrderTableRequest {

    private Long id;

    @Positive
    private int numberOfGuests;

    private Boolean empty;

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public OrderTable toEntity() {
        if(Objects.isNull(empty)) {
            return OrderTable.create(numberOfGuests);
        }
        return OrderTable.create(numberOfGuests, empty);
    }
}
