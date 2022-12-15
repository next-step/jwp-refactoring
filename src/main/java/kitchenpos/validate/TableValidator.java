package kitchenpos.validate;

import java.util.Objects;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {

    public void validateChangeNumberOfGuests(OrderTable table, int numberOfGuests){
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        if (table.isEmpty()) {
            throw new IllegalArgumentException();
        }

    }

    public void validateChangeEmpty(OrderTable table){
        if (Objects.nonNull(table.getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }
}
