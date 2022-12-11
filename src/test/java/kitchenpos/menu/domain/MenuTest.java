package kitchenpos.menu.domain;

import static kitchenpos.menu.domain.MenuProductTest.메뉴상품_생성;
import static kitchenpos.menugroup.domain.MenuGroupTest.메뉴그룹_생성;
import static kitchenpos.product.domain.ProductTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

    @Test
    @DisplayName("메뉴 생성 시 메뉴의 가격이 0원 미만이면 예외가 발생한다.")
    void validatePrice() {
        // given
        String name = "순대국밥";
        BigDecimal price = BigDecimal.valueOf(-1);
        MenuGroup menuGroup = 메뉴그룹_생성(1L, "식사");
        Product product = 상품_생성(1L, "순대국밥", BigDecimal.valueOf(6000));
        MenuProduct menuProduct = 메뉴상품_생성(1L, null, product, 1L);

        // expect
        assertThatThrownBy(() -> 메뉴_생성(null, name, price, menuGroup, Arrays.asList(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 시 메뉴그룹이 존재하지 않으면 예외가 발생한다.")
    void validateMenuGroup() {
        // given
        String name = "순대국밥";
        BigDecimal price = BigDecimal.valueOf(6000);
        MenuGroup menuGroup = null;
        Product product = 상품_생성(1L, "순대국밥", BigDecimal.valueOf(6000));
        MenuProduct menuProduct = 메뉴상품_생성(1L, null, product, 1L);

        // expect
        assertThatThrownBy(() -> 메뉴_생성(null, name, price, menuGroup, Arrays.asList(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 시 메뉴의 가격이 상품들의 합보다 크면 예외가 발생한다.")
    void validateMenuProductsSumPrice() {
        // given
        String name = "순대국밥";
        BigDecimal price = BigDecimal.valueOf(7000);
        MenuGroup menuGroup = 메뉴그룹_생성(1L, "식사");
        Product product = 상품_생성(1L, "순대국밥", BigDecimal.valueOf(6000));
        MenuProduct menuProduct = 메뉴상품_생성(1L, null, product, 1L);

        // expect
        assertThatThrownBy(() -> 메뉴_생성(null, name, price, menuGroup, Arrays.asList(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static Menu 메뉴_생성(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu.Builder()
                .id(id)
                .name(name)
                .price(price)
                .menuGroup(menuGroup)
                .menuProducts(menuProducts)
                .build();
    }
}