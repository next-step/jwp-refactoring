package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    private Menu 나홀로셋트;

    @BeforeEach
    void setUp() {
        나홀로셋트 = new Menu("나홀로셋트", Money.price(25000L), new MenuGroup("중국집"));
    }

    @DisplayName("메뉴에 상품을 여러개 등록할 수 있다.")
    @Test
    void createMenu() {
        Product 짜장면 = new Product("짜장면", Money.price(6000L));
        Product 짬뽕 = new Product("짬뽕", Money.price(7000L));
        Product 탕수육 = new Product("탕수육", Money.price(13000L));

        Arrays.asList(
                new MenuProduct(짜장면, Quantity.of(1L)),
                new MenuProduct(짬뽕, Quantity.of(1L)),
                new MenuProduct(탕수육, Quantity.of(1L))
        ).forEach(product -> 나홀로셋트.add(product));

        assertThat(나홀로셋트.getMenuProducts()).hasSize(3);
    }

    @DisplayName("메뉴의 가격이 메뉴의 상품 가격 * 수량보다 작아야 한다.")
    @Test
    void menuPriceLessThanProductAllPrice() {
        Product 짜장면 = new Product("짜장면", Money.price(5000L));
        Product 짬뽕 = new Product("짬뽕", Money.price(5000L));
        Product 탕수육 = new Product("탕수육", Money.price(13000L));

        Arrays.asList(
                new MenuProduct(짜장면, Quantity.of(1L)),
                new MenuProduct(짬뽕, Quantity.of(1L)),
                new MenuProduct(탕수육, Quantity.of(1L))
        ).forEach(product -> 나홀로셋트.add(product));

        assertThatThrownBy(() -> {
            나홀로셋트.checkAllowProductsPrice();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다.")
    @Test
    void isNotCollectMenuPrice() {
        assertThatThrownBy(() -> new Menu("나홀로셋트", Money.price(-1), new MenuGroup("중국집")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}