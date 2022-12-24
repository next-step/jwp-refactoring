package kitchenpos.menu.domain;

import kitchenpos.common.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuProductsTest {

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

    @DisplayName("메뉴 상품을 추가 한다.")
    @Test
    void createMenuProduct() {
        양식_세트_목록.addMenuProduct(양식_세트, 에이드);

        assertThat(양식_세트_목록.getMenuProducts()).hasSize(3);
    }

    @DisplayName("이미 포함된 메뉴 상품은 추가되지 않는다.")
    @Test
    void createSameMenuProduct() {
        양식_세트_목록.addMenuProduct(양식_세트, 스테이크);

        assertThat(양식_세트_목록.getMenuProducts()).hasSize(2);
    }

    @DisplayName("메뉴 상품의 총 금액의 합보다 메뉴의 가격이 클 수 없다.")
    @Test
    void validateMenuPrice() {
        assertThatThrownBy(() -> {
            양식_세트_목록.validatePrice(양식_세트.getPriceToDecimal());
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.INVALID_ADD_MENU_PRICE.getErrorMessage());
    }
}
