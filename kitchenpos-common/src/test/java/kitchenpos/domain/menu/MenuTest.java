package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.domain.common.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴의 가격은 담품 상품의 가격의 합보다 비싼 경우 예외 발생")
    @Test
    void 메뉴_가격_예외() {
        // given
        Menu 후라이드_치킨 = Menu.of("후라이드 치킨 + 양념 치킨", 35000, 1L);
        Price 단품_가격의_합 = Price.of(33000);

        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> 후라이드_치킨.validateMenuPrice(단품_가격의_합)
            )
            .withMessageContaining("메뉴의 가격이 단품의 합보다 비쌉니다.");
    }

}