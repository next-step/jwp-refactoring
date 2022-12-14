package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품 테스트")
public class MenuProductTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        final long 상품_id = 1;
        final long 수량 = 1;
        assertThat(MenuProduct.of(상품_id,수량)).isEqualTo(MenuProduct.of(상품_id,수량));
    }

    public static MenuProduct 메뉴_상품(Long productId, long quantity) {
        return MenuProduct.of(productId, quantity);
    }
}
