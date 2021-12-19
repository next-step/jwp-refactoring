package kitchenpos.common.domain.fixture;

import kitchenpos.common.domain.Quantity;

public class QuantityFixture {
    private QuantityFixture() {
        throw new UnsupportedOperationException();
    }

    public static Quantity create(Long quantity) {
        return Quantity.of(quantity);
    }
}
