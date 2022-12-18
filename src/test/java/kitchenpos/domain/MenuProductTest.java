package kitchenpos.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.MenuProductTestFixture.짜장면메뉴상품;
import static kitchenpos.fixture.ProductTestFixture.단무지요청;
import static kitchenpos.fixture.ProductTestFixture.상품생성;
import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    @DisplayName("메뉴상품 생성 작업을 성공한다.")
    @Test
    void of() {
        // when
        MenuProduct menuProduct = MenuProduct.of(상품생성(단무지요청()), 1L);

        // then
        assertThat(menuProduct).isNotNull();
    }

    @DisplayName("메뉴상품의 모든 가격의 총합계산 작업을 성공한다.")
    @Test
    void totalPrice() {
        // given
        MenuProduct 짜장면메뉴상품 = 짜장면메뉴상품();
        Price price = 짜장면메뉴상품.getProduct().getPrice();
        Quantity quantity = Quantity.from(짜장면메뉴상품.getQuantity());

        // when & then
        assertThat(짜장면메뉴상품.totalPrice()).isEqualTo(price.multiply(quantity));
    }
}
