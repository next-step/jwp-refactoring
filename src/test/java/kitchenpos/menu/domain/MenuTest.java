package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MenuTest {

    @Test
    @DisplayName("Menu의 정상 생성을 확인한다.")
    void createMenu() {
        MenuProduct menuProduct = new MenuProduct(new Product("상품", BigDecimal.valueOf(10000L)), 10);
        Menu menu = new Menu("메뉴", BigDecimal.TEN, 10L, Collections.singletonList(menuProduct));

        assertThat(menu.getName()).isEqualTo("메뉴");
        assertThat(menu.getPrice()).isEqualTo(BigDecimal.TEN);
        assertThat(menu.getMenuGroupId()).isEqualTo(10L);
        assertThat(menu.getMenuProducts()).hasSize(1);
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Menu 생성시 실페 케이스를 체크한다.")
    @MethodSource("providerCreateMenuFailCase")
    void createMenuFail(String testName, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Menu(name, price, menuGroupId, menuProducts));
    }

    private static Stream<Arguments> providerCreateMenuFailCase() {
        MenuProduct menuProduct = new MenuProduct(new Product("상품", BigDecimal.valueOf(10000L)), 1);

        return Stream.of(
            Arguments.of("이름이 존재하지 않을 경우", null, BigDecimal.TEN, 10L, Collections.singletonList(menuProduct)),
            Arguments.of("가격이 존재하지 않을 경우", "상품", null, 10L, Collections.singletonList(menuProduct)),
            Arguments.of("메뉴 그룹 정보가 존재하지 않을 경우", "상품", BigDecimal.TEN, null, Collections.singletonList(menuProduct)),
            Arguments.of("상품이 존재하지 않았을 경우", "상품", BigDecimal.TEN, 10L, Collections.emptyList()),
            Arguments.of("메뉴의 금액이 상품의 총합보다 클 경우", "상품", BigDecimal.valueOf(20000L), 10L, Collections.singletonList(menuProduct))
        );
    }

}
