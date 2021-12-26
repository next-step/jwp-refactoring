package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MenuProductTest {
    
    @DisplayName("메뉴 상품의 수량 반영 가격 확인")
    @ParameterizedTest
    @CsvSource(value = { "3000:2:6000", "5000:1:5000" }, delimiter = ':')
    void 메뉴_상품_가격(int price, int quantity, int expected) {
        // given
        Menu 메뉴 = Menu.of("치킨", price, MenuGroup.from("메뉴그룹"));
        Product 상품 = Product.of("치킨", price);
        MenuProduct 메뉴_상품 = MenuProduct.of(상품, quantity);
        
        // when
        Price 메뉴_상품_가격 = 메뉴_상품.getPrice();
        
        // then
        assertThat(메뉴_상품_가격.intValue()).isEqualTo(expected);
    }

}
