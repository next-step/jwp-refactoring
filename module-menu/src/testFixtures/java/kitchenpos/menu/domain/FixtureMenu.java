package kitchenpos.menu.domain;

import java.math.BigDecimal;

public class FixtureMenu extends Menu {

    public FixtureMenu(String name) {
        super(1L, name, BigDecimal.valueOf(15_000), 1L);
    }
}
