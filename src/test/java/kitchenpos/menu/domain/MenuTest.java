package kitchenpos.menu.domain;

import kitchenpos.price.domain.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Menu 클래스 테스트")
class MenuTest {

    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        Product product = new Product("강정치킨", BigDecimal.valueOf(15_000L));
        menuProduct = new MenuProduct(product, new Quantity(1L));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        Menu menu = new Menu("강정치킨", BigDecimal.valueOf(15_000L), 1L);
        assertThat(menu).isNotNull();
    }

    @DisplayName("이름없이 메뉴를 생성한다.")
    @Test
    void failureCreateWithEmptyName() {
        assertThatThrownBy(() -> {
            new Menu(null, BigDecimal.valueOf(15_000L), 1L);
        }).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("가격인 -1인 메뉴를 생성한다.")
    @Test
    void failureCreateWithNegativePrice() {
        assertThatThrownBy(() -> {
            new Menu("강정치킨", BigDecimal.valueOf(-1), 1L);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴그룹없이 메뉴를 생성한다.")
    @Test
    void failureCreateWithNullMenuGroupId() {
        assertThatThrownBy(() -> {
            new Menu("강정치킨", BigDecimal.valueOf(15_000L), null);
        }).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("메뉴에 메뉴 상품들을 추가한다.")
    @Test
    void addAllMenuProducts() {
        Menu menu = new Menu("강정치킨", BigDecimal.valueOf(15_000L), 1L);

        menu.addMenuProducts(Arrays.asList(menuProduct));

        assertAll(
                () -> assertThat(menu.getMenuProducts()).hasSize(1),
                () -> assertThat(menu.getMenuProducts()).element(0)
                                                        .satisfies(it -> {
                                                            assertThat(it.getMenu()).isNotNull();
                                                        })
        );
    }

    @DisplayName("메뉴에 메뉴 가격이 상품들의 금액의 합보다 큰 메뉴 상품들을 추가한다.")
    @Test
    void addAllMenuProductsThrownException() {
        Menu menu = new Menu("강정치킨", BigDecimal.valueOf(16_000L), 1L);

        assertThatThrownBy(() -> {
            menu.addMenuProducts(Arrays.asList(menuProduct));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("상품들 금액의 합이 메뉴 가격보다 클 수 없습니다.");
    }
}
