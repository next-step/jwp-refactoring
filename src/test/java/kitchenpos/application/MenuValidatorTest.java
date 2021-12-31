package kitchenpos.application;

import kitchenpos.common.exceptions.GreaterProductSumPriceException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.TestMenuFactory;
import kitchenpos.fixture.TestMenuProductFactory;
import kitchenpos.fixture.TestProductFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    @Mock
    private ProductService productService;
    @InjectMocks
    private MenuValidator menuValidator;

    @DisplayName("메뉴의 가격이 상품의 가격의 합보다 높으면 안된다.")
    @Test
    void chekcOverPrice() {
        final Menu 메뉴 = TestMenuFactory.메뉴_생성됨(1L, "메뉴", 20001, "메뉴그룹");
        final Product 상품 = TestProductFactory.상품_생성됨(1L, "상품", 10000);
        final MenuProduct 메뉴상품 = TestMenuProductFactory.메뉴상품_생성됨(1L, 메뉴, 상품, 2);
        final List<MenuProduct> 메뉴상품_목록 = Collections.singletonList(메뉴상품);
        메뉴.addMenuProducts(메뉴상품_목록);

        given(productService.getById(anyLong())).willReturn(상품);

        assertThatThrownBy(() -> menuValidator.isOverPrice(메뉴))
                .isInstanceOf(GreaterProductSumPriceException.class);
    }
}
