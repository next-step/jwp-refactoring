package kitchenpos.menu.domain;

import static kitchenpos.menu.domain.MenuProductsTest.메뉴_상품_리스트;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴")
class MenuTest {

    @DisplayName("메뉴의 가격은 메뉴 상품의 전체 금액보다 클 수 없다.")
    @Test
    void 가격_검증() {
        MenuProducts menuProducts = new MenuProducts(메뉴_상품_리스트);

        assertThatThrownBy(() -> new Menu.Builder()
                .name("비싼 메뉴")
                .price(Price.from(100000))
                .menuProducts(menuProducts)
                .build()).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성")
    @Nested
    class 생성 {
        @DisplayName("이름, 가격, 메뉴 그룹을 지정하면 생성할 수 있다.")
        @Test
        void 생성_성공() {
            Menu menu = new Menu.Builder()
                    .name("메뉴")
                    .price(Price.from(0))
                    .menuGroup(new MenuGroup("메뉴 그룹"))
                    .build();

            assertThat(menu).isNotNull();
        }

        @DisplayName("이름이 NULL이면 생성할 수 없습니다.")
        @Test
        void 이름이_NULL() {
            assertThatThrownBy(() -> new Menu.Builder()
                    .price(Price.from(1000))
                    .menuGroup(new MenuGroup("메뉴 그룹"))
                    .build()).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격름이 NULL이면 생성할 수 없습니다.")
        @Test
        void 가격이_NULL() {
            assertThatThrownBy(() -> new Menu.Builder()
                    .price(Price.from(1000))
                    .menuGroup(new MenuGroup("메뉴 그룹"))
                    .build()).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 그룹이 NULL이면 생성할 수 없습니다.")
        @Test
        void 메뉴_그룹이_NULL() {
            assertThatThrownBy(() -> new Menu.Builder()
                    .name("메뉴")
                    .price(Price.from(1000))
                    .build()).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
