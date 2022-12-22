package kitchenpos;

import kitchenpos.domain.OrderTable;

public class OrderTableBuilder {

    public static OrderTable emptyOrderTableWithGuestNo(Long id, int numberOfGuests) {
        return new OrderTable(id, numberOfGuests, true);
    }

    public static OrderTable emptyOrderTableWithGuestNo(int numberOfGuests) {
        return new OrderTable(numberOfGuests, true);
    }

    public static OrderTable nonEmptyOrderTableWithGuestNo(Long id, int numberOfGuests) {
        return new OrderTable(id, numberOfGuests, false);
    }

    public static OrderTable nonEmptyOrderTableWithGuestNo(int numberOfGuests) {
        return new OrderTable(numberOfGuests, false);
    }

}
