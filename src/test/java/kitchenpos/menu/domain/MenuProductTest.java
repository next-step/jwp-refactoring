package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품 테스트")
public class MenuProductTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        final Long 상품_id = 1L;
        final long 수량 = 1;
        //when, then:
        assertThat(MenuProduct.of(1L, 수량)).isEqualTo(MenuProduct.of(1L, 수량));
    }

    public static MenuProduct 메뉴_상품(Product product, long quantity) {
        return MenuProduct.of(product.getId(), quantity);
    }
}
