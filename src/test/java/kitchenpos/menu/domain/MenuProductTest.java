package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.helper.MenuProductFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
@DisplayName("메뉴 상품 관련 Domain 단위 테스트")
class MenuProductTest {

    @DisplayName("금액을 생성한다.")
    @Test
    void calculateAmount() {
        //given
        MenuProduct menuProduct = MenuProductFixtures.양념치킨_메뉴;

        //when
        Amount amount = menuProduct.createAmount();

        //then
        assertThat(amount.getAmount()).isEqualTo(16_000);
    }
}
