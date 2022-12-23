package kitchenpos;

import kitchenpos.domain.OrderTable;

public class OrderTableBuilder {

    public static OrderTable emptyOrderTableWithIdAndGuestNo(Long id, int numberOfGuests) {
        return new OrderTable(id, null, numberOfGuests, true);
    }

    public static OrderTable emptyOrderTableWithGuestNo(int numberOfGuests) {
        return new OrderTable(null, null, numberOfGuests, true);
    }

    public static OrderTable nonEmptyOrderTableWithIdAndGuestNo(Long id, int numberOfGuests) {
        return new OrderTable(id, null, numberOfGuests, false);
    }

    public static OrderTable nonEmptyOrderTableWithGuestNo(int numberOfGuests) {
        return new OrderTable(null, null, numberOfGuests, false);
    }

}
