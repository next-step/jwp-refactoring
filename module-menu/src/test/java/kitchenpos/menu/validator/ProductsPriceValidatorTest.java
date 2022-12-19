package kitchenpos.menu.validator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
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
        BDDMockito.given(menu.getPrice()).willReturn(new BigDecimal(20000));
        BDDMockito.given(menu.getMenuProducts()).willReturn(Collections.singletonList(menuProduct));
        BDDMockito.given(productRepository.findById(ArgumentMatchers.any())).willReturn(Optional.empty());

        ThrowingCallable 등록되지_않은_상품을_메뉴로_지정_한_경우 = () -> validator.validate(menu);

        Assertions.assertThatIllegalArgumentException().isThrownBy(등록되지_않은_상품을_메뉴로_지정_한_경우);
    }

    @Test
    void 메뉴의_가격은_메뉴상품들_가격의_합보다_낮아야_한다() {
        MenuProduct menuProduct = new MenuProduct(menu, 1L, 1L);
        Product product = new Product(1L, "상품", new BigDecimal(16000));
        ProductsPriceValidator validator = new ProductsPriceValidator(productRepository);
        BDDMockito.given(menu.getPrice()).willReturn(new BigDecimal(20000));
        BDDMockito.given(menu.getMenuProducts()).willReturn(Collections.singletonList(menuProduct));
        BDDMockito.given(productRepository.findById(ArgumentMatchers.any())).willReturn(Optional.of(product));

        ThrowingCallable 메뉴의_가격이_메뉴상품들_가격보다_큰_경우 = () -> validator.validate(menu);

        Assertions.assertThatIllegalArgumentException().isThrownBy(메뉴의_가격이_메뉴상품들_가격보다_큰_경우);
    }
}
