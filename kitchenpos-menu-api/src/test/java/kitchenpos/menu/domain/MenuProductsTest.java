package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuExceptionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 상품 목록을 관리하는 클래스 테스트")
class MenuProductsTest {

    private MenuGroup 양식;
    private Menu 양식_세트;
    private MenuProducts 양식_세트_목록;
    private MenuProduct 스테이크;
    private MenuProduct 스파게티;
    private MenuProduct 에이드;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup("양식");
        양식_세트 = new Menu("양식 세트", new BigDecimal(50000), 양식);
        스테이크 = new MenuProduct(양식_세트, new Product("스테이크", new BigDecimal(25000)), 1);
        스파게티 = new MenuProduct(양식_세트, new Product("스파게티", new BigDecimal(18000)), 1);
        에이드 = new MenuProduct(양식_세트, new Product("에이드", new BigDecimal(3500)), 2);

        양식_세트_목록 = new MenuProducts();
        양식_세트_목록.addMenuProduct(양식_세트, 스테이크);
        양식_세트_목록.addMenuProduct(양식_세트, 스파게티);
    }

    @Test
    void 메뉴_상품을_추가() {
        양식_세트_목록.addMenuProduct(양식_세트, 에이드);

        assertThat(양식_세트_목록.getMenuProducts()).hasSize(3);
    }

    @Test
    void 이미_포함된_메뉴_상품은_추가되지_않음() {
        양식_세트_목록.addMenuProduct(양식_세트, 스테이크);

        assertThat(양식_세트_목록.getMenuProducts()).hasSize(2);
    }

    @Test
    void 메뉴_상품의_총_금액의_합보다_메뉴의_가격이_클_수_없음() {
        assertThatThrownBy(() -> {
            양식_세트_목록.validatePrice(양식_세트.getPrice());
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuExceptionCode.INVALID_PRICE.getMessage());
    }
}
