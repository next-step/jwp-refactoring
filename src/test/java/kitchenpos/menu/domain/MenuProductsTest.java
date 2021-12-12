package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Collections;
import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품들")
class MenuProductsTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> MenuProducts.singleton(
                MenuProduct.of(1L, Quantity.from(1L))
            ));
    }

    @Test
    @DisplayName("메뉴 상품 리스트 필수")
    void instance_nullMenuProducts_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> MenuProducts.from(null))
            .withMessage("메뉴 상품 리스트는 필수입니다.");
    }

    @Test
    @DisplayName("메뉴 상품 리스트에 null이 포함될 수 없음")
    void instance_containNull_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> MenuProducts.from(Collections.singletonList(null)))
            .withMessageEndingWith("null이 포함될 수 없습니다.");
    }
}
