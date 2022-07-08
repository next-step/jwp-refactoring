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
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuTest {

    @Test
    @DisplayName("메뉴 생성 성공")
    void menu() {
        // given
        Product 피자 = Product.of(Name.of("피자"), Price.of(BigDecimal.valueOf(17_000)));
        MenuGroup 피자_2판_메뉴_그룹 = MenuGroup.of(Name.of("피자_2판_메뉴_그룹"));
        MenuProduct 피자_2판 = MenuProduct.of(피자.getId(), Quantity.of(2));
        MenuProducts 피자_구성품 = MenuProducts.of(Arrays.asList(피자_2판));

        Name 세트_메뉴_이름 = Name.of("피자_두판_세트_메뉴");
        Price 세트_메뉴_가격 = Price.of(BigDecimal.valueOf(30_000));

        // when
        Menu menu = Menu.of(세트_메뉴_이름, 세트_메뉴_가격, 피자_2판_메뉴_그룹.getId(), 피자_구성품);

        // then
        assertAll(
                () -> assertThat(menu).isNotNull(),
                () -> assertThat(menu.getName()).isEqualTo("피자_두판_세트_메뉴"),
                () -> assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(30_000)),
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(피자_2판_메뉴_그룹.getId()),
                () -> assertThat(menu.getMenuProducts()).isEqualTo(피자_구성품.getMenuProducts())
        );
    }
}
