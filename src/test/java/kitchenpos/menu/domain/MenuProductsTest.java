package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.domain.Product;

public class MenuProductsTest {
    
    @DisplayName("메뉴 상품들의 총 가격을 확인한다")
    @Test
    void 메뉴_상품_총_가격_학인() {
        // given
        Menu 메뉴 = Menu.of("치킨세트", 22_000L, MenuGroup.from("메뉴그룹"));
        Product 치킨 = Product.of("치킨", 18_000L);
        Product 콜라 = Product.of("치킨", 2_000L);
        MenuProduct 메뉴_치킨 = MenuProduct.of(치킨, 1L);
        MenuProduct 메뉴_콜라 = MenuProduct.of(콜라, 2L);
        MenuProducts 메뉴_상품_목록 = MenuProducts.from(Arrays.asList(메뉴_치킨, 메뉴_콜라));
        
        // when
        MenuPrice 총_가격 = 메뉴_상품_목록.getTotalPrice();
    
        // then
        assertThat(총_가격).isEqualTo(MenuPrice.from(22_000L));
    }

}
