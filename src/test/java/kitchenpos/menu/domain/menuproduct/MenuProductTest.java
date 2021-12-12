package kitchenpos.menu.domain.menuproduct;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.MenuDomainFixture.후라이드_치킨;
import static kitchenpos.fixture.MenuProductDomainFixture.menuProduct;
import static kitchenpos.fixture.ProductDomainFixture.후라이드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 상품 관리")
class MenuProductTest {

    @Nested
    @DisplayName("메뉴 상품 생성")
    class CreateMenuProduct {

        @Test
        @DisplayName("성공")
        public void create() {
            // given
            final int quantity = 1;

            // when
            final MenuProduct actual = menuProduct(후라이드_치킨, 후라이드, quantity);

            // then
            assertAll(
                    () -> assertThat(actual.getMenu()).isEqualTo(후라이드_치킨),
                    () -> assertThat(actual.getProduct()).isEqualTo(후라이드),
                    () -> assertThat(actual.getQuantity()).isEqualTo(quantity)
            );
        }

        @Test
        @DisplayName("실패 - 메뉴 없음")
        public void failMenuEmpty() {
            // given
            final int quantity = 1;

            // when
            Assertions.assertThatThrownBy(() -> {
                MenuProduct actual = menuProduct(null, 후라이드, quantity);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("실패 - 상품 없음")
        public void failProductEmpty() {
            // given
            final int quantity = 1;

            // when
            Assertions.assertThatThrownBy(() -> {
                MenuProduct actual = menuProduct(후라이드_치킨, null, quantity);
            }).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
