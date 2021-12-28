package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MenuProductTest {
    
    @DisplayName("상품의 가격은 메뉴의 가격과 수량을 곱한 값이다")
    @ParameterizedTest
    @CsvSource(value = { "3000:2:6000", "5000:1:5000" }, delimiter = ':')
    void 메뉴_상품_가격(Long price, int quantity, int expected) {
        // given
        Menu 메뉴 = Menu.of("치킨", price, MenuGroup.from("메뉴그룹"));
        Product 상품 = Product.of("치킨", price);
        MenuProduct 메뉴_상품 = MenuProduct.of(상품, quantity);
        
        // when
        Price 메뉴_상품_가격 = 메뉴_상품.getPrice();
        
        // then
        assertThat(메뉴_상품_가격.getValue()).isEqualTo(expected);
    }

}
