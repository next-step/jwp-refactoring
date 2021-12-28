package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class OrderTableRequest {

    @Positive
    @NotNull
    private Long id;

    @Positive
    @NotNull
    private Integer numberOfGuests;

    private Boolean empty;

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public OrderTable toEntity() {
        if(Objects.isNull(empty)) {
            return OrderTable.setting(numberOfGuests);
        }
        return OrderTable.setting(numberOfGuests, empty);
    }
}
