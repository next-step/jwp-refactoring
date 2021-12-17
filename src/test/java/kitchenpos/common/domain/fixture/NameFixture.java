package kitchenpos.common.domain.fixture;

import kitchenpos.common.domain.Name;

public class NameFixture {
    private NameFixture() {
        throw new UnsupportedOperationException();
    }

    public static Name of(String name) {
        return Name.of(name);
    }
}
