package kitchenpos.menu.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Menu 테스트")
class MenuTest {
    private Long productId = 1L;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        menuProduct = new MenuProduct(productId, 1L);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        Menu menu = new Menu("메뉴이름", BigDecimal.valueOf(1000L), 1L);
        assertThat(menu).isNotNull();
    }

    @DisplayName("이름없이 메뉴를 생성한다.")
    @Test
    void failureCreateWithEmptyName() {
        assertThatThrownBy(() -> {
            new Menu(null, BigDecimal.valueOf(1000L), 1L);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격인 -1인 메뉴를 생성한다.")
    @Test
    void failureCreateWithNegativePrice() {
        assertThatThrownBy(() -> {
            new Menu("메뉴이름", BigDecimal.valueOf(-1), 1L);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴그룹없이 메뉴를 생성한다.")
    @Test
    void failureCreateWithNullMenuGroupId() {
        assertThatThrownBy(() -> {
            new Menu("메뉴이름", BigDecimal.valueOf(1000L), null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 메뉴 상품들을 추가한다.")
    @Test
    void addAllMenuProducts() {
        Menu menu = new Menu("메뉴이름", BigDecimal.valueOf(1000L), 1L);

        menu.addMenuProduct(Arrays.asList(menuProduct));

        assertAll(
            () -> assertThat(menu.getMenuProducts()).hasSize(1),
            () -> assertThat(menu.getMenuProducts()).element(0)
                .satisfies(it -> {
                    assertThat(it.getMenu()).isNotNull();
                })
        );
    }
}
