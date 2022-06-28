package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MenuProductsTest {

    @ParameterizedTest(name = "{0} -> {1}")
    @DisplayName("빈 메뉴 상품을 추가할 경우 에러를 던진다.")
    @MethodSource("providerAddMenuProductsCase")
    void addMenuProducts(List<MenuProduct> products, Class<? extends Exception> exception) {
        MenuProducts menuProducts = new MenuProducts();
        assertThatExceptionOfType(exception)
            .isThrownBy(() -> menuProducts.addMenuProducts(products));
    }

    private static Stream<Arguments> providerAddMenuProductsCase() {
        return Stream.of(
            Arguments.of(null, NullPointerException.class),
            Arguments.of(Collections.emptyList(), IllegalArgumentException.class),
            Arguments.of(Collections.singletonList(null), NullPointerException.class)
        );
    }

}
