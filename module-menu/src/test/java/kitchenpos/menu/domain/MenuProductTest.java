package kitchenpos.menu.domain;

import static kitchenpos.helper.ProductFixtures.양념치킨_상품;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
@DisplayName("메뉴 상품 관련 Domain 단위 테스트")
class MenuProductTest {

    @DisplayName("금액을 생성한다.")
    @Test
    void getAmount() {
        //given
        MenuProduct menuProduct = MenuProductFixtures.양념치킨_메뉴상품;

        //when
        Price amount = menuProduct.getTotalPrice(양념치킨_상품);

        //then
        assertThat(amount.getPrice()).isEqualTo(16_000);
    }
}
