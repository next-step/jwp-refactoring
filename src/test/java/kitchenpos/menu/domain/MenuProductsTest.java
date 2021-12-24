package kitchenpos.menu.domain;

import static common.MenuProductFixture.떡볶이_1개;
import static common.MenuProductFixture.양념치킨_1개;
import static common.MenuProductFixture.콜라_1개;

import common.MenuFixture;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.product.domain.Amount;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MenuProductsTest {

    @ParameterizedTest
    @MethodSource("initMenuParam")
    void 음식_수량에따른_합계(List<MenuProduct> menuProducts, Amount amount) {
        MenuProducts products = MenuProducts.of(menuProducts);

        Amount sum = products.sum();
        Assertions.assertThat(sum).isEqualTo(amount);
    }

    public static Stream<Arguments> initMenuParam() {
        Menu 메뉴_양념치킨 = MenuFixture.메뉴_양념치킨();
        return Stream.of(
            Arguments.of(Arrays.asList(콜라_1개(메뉴_양념치킨), 양념치킨_1개(메뉴_양념치킨)), Amount.of(18000)),
            Arguments.of(Arrays.asList(떡볶이_1개(메뉴_양념치킨), 양념치킨_1개(메뉴_양념치킨)), Amount.of(22000))
        );
    }
}