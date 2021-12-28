package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 테스트")
class MenuTest {

    @DisplayName("메뉴 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // given
        Name name = Name.of("강정치킨_두마리_세트_메뉴");
        Price price = Price.of(BigDecimal.valueOf(30_000));
        Long menuGroupId = 1L;
        Long productId = 1L;
        MenuProducts menuProducts = MenuProducts.of(Arrays.asList(MenuProduct.of(productId, Quantity.of(2))));

        // when
        Menu menu = Menu.of(name, price, menuGroupId, menuProducts);

        // then
        assertAll(
                () -> assertThat(menu).isNotNull()
                , () -> assertThat(menu.getName()).isEqualTo(name)
                , () -> assertThat(menu.getPrice()).isEqualTo(price)
                , () -> assertThat(menu.getMenuGroupId()).isEqualTo(menuGroupId)
                , () -> assertThat(menu.getMenuProducts()).isEqualTo(menuProducts)
        );
    }
}
