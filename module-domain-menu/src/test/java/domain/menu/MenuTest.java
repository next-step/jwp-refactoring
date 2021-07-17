package domain.menu;

import common.valueobject.exception.InvalidNameException;
import common.valueobject.exception.NegativePriceException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    void create() {
        //when
        MenuGroup menuGroup = MenuGroup.of("1인세트");
        BigDecimal menuPrice = BigDecimal.valueOf(8000L);
        Product product1 = Product.of("제육볶음", BigDecimal.valueOf(8000L));
        Product product2 = Product.of("콜라", BigDecimal.valueOf(1000L));
        MenuProduct menuProduct1 = MenuProduct.of(product1, 1);
        MenuProduct menuProduct2 = MenuProduct.of(product2, 1);

        //then
        Menu menu = Menu.create("제육+음료세트", menuPrice, menuGroup, Lists.list(menuProduct1, menuProduct2));

        //then
        assertThat(menu.getMenuGroup()).isEqualTo(menuGroup);
        assertThat(menu.getMenuProducts()).contains(menuProduct1, menuProduct2);
        assertThat(menu.getName().getValue()).isEqualTo("제육+음료세트");
        assertThat(menu.getPrice().getValue()).isEqualTo(BigDecimal.valueOf(8000L));
    }

    @DisplayName("메뉴의 총 가격은 0원 이상이어야한다.")
    @Test
    void createMenuExceptionIfPriceIsNull() {
        //when
        assertThatThrownBy(() -> Menu.of("10만원의 행복 파티 세트 (12인)", BigDecimal.valueOf(-10)))
                .isInstanceOf(NegativePriceException.class); //then
    }

    @DisplayName("메뉴의 이름을 지정해야한다.")
    @Test
    void createMenuExceptionIfNameIsNull() {
        //when
        assertThatThrownBy(() -> Menu.of("", BigDecimal.valueOf(100)))
                .isInstanceOf(InvalidNameException.class); //then
        //when
        assertThatThrownBy(() -> Menu.of(null, BigDecimal.valueOf(100)))
                .isInstanceOf(InvalidNameException.class); //then
    }
}
