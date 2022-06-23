package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.MenuEntity;
import kitchenpos.menu.domain.MenuProductEntity;
import kitchenpos.menu.domain.ProductEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴에 대한 단위 테스트")
class MenuTest {

    @DisplayName("메뉴에 메뉴상품을 등록하면 메뉴상품의 값이 메뉴로 매핑된다")
    @Test
    void mapping_test() {
        // given
        ProductEntity 상품 = ProductEntity.of("test", BigDecimal.valueOf(500L));
        MenuProductEntity 메뉴_상품 = MenuProductEntity.of(null, 상품, 3);
        MenuProductEntity 메뉴_상품2 = MenuProductEntity.of(null, 상품, 2);
        MenuEntity 메뉴 = MenuEntity.of("menu", BigDecimal.valueOf(500L), null);

        // when
        메뉴.registerMenuProducts(Arrays.asList(메뉴_상품, 메뉴_상품2));

        // then
        assertThat(메뉴_상품.getMenu()).isEqualTo(메뉴);
        assertThat(메뉴_상품2.getMenu()).isEqualTo(메뉴);
    }
}
