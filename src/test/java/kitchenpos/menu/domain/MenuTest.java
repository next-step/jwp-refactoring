package kitchenpos.menu.domain;

import kitchenpos.menu.exception.IllegalPriceException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuTest {

    private String menuName = "메뉴1";
    private BigDecimal price = BigDecimal.valueOf(20_000);
    private MenuGroup menuGroup = new MenuGroup(1L, "그룹1");
    private Product 상품1 = new Product(1L, "상품1", BigDecimal.valueOf(10_000));
    private Product 상품2 = new Product(2L, "상품2", BigDecimal.valueOf(10_000));
    private MenuProduct 메뉴상품1 = new MenuProduct(상품1.getId(), 1L);
    private MenuProduct 메뉴상품2 = new MenuProduct(상품2.getId(), 1L);
    private List<MenuProduct> menuProducts = Arrays.asList(메뉴상품1, 메뉴상품2);

    @DisplayName("메뉴가 생성된다. (메뉴명, 가격, 메뉴그룹id, 메뉴 상품 리스트) 입력")
    @Test
    void createMenu() {
        Menu menu = new Menu(menuName, price, menuGroup, 20_000L, menuProducts);

        assertAll(
                () -> assertThat(menu.getName()).isEqualTo(menuName),
                () -> assertThat(menu.getPrice()).isEqualTo(price),
                () -> assertThat(menu.getMenuGroup()).isEqualTo(menuGroup),
                () -> assertThat(menu.getMenuProducts()).containsExactly(메뉴상품1, 메뉴상품2));
    }

    @DisplayName("메뉴 생성을 실패한다. -가격이 0 보다 작을 경우 실패한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, Integer.MIN_VALUE})
    void fail_createMenu_for_price1(int price) {
        assertThatThrownBy(() -> new Menu(menuName, new BigDecimal(price), menuGroup,20_000L, menuProducts))
                .isInstanceOf(IllegalPriceException.class);
    }

    @DisplayName("메뉴 생성을 실패한다. -가격이 null일 경우 실패한다.")
    @Test
    void fail_createMenu_for_price2() {
        assertThatThrownBy(() -> new Menu(menuName, null, menuGroup, 20_000L, menuProducts))
                .isInstanceOf(IllegalPriceException.class);
    }

    @DisplayName("메뉴 생성을 실패한다. -메뉴의 가격이 메뉴 상품 리스트에 있는 상품들 가격의 합보다 클 경우")
    @Test
    void fail_createMenu_for_price3() {
        BigDecimal bigPrice = BigDecimal.valueOf(30_000);
        assertThatThrownBy(() -> new Menu(menuName, bigPrice, menuGroup, 20_000L, menuProducts))
                .isInstanceOf(IllegalPriceException.class);
    }
}
