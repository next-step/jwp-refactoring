package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuTest {

    private MenuProduct 메뉴_상품;

    @BeforeEach
    void setUp() {
        Product 상품 = new Product("치킨", BigDecimal.valueOf(5000L));
        메뉴_상품 = new MenuProduct(상품, 1L);
    }

    @Test
    void 메뉴_생성() {
        Menu menu = new Menu("치킨", BigDecimal.valueOf(5000L), 1L);
        assertThat(menu).isNotNull();
    }

    @Test
    void 메뉴_가격이_0미만인_경우() {
        assertThatThrownBy(() -> new Menu("치킨", BigDecimal.valueOf(-1L), 1L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 이름이_없는_경우() {
        assertThatThrownBy(() -> new Menu(null, BigDecimal.valueOf(5000L), 1L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 상품_등록() {
        Menu menu = new Menu("치킨", BigDecimal.valueOf(5000L), 1L);

        menu.addMenuProducts(Collections.singletonList(메뉴_상품));

        assertAll(
                () -> assertThat(menu.getMenuProducts()).hasSize(1),
                () -> assertThat(menu.getMenuProducts()).element(0)
                        .satisfies(it -> {
                            assertThat(it.getMenu()).isNotNull();
                        })
        );
    }

    @Test
    void 메뉴_가격이_상품_가격들의_합보다_큰경우() {
        Menu menu = new Menu("치킨", BigDecimal.valueOf(7000L), 1L);

        assertThatThrownBy(() -> menu.addMenuProducts(Collections.singletonList(메뉴_상품)))
                .isInstanceOf(RuntimeException.class);
    }

}