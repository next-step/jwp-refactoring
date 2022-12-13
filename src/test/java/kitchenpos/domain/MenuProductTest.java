package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.ProductTestFixture.단무지_요청;
import static kitchenpos.fixture.ProductTestFixture.상품생성;
import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    @Test
    void of() {
        MenuProduct menuProduct = MenuProduct.of(상품생성(단무지_요청()), 1L);

        assertThat(menuProduct).isNotNull();
    }
}
