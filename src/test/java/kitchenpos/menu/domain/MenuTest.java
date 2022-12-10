package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuExceptionCode;
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

@DisplayName("메뉴 클래스 테스트")
class MenuTest {

    private MenuGroup 양식;
    private Menu 양식_세트;
    private MenuProduct 스파게티;
    private MenuProduct 스테이크;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup("양식");
        양식_세트 = new Menu("양식 세트", new BigDecimal(43000), 양식);

        스테이크 = new MenuProduct(양식_세트, new Product("스테이크", new BigDecimal(25000)), 1);
        스파게티 = new MenuProduct(양식_세트, new Product("스파게티", new BigDecimal(18000)), 1);
    }

    @Test
    void 동등성_테스트() {
        assertEquals(new Menu("양식 세트", new BigDecimal(50000), 양식),
                new Menu("양식 세트", new BigDecimal(50000), 양식));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이름을_null_또는_빈_값으로_입력할_수_없음(String name) {
        assertThatThrownBy(() -> {
            new Menu(name, new BigDecimal(43000), 양식);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuExceptionCode.REQUIRED_NAME.getMessage());
    }

    @Test
    void 메뉴_그룹은_null일_수_없음() {
        assertThatThrownBy(() -> {
            new Menu("양식 세트", new BigDecimal(43000), null);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuExceptionCode.REQUIRED_MENU_GROUP.getMessage());
    }

    @Test
    void 메뉴_가격은_null일_수_없음() {
        assertThatThrownBy(() -> {
            new Menu("양식 세트", null, 양식);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuExceptionCode.REQUIRED_PRICE.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, -100, -500, -1000 })
    void 메뉴_가격은_음수일_수_없음(int price) {
        assertThatThrownBy(() -> {
            new Menu("양식 세트", new BigDecimal(price), 양식);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuExceptionCode.INVALID_PRICE.getMessage());
    }

    @Test
    void 메뉴_상품을_추가함() {
        양식_세트.create(Arrays.asList(스테이크, 스파게티));

        assertThat(양식_세트.getMenuProducts()).hasSize(2);
    }

    @Test
    void 이미_등록된_메뉴_상품은_추가되지_않음() {
        양식_세트.create(Arrays.asList(스테이크, 스파게티));
        양식_세트.create(Arrays.asList(스테이크, 스파게티));

        assertThat(양식_세트.getMenuProducts()).hasSize(2);
    }

    @Test
    void 메뉴_상품_추가시_메뉴_상품의_총_금액의_합보다_메뉴의_가격이_클_수_없음() {
        Menu 양식_세트2 = new Menu("양식 세트2", new BigDecimal(60000), 양식);

        assertThatThrownBy(() -> {
            양식_세트2.create(Arrays.asList(스테이크, 스파게티));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuExceptionCode.INVALID_PRICE.getMessage());
    }
}
