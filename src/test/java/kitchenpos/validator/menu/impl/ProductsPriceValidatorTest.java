package kitchenpos.validator.menu.impl;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductsPriceValidatorTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private Menu menu;
    @Mock
    private MenuProduct menuProduct;

    @Test
    void 등록되지_않은_상품은_메뉴로_지정할_수_없다() {
        ProductsPriceValidator validator = new ProductsPriceValidator(productRepository);
        given(menu.getPrice()).willReturn(new BigDecimal(20000));
        given(menu.getMenuProducts()).willReturn(Collections.singletonList(menuProduct));
        given(productRepository.findById(any())).willReturn(Optional.empty());

        ThrowingCallable 등록되지_않은_상품을_메뉴로_지정_한_경우 = () -> validator.validate(menu);

        assertThatIllegalArgumentException().isThrownBy(등록되지_않은_상품을_메뉴로_지정_한_경우);
    }

    @Test
    void 메뉴의_가격은_메뉴상품들_가격의_합보다_낮아야_한다() {
        MenuProduct menuProduct = new MenuProduct(menu, 1L, 1L);
        Product product = new Product(1L, "상품", new BigDecimal(16000));
        ProductsPriceValidator validator = new ProductsPriceValidator(productRepository);
        given(menu.getPrice()).willReturn(new BigDecimal(20000));
        given(menu.getMenuProducts()).willReturn(Collections.singletonList(menuProduct));
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        ThrowingCallable 메뉴의_가격이_메뉴상품들_가격보다_큰_경우 = () -> validator.validate(menu);

        assertThatIllegalArgumentException().isThrownBy(메뉴의_가격이_메뉴상품들_가격보다_큰_경우);
    }
}
