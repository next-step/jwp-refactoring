package kitchenpos.dto.request;

import java.util.Objects;
import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.domain.OrderTable;

public class OrderTableRequest {
    private Long id;
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableRequest(Long id, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableRequest of(OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public Long getId() {
        return id;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void validateNumberOfGuests() {
        if (Objects.isNull(numberOfGuests) || numberOfGuests < 0) {
            throw new OrderTableException("게스트수는 0미만일 수 없습니다", numberOfGuests);
        }
    }

    public Boolean getEmpty() {
        return empty;
    }
}
