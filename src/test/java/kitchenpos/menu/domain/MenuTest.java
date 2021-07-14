package kitchenpos.menu.domain;

import kitchenpos.common.Message;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

//    private String 메뉴이름 = "후라이드 세트";
//    private MenuGroup 인기메뉴 = new MenuGroup("인기메뉴");
//    private Product 후라이드 = new Product("후라이드", Price.valueOf(15000));
//    private Product 콜라 = new Product("콜라", Price.valueOf(3000));
//    private MenuProduct 후라이드한마리 = new MenuProduct(후라이드, Quantity.of(1L));
//    private MenuProduct 콜라한개 = new MenuProduct(콜라, Quantity.of(1L));
//    private List<MenuProduct> 메뉴상품목록 = Arrays.asList(콜라한개, 후라이드한마리);
//
//    @DisplayName("메뉴 등록시, 가격이 메뉴상품 목록의 가격합보다 큰 경우 예외발생")
//    @Test
//    void 메뉴등록시_가격이_메뉴상품_목록의_가격합보다_큰_경우_예외발생() {
//
//        Price 메뉴상품_가격합보다_큰_가격 = 후라이드한마리.getTotalPrice()
//                .add(콜라한개.getTotalPrice())
//                .add(Price.valueOf(1000));
//
//        assertThatThrownBy(() -> new Menu(메뉴이름, 메뉴상품_가격합보다_큰_가격, 인기메뉴, 메뉴상품목록))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage(Message.ERROR_MENU_PRICE_CANNOT_BE_BIGGER_THAN_MENUPRODUCTS_TOTAL.showText());
//    }
}
