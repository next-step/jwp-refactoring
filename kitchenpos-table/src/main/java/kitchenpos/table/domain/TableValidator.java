package kitchenpos.table.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableValidator {
    private static final int MIN_NUMBER_OF_GUESTS = 0;
    private static final String ERROR_MESSAGE_INVALID_NUMBER_OF_GUESTS = "방문객 수는 " + MIN_NUMBER_OF_GUESTS + " 명 이상이어야 합니다.";

    public void validateOrderTableEqualsSize(List<OrderTable> savedOrderTables, List<Long> orderTableIds) {
        if (savedOrderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }
    }

    public void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_NUMBER_OF_GUESTS);
        }
    }
}
