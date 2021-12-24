package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 테스트")
class MenuTest {

    @DisplayName("메뉴 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // given
        Name name = Name.of("강정치킨_두마리_세트_메뉴");
        Price price = Price.of(BigDecimal.valueOf(30_000));
        MenuGroup menuGroup = MenuGroup.of("추천_메뉴_그룹");
        Product product = Product.of("강정치킨", BigDecimal.valueOf(17_000));
        MenuProducts menuProducts = MenuProducts.of(Arrays.asList(MenuProduct.of(product, Quantity.of(2))));

        // when
        Menu menu = Menu.of(name, price, menuGroup, menuProducts);

        // then
        assertAll(
                () -> assertThat(menu).isNotNull()
                , () -> assertThat(menu.getName()).isEqualTo(name)
                , () -> assertThat(menu.getPrice()).isEqualTo(price)
                , () -> assertThat(menu.getMenuGroup()).isEqualTo(menuGroup)
                , () -> assertThat(menu.getMenuProducts()).isEqualTo(menuProducts)
        );
    }

    @DisplayName("메뉴 생성 실패 테스트 - 메뉴가 메뉴 상품들의 합계보다 비쌈")
    @Test
    void instantiate_failure_isMoreExpensive() {
        // given
        Name name = Name.of("강정치킨_두마리_세트_메뉴");
        Price price = Price.of(BigDecimal.valueOf(51_000));
        MenuGroup menuGroup = MenuGroup.of("추천_메뉴_그룹");
        Product product = Product.of("강정치킨", BigDecimal.valueOf(17_000));
        MenuProducts menuProducts = MenuProducts.of(Arrays.asList(MenuProduct.of(product, Quantity.of(2))));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Menu.of(name, price, menuGroup, menuProducts));
    }
}
