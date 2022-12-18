package kitchenpos.menu.domain;

import kitchenpos.common.ErrorCode;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("메뉴 도메인 단위 테스트")
public class MenuTest {

    private MenuGroup 한식;
    private Menu 한정식_A세트;
    private MenuProduct 불고기;
    private MenuProduct 용포탕;

    @BeforeEach
    void setUp() {
        한식 = new MenuGroup("한식");
        한정식_A세트 = new Menu("한정식 A세트", new BigDecimal(50000), 한식);
        불고기 = new MenuProduct(한정식_A세트, new Product("불고기", new BigDecimal(15000)), 1L);
        용포탕 = new MenuProduct(한정식_A세트, new Product("용포탕", new BigDecimal(35000)), 1L);
    }

    @DisplayName("메뉴 이름이 같으면 동등하다.")
    @Test
    void menuEquals() {
        assertEquals(new Menu("한정식 A세트", new BigDecimal(50000), 한식),
                new Menu("한정식 A세트", new BigDecimal(45000), 한식));
    }

    @DisplayName("이름을 null 또는 빈 값으로 입력하면 예외처리 된다.")
    @ParameterizedTest
    @NullAndEmptySource
    void makeExceptionWhenNameIsNullOrEmpty(String name) {
        assertThatThrownBy(() -> {
            new Menu(name, new BigDecimal(43000), 한식);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.INVALID_FORMAT_MENU_NAME.getErrorMessage());
    }

    @DisplayName("메뉴그룹이 null이면 예외처리 된다.")
    @Test
    void makeExceptionWhenMenuGroupIsNull() {
        assertThatThrownBy(() -> {
            new Menu("한정식_A", new BigDecimal(43000), null);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.INVALID_FORMAT_MENU_GROUP.getErrorMessage());
    }

    @DisplayName("메뉴가격이 null이면 예외처리 된다.")
    @Test
    void makeExceptionWhenPriceIsNull() {
        assertThatThrownBy(() -> {
            new Menu("한정식_A", null, 한식);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.INVALID_FORMAT_PRICE.getErrorMessage());
    }

    @DisplayName("메뉴가격이 음수이면 예외처리 된다.")
    @ParameterizedTest
    @ValueSource(ints = { -1, -100, -500, -1000 })
    void makeExceptionWhenPriceIsNegative(int price) {
        assertThatThrownBy(() -> {
            new Menu("한정식_A", new BigDecimal(price), 한식);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.INVALID_FORMAT_PRICE_IS_NEGATIVE.getErrorMessage());
    }

    @DisplayName("메뉴상품을 추가하면 메뉴에 추가된다.")
    @Test
    void addMenuProductTest() {
        한정식_A세트.create(Arrays.asList(불고기, 용포탕));
        assertThat(한정식_A세트.getMenuProducts()).hasSize(2);
    }

    @DisplayName("이미 등록된 메뉴 상품은 추가 되지 않는다.")
    @Test
    void addSameMenuProductTest() {
        한정식_A세트.create(Arrays.asList(불고기, 용포탕));
        한정식_A세트.create(Arrays.asList(불고기, 용포탕));
        assertThat(한정식_A세트.getMenuProducts()).hasSize(2);
    }

    @DisplayName("메뉴상품 추가 시 메뉴상품의 총 금액의 합보다 메뉴의 가격이 클 수 없다.")
    @Test
    void makeExceptionWhenMenuPriceGreaterThanMenuProductPrice() {
        Menu 한정식_B세트 = new Menu("한정식 B세트", new BigDecimal(60000), 한식);

        assertThatThrownBy(() -> {
            한정식_B세트.create(Arrays.asList(불고기, 용포탕));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.INVALID_ADD_MENU_PRICE.getErrorMessage());
    }
}
