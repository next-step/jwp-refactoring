package kitchenpos.menu.domain;

import kitchenpos.common.Message;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    private String 메뉴이름 = "후라이드 세트";
    private MenuGroup 인기메뉴 = new MenuGroup("인기메뉴");
    private Product 후라이드 = new Product("후라이드", BigDecimal.valueOf(15000));
    private Product 콜라 = new Product("콜라", BigDecimal.valueOf(3000));
    private MenuProduct 후라이드한마리 = new MenuProduct(후라이드, 1L);
    private MenuProduct 콜라한개 = new MenuProduct(콜라, 1L);
    private List<MenuProduct> 메뉴상품목록 = Arrays.asList(콜라한개, 후라이드한마리);

    @DisplayName("메뉴 등록시, 가격정보가 입력되지 않으면 예외발생")
    @Test
    void 메뉴등록시_가격정보_없는_경우_예외발생() {
        BigDecimal 비어있는_가격정보 = null;

        assertThatThrownBy(() -> new Menu(메뉴이름, 비어있는_가격정보, 인기메뉴, 메뉴상품목록))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Message.ERROR_MENU_PRICE_REQUIRED.showText());
    }

    @DisplayName("메뉴 등록시, 가격정보가 0원 미만인 경우 예외발생")
    @Test
    void 메뉴등록시_가격정보_0원_미만인_경우_예외발생() {
        BigDecimal 음수인_가격정보 = BigDecimal.valueOf(-1000);

        assertThatThrownBy(() -> new Menu(메뉴이름, 음수인_가격정보, 인기메뉴, 메뉴상품목록))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Message.ERROR_MENU_PRICE_SHOULD_BE_OVER_THAN_ZERO.showText());
    }

    @DisplayName("메뉴 등록시, 가격이 메뉴상품 목록의 가격합보다 큰 경우 예외발생")
    @Test
    void 메뉴등록시_가격이_메뉴상품_목록의_가격합보다_큰_경우_예외발생() {

        BigDecimal 메뉴상품_가격합보다_큰_가격 = 후라이드한마리.getTotalPrice().add(콜라한개.getTotalPrice())
                .add(BigDecimal.valueOf(1000));

        assertThatThrownBy(() -> new Menu(메뉴이름, 메뉴상품_가격합보다_큰_가격, 인기메뉴, 메뉴상품목록))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Message.ERROR_MENU_PRICE_CANNOT_BE_BIGGER_THAN_MENUPRODUCTS_TOTAL.showText());
    }
}
