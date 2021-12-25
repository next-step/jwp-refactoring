package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import kitchenpos.common.exception.InvalidArgumentException;
import kitchenpos.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("메뉴 도메인 테스트")
class MenuTest {

    private final Product 후라이드치킨_상품 = Product.of("후라이드치킨", 10000);
    private final Product 양념치킨_상품 = Product.of("양념치킨", 11000);
    private final MenuProduct 후라이드치킨 = MenuProduct.of(후라이드치킨_상품, 1L);
    private final MenuProduct 양념치킨 = MenuProduct.of(양념치킨_상품, 1L);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(후라이드치킨_상품, "id", 1L);
        ReflectionTestUtils.setField(양념치킨_상품, "id", 2L);
        ReflectionTestUtils.setField(후라이드치킨, "seq", 1L);
        ReflectionTestUtils.setField(양념치킨, "seq", 2L);
    }

    @Test
    @DisplayName("메뉴 정합성 체크")
    void validateMenuGroup() {
        assertThatThrownBy(() -> Menu.of("메뉴", 10000, null))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("메뉴그룹은 필수입니다.");
    }

    @Test
    @DisplayName("메뉴 상품 추가")
    void addMenuProduct() {
        Menu menu = Menu.of("세트_1", 20000, MenuGroup.from("치킨"));

        menu.addMenuProducts(Arrays.asList(후라이드치킨, 양념치킨));

        assertThat(menu.getMenuProducts().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("구성하는 상품의 총 가격보다 작거나 같아야 한다.")
    void validatePrice() {
        Menu menu = Menu.of("세트_1", 22000, MenuGroup.from("치킨"));

        assertThatThrownBy(() -> menu.addMenuProducts(Arrays.asList(후라이드치킨, 양념치킨)))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("메뉴의 총 가격은 구성하는 상품의 총가격보다 작거나 같아야 합니다.");
    }

}