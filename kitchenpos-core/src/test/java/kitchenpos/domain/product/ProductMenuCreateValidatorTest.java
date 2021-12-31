package kitchenpos.domain.product;

import kitchenpos.domain.menu.domain.Menu;
import kitchenpos.domain.menu.domain.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.product.validator.ProductMenuCreateValidator;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 가격 유효성 검사 테스트")
@ExtendWith(MockitoExtension.class)
class ProductMenuCreateValidatorTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductMenuCreateValidator menuPriceValidator;

    @DisplayName("메뉴의 가격은 포함된 상품들의 금액의 합 보다 크지 않을경우 유효하다.")
    @Test
    void validate() {
        // given
        final Menu menu = Menu.of("메뉴이름", 6000, 1L,
                Arrays.asList(
                        MenuProduct.of(1L, 3),
                        MenuProduct.of(2L, 3))
        );
        List<Product> expectedProducts = Arrays.asList(
                Product.generate(1L, "마일드참치캔", 1000),
                Product.generate(2L, "고추참치캔", 2000)
        );
        given(productRepository.findAllByIds(Arrays.asList(1L, 2L))).willReturn(expectedProducts);

        // when
        final Executable executable = () -> menuPriceValidator.validate(menu);
        // then
        assertDoesNotThrow(executable);
    }

    @DisplayName("메뉴의 가격은 포함된 상품들의 금액의 합 보다 클 경우 유효하지 못하다.")
    @Test
    void validateByIllegalPrice() {
        // given
        final Menu menu = Menu.of("메뉴이름", 16000, 1L,
                Arrays.asList(
                        MenuProduct.of(1L, 3),
                        MenuProduct.of(2L, 3))
        );
        List<Product> expectedProducts = Arrays.asList(
                Product.generate(1L, "마일드참치캔", 1000),
                Product.generate(2L, "고추참치캔", 2000)
        );
        given(productRepository.findAllByIds(Arrays.asList(1L, 2L))).willReturn(expectedProducts);

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> menuPriceValidator.validate(menu);
        // then
        assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴상품그룹의 메뉴 아이디에 따른 메뉴 상품의 상품이 존재하지 않을 경우")
    @Test
    void createByNotExistProduct() {
        // given
        final Menu menu = Menu.of("메뉴이름", 16000, 1L,
                Arrays.asList(
                        MenuProduct.of(1L, 3),
                        MenuProduct.of(2L, 3))
        );
        List<Product> expectedProducts = Arrays.asList(
                Product.generate(1L, "마일드참치캔", 1000),
                Product.generate(3L, "고추참치캔", 2000)
        );
        given(productRepository.findAllByIds(Arrays.asList(1L, 2L))).willReturn(expectedProducts);

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> menuPriceValidator.validate(menu);
        // then
        assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
    }

}
