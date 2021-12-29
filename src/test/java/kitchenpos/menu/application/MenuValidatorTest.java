package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;

@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {
    
    @Mock
    private ProductService productService;
    
    @InjectMocks
    private MenuValidator menuValidator;
    
    @DisplayName("메뉴 가격은 포함된 상품들의 총 금액보다 클 수 없다")
    @Test
    void 메뉴_가격_상품_가격_비교() {
        // given
        Menu 메뉴 = Menu.of("치킨세트", 24000L, MenuGroup.from("메뉴그룹"));
        Product 치킨 = Product.of("치킨", 18000L);
        Product 콜라 = Product.of("치킨", 2000L);
        MenuProduct 메뉴_치킨 = MenuProduct.of(1L, 1L);
        MenuProduct 메뉴_콜라 = MenuProduct.of(2L, 2L);
        메뉴.addMenuProducts(Arrays.asList(메뉴_치킨, 메뉴_콜라));
        
        given(productService.findById(1L)).willReturn(치킨);
        given(productService.findById(2L)).willReturn(콜라);

        // when, then
        assertThatThrownBy(() -> {
            menuValidator.checkTotalPrice(메뉴);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("메뉴 가격이 상품 가격의 합보다 큽니다");
    }
}
