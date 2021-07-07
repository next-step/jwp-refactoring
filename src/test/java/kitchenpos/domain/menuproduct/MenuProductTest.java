package kitchenpos.domain.menuproduct;

import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;
import kitchenpos.fixture.CleanUp;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {
    @BeforeEach
    void setUp() {
        CleanUp.cleanUp();
    }

    @Test
    void getAmount() {
        MenuProduct menuProduct = new MenuProduct(null, ProductFixture.양념치킨_1000원, new Quantity(10));

        assertThat(menuProduct.getAmount()).isEqualTo(new Price(10000));
    }
}