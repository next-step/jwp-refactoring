package kitchenpos.menu.domain;

import static kitchenpos.menugroup.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenu;
import static kitchenpos.product.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("메뉴 관련 도메인 테스트")
public class MenuTest {

    private Product 감자튀김;
    private Product 불고기버거;
    private Product 콜라;
    private MenuGroup 햄버거세트;
    private MenuProduct 감자튀김상품;
    private MenuProduct 불고기버거상품;
    private MenuProduct 콜라상품;

    @BeforeEach
    void setUp() {
        감자튀김 = generateProduct(1L, "감자튀김", BigDecimal.valueOf(3000L));
        콜라 = generateProduct(2L, "콜라", BigDecimal.valueOf(1500L));
        불고기버거 = generateProduct(3L, "불고기버거", BigDecimal.valueOf(4000L));
        햄버거세트 = generateMenuGroup(1L, "햄버거세트");
        감자튀김상품 = generateMenuProduct(1L, null, 감자튀김, 1L);
        콜라상품 = generateMenuProduct(2L, null, 콜라, 1L);
        불고기버거상품 = generateMenuProduct(3L, null, 불고기버거, 1L);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // given
        String name = "불고기버거세트";
        BigDecimal price = BigDecimal.valueOf(8500);

        // when
        Menu 불고기버거세트 = generateMenu(1L, name, price, 햄버거세트, Arrays.asList(감자튀김상품, 불고기버거상품, 콜라상품));

        // then
        assertAll(
                () -> assertThat(불고기버거세트.getName().value()).isEqualTo(name),
                () -> assertThat(불고기버거세트.getMenuProducts().findMenuProducts()).containsExactly(감자튀김상품, 불고기버거상품, 콜라상품)
        );
    }

    @DisplayName("메뉴 생성 시, 가격이 비어있으면 에러가 발생한다.")
    @Test
    void createMenuThrowErrorWhenPriceIsNull() {
        // given
        String name = "불고기버거세트";
        BigDecimal price = null;

        // when & then
        assertThatThrownBy(() -> generateMenu(1L, name, price, 햄버거세트, Arrays.asList(감자튀김상품, 불고기버거상품, 콜라상품)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.가격은_비어있을_수_없음.getErrorMessage());
    }

    @ParameterizedTest(name = "메뉴 생성 시, 가격이 음수이면 에러가 발생한다. (가격: {0})")
    @ValueSource(longs = {-1000, -2030})
    void createMenuThrowErrorWhenPriceIsSmallerThanZero(long price) {
        // given
        String name = "불고기버거세트";

        // when & then
        assertThatThrownBy(() -> generateMenu(1L, name, BigDecimal.valueOf(price), 햄버거세트, Arrays.asList(감자튀김상품, 불고기버거상품, 콜라상품)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.가격은_0보다_작을_수_없음.getErrorMessage());
    }

    @DisplayName("메뉴 생성 시, 이름이 null이면 에러가 발생한다.")
    @Test
    void createMenuThrowErrorWhenNameIsNull() {
        // given
        String name = null;
        BigDecimal price = BigDecimal.valueOf(8500);

        // when & then
        assertThatThrownBy(() -> generateMenu(1L, name, price, 햄버거세트, Arrays.asList(감자튀김상품, 불고기버거상품, 콜라상품)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.이름은_비어_있을_수_없음.getErrorMessage());
    }

    @DisplayName("메뉴 생성 시, 이름이 비어있이면 에러가 발생한다.")
    @Test
    void createMenuThrowErrorWhenNameIsEmpty() {
        // given
        String name = "";
        BigDecimal price = BigDecimal.valueOf(8500);

        // when & then
        assertThatThrownBy(() -> generateMenu(1L, name, price, 햄버거세트, Arrays.asList(감자튀김상품, 불고기버거상품, 콜라상품)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.이름은_비어_있을_수_없음.getErrorMessage());
    }
}
