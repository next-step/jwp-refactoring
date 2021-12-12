package kitchenpos.menu.domain;

import static kitchenpos.menu.sample.ProductSample.십원치킨;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.stream.Stream;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("메뉴")
class MenuTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Menu.of(
                Name.from("후라이드치킨세트"),
                Price.ZERO,
                MenuGroup.from(Name.from("두마리메뉴")),
                치킨_메뉴_상품들()
            ));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 생성 불가능")
    @DisplayName("이름, 가격, 메뉴 그룹, 메뉴 상품들은 필수")
    @MethodSource
    void instance_nullArguments_thrownIllegalArgumentException(
        Name name, Price price, MenuGroup menuGroup, MenuProducts products) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Menu.of(name, price, menuGroup, products))
            .withMessageEndingWith("필수입니다.");
    }

    @Test
    @DisplayName("메뉴 상품들이 비어있으면 안됨")
    void instance_emptyMenuProducts_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Menu.of(
                mock(Name.class),
                Price.ZERO,
                mock(MenuGroup.class),
                MenuProducts.from(Collections.emptyList())))
            .withMessageEndingWith("개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("메뉴 가격이 메뉴 상품들의 가격보다 같거나 저렴해야 함")
    void instance_priceGreaterThanProductPrice_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Menu.of(
                mock(Name.class),
                Price.from(BigDecimal.valueOf(Long.MAX_VALUE)),
                mock(MenuGroup.class),
                치킨_메뉴_상품들()))
            .withMessageEndingWith("가격보다 작거나 같아야 합니다.");
    }

    private static Stream<Arguments> instance_nullArguments_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(null, Price.ZERO, mock(MenuGroup.class), 치킨_메뉴_상품들()),
            Arguments.of(Name.from("치킨"), null, mock(MenuGroup.class), 치킨_메뉴_상품들()),
            Arguments.of(Name.from("치킨"), Price.ZERO, null, 치킨_메뉴_상품들()),
            Arguments.of(Name.from("치킨"), Price.ZERO, mock(MenuGroup.class), null)
        );

    }

    private static MenuProducts 치킨_메뉴_상품들() {
        return MenuProducts.singleton(
            MenuProduct.of(십원치킨(), Quantity.from(2L))
        );
    }
}
