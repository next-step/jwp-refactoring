package kitchenpos.table.validator;

import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {

    private final TableOrderValidator tableOrderValidator;

    public TableValidator(TableOrderValidator tableOrderValidator) {
        this.tableOrderValidator = tableOrderValidator;
    }

    public void validateChangeNumberOfGuests(OrderTable table, int numberOfGuests){
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        if (table.isEmpty()) {
            throw new IllegalArgumentException();
        }

    }

    public void validateChangeEmpty(OrderTable table){
        if(tableOrderValidator.existsDinningOrder(table.getId())){
            throw new IllegalArgumentException();
        }

        if (table.isGrouping()) {
            throw new IllegalArgumentException();
        }
    }


}
