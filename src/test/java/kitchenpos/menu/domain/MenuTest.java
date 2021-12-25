package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.exception.PriceNotAcceptableException;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴 생성")
    @Test
    void construct1() {
        //given
        String menuName = "앙념반 후라이드반";
        Price price = new Price(BigDecimal.ZERO);
        MenuGroup menuGroup = new MenuGroup("추천메뉴");
        Menu expectMenu = new Menu(menuName, price, menuGroup);

        //when
        Menu menu = new Menu(menuName, price, menuGroup);

        //then
        assertThat(menu.getName()).isEqualTo(expectMenu.getName());
        assertThat(menu.getMenuGroup()).isEqualTo(expectMenu.getMenuGroup());
    }

    @DisplayName("메뉴 상품을 포함한 메뉴 생성")
    @Test
    void construct2() {
        //given
        MenuGroup menuGroup = new MenuGroup("추천메뉴");
        Product 타코야끼 = new Product(1L, "타코야끼", new Price(BigDecimal.valueOf(12000)));
        Product 뿌링클 = new Product(2L, "뿌링클", new Price(BigDecimal.valueOf(15000)));
        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(타코야끼, new Quantity(3L)),
            new MenuProduct(뿌링클, new Quantity(1L)));

        //when
        // 타코야끼x3 = 36,000, 뿌링클X1 = 15,000 => 51,000
        String menuName = "타코야끼와 뿌링클";
        Menu menu = new Menu(menuName, new Price(BigDecimal.valueOf(51000)), menuGroup,
            menuProducts);

        //then
        assertThat(menu.getName()).isEqualTo(menuName);
    }

    @DisplayName("메뉴에 속한 상품리스트가 없을 경우 메뉴 가격은 0원 이어야 한다.")
    @Test
    void construct_exception1() {
        //given
        String menuName = "앙념반 후라이드반";
        Price price = new Price(BigDecimal.valueOf(15000));
        MenuGroup menuGroup = new MenuGroup("추천메뉴");

        //when, then
        assertThatThrownBy(() -> new Menu(menuName, price, menuGroup))
            .isInstanceOf(PriceNotAcceptableException.class);
    }

    @DisplayName("메뉴 가격은 상품 리스트의 합보다 작거나 같아야 한다.")
    @Test
    void construct_exception2() {
        //given
        MenuGroup menuGroup = new MenuGroup("추천메뉴");
        Product 타코야끼 = new Product(1L, "타코야끼", new Price(BigDecimal.valueOf(12000)));
        Product 뿌링클 = new Product(2L, "뿌링클", new Price(BigDecimal.valueOf(15000)));
        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(타코야끼, new Quantity(3L)),
            new MenuProduct(뿌링클, new Quantity(1L)));

        //when, then
        // 타코야끼x3 = 36,000, 뿌링클X1 = 15,000 => 51,000 < 51001
        assertThatThrownBy(() -> new Menu("타코야끼와 뿌링클", new Price(BigDecimal.valueOf(51001)),
            menuGroup, menuProducts))
            .isInstanceOf(PriceNotAcceptableException.class);
    }
}
