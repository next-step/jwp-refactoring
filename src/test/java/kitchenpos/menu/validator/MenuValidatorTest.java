package kitchenpos.menu.validator;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private Menu menu;
    private MenuValidator menuValidator;

    @BeforeEach
    void setUp() {
        menuValidator = new MenuValidator(menuGroupRepository, productRepository);
    }

    @Test
    void 메뉴_등록시_등록되어_있는_메뉴_그룹만_지정할_수_있다() {
        given(menuGroupRepository.findById(any())).willReturn(Optional.empty());

        ThrowingCallable 등록되지_않은_메뉴_그룹_지정_할_경우 = () -> menuValidator.validateCreation(1L);

        assertThatIllegalArgumentException().isThrownBy(등록되지_않은_메뉴_그룹_지정_할_경우);
    }

    @Test
    void 등록되지_않은_상품은_메뉴로_지정할_수_없다() {
        given(productRepository.findById(any())).willReturn(Optional.empty());
        given(menu.getMenuProducts()).willReturn(Collections.singletonList(new MenuProduct(null, 1L, 1)));

        ThrowingCallable 등록되지_않은_상품을_메뉴로_지정_한_경우 = () -> menuValidator.validateProductsPrice(menu);

        assertThatIllegalArgumentException().isThrownBy(등록되지_않은_상품을_메뉴로_지정_한_경우);
    }

    @Test
    void 메뉴의_가격은_메뉴_상품들_가격의_합보다_낮아야_한다() {
        given(productRepository.findById(any())).willReturn(Optional.of(new Product(1L, "상품", new BigDecimal(16000))));
        given(menu.getMenuProducts()).willReturn(Collections.singletonList(new MenuProduct(null, 1L, 1)));
        given(menu.getPrice()).willReturn(new BigDecimal(20000));

        ThrowingCallable 메뉴_가격이_상품들_가격의_합보다_높은_경우 = () -> menuValidator.validateProductsPrice(menu);

        assertThatIllegalArgumentException().isThrownBy(메뉴_가격이_상품들_가격의_합보다_높은_경우);
    }
}
