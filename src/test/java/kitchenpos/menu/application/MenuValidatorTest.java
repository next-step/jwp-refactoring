package kitchenpos.menu.application;

import kitchenpos.common.exception.MenuProductSumPriceException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 벨리데이터 관련 테스트")
@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    @Mock
    private ProductService productService;

    private MenuValidator menuValidator;

    private MenuRequest 가격이_말도_안되는_짜장면_요청;
    private MenuProduct 짜장면_하나;
    private MenuProduct 짜장면_두개;
    private Product 짜장면_상품;


    @BeforeEach
    void setUp() {
        menuValidator = new MenuValidator(productService);

        짜장면_상품 = new Product("짜장면", new BigDecimal(1000));
        짜장면_하나 = new MenuProduct(1L, new Menu(), 짜장면_상품.getId(), 1);
        짜장면_두개 = new MenuProduct(2L, new Menu(), 짜장면_상품.getId(), 2);
        가격이_말도_안되는_짜장면_요청 = new MenuRequest("짜장면", 1000, 1L, Lists.newArrayList(짜장면_하나, 짜장면_두개));
    }


    @DisplayName("메뉴의 가격이 상품들의 총 가격의 합보다 커야한다.")
    @Test
    void createMenuSumBiggerThanPriceExceptionTest() {
        assertThatThrownBy(() -> {
            when(productService.findById(any())).thenReturn(짜장면_상품);

            // when
            메뉴의_조건을_확인한다(가격이_말도_안되는_짜장면_요청);

            // then
        }).isInstanceOf(MenuProductSumPriceException.class);
    }

    private void 메뉴의_조건을_확인한다(MenuRequest menuRequest) {
        this.menuValidator.validateMenu(new MenuGroup(), menuRequest);
    }
}