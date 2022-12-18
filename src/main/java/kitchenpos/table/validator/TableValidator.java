package kitchenpos.table.validator;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
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

    public void validateChangeEmpty(OrderTable table, List<Order> orders){
        validateDinning(orders);

        if (table.isGrouping()) {
            throw new IllegalArgumentException();
        }
    }

    public void validateDinning(List<Order> orders){
        if(hasDinningOrder(orders)){
            throw new IllegalArgumentException();
        }
    }

    private boolean hasDinningOrder(List<Order> orders) {
        return orders.stream()
                .map(Order::isDinning)
                .findFirst()
                .isPresent();
    }
}
