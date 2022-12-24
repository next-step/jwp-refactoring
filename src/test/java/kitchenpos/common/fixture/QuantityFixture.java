package kitchenpos.common.fixture;

import kitchenpos.common.vo.Quantity;

public class QuantityFixture {

    public static Quantity quantityMenuProductA() {
        return new Quantity(1);
    }

    public static Quantity quantityOrderLineItemA() {
        return new Quantity(1);
    }
}
