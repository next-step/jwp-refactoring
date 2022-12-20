package kitchenpos.menu.domain;

import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품 테스트")
public class MenuProductTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        final long 수량 = 1;
        //when, then:
        assertThat(MenuProduct.of(1L, 수량)).isEqualTo(MenuProduct.of(1L, 수량));
    }
}
