package kitchenpos.menu.domain;

import kitchenpos.exception.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("메뉴 상품 목록 단위 테스트")
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
        양식_세트 = new Menu("양식 세트", 50000, 양식);
        스테이크 = new MenuProduct(양식_세트, new Product("스테이크", 25000), 1);
        스파게티 = new MenuProduct(양식_세트, new Product("스파게티", 18000), 1);
        에이드 = new MenuProduct(양식_세트, new Product("에이드", 3500), 2);

        양식_세트_목록 = new MenuProducts();
        양식_세트_목록.addMenuProduct(양식_세트, 스테이크);
        양식_세트_목록.addMenuProduct(양식_세트, 스파게티);
    }

    @DisplayName("메뉴 상품을 추가한다.")
    @Test
    void 메뉴_상품을_추가한다() {
        양식_세트_목록.addMenuProduct(양식_세트, 에이드);

        assertThat(양식_세트_목록.values()).hasSize(3);
    }

    @DisplayName("이미 포함된 메뉴 상품은 추가되지 않는다.")
    @Test
    void 이미_포함된_메뉴_상품은_추가되지_않는다() {
        양식_세트_목록.addMenuProduct(양식_세트, 스테이크);

        assertThat(양식_세트_목록.values()).hasSize(2);
    }

    @DisplayName("메뉴 상품의 총 금액의 합보다 메뉴의 가격이 클 수 없다.")
    @Test
    void 메뉴_상품의_총_금액의_합보다_메뉴의_가격이_클_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 양식_세트_목록.validatePrice(양식_세트.getPrice()))
                .withMessage(ErrorMessage.MENU_INVALID_PRICE.getMessage());
    }
}
