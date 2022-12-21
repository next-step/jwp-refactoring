package kitchenpos.application;

import kitchenpos.domain.OrderTable;

class OrderTableBuilder {

    static OrderTable emptyOrderTableWithGuestNo(int numberOfGuests) {
        return new OrderTable(numberOfGuests, true);
    }

    static OrderTable nonEmptyOrderTableWithGuestNo(int numberOfGuests) {
        return new OrderTable(numberOfGuests, false);
    }

}
