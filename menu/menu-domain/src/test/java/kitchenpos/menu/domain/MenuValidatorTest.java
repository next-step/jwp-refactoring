package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.KitchenposException;
import kitchenpos.common.exception.KitchenposNotFoundException;
import kitchenpos.common.price.domain.Price;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuValidator menuValidator;

    private Product product;
    private MenuProducts menuProducts;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        menuProduct = new MenuProduct(1L, 2);
        product = new Product("product", BigDecimal.ONE);
        menuProducts = new MenuProducts(Arrays.asList(menuProduct));
    }

    @DisplayName("메뉴 상품 가격의 합과 입력받은 가격 비교하여 입력받은 가격이 더 크면 에러")
    @Test
    void validateErrorWhenPriceIsBiggerThanSum() {
        // given
        ID로_메뉴_그룹_존재여부_확인(true);
        ID로_상품_조회(product);
        Menu menu = new Menu(1L, "menu", new Price(BigDecimal.valueOf(3)), 1L, menuProducts);

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> menuValidator.validate(menu))
            .withMessage("각 상품 가격의 합보다 많은 가격입니다.");
    }

    @DisplayName("메뉴 상품이 없을 시 에러")
    @Test
    void validateErrorWhenProductNotExists() {
        // given
        ID로_메뉴_그룹_존재여부_확인(true);
        ID로_상품_조회(null);
        Menu menu = new Menu(1L, "menu", new Price(BigDecimal.ONE), 1L, menuProducts);

        // when and then
        assertThatExceptionOfType(KitchenposNotFoundException.class)
            .isThrownBy(() -> menuValidator.validate(menu));
    }

    @DisplayName("메뉴 그룹이 없을 시 에러")
    @Test
    void validateErrorWhenMenuGroupNotExists() {
        // given
        ID로_메뉴_그룹_존재여부_확인(false);
        Menu menu = new Menu(1L, "menu", new Price(BigDecimal.ONE), 1L, menuProducts);

        // when and then
        assertThatExceptionOfType(KitchenposNotFoundException.class)
            .isThrownBy(() -> menuValidator.validate(menu));
    }

    @DisplayName("메뉴 상품 유효성 검사 성공")
    @Test
    void validate() {
        // given
        ID로_메뉴_그룹_존재여부_확인(true);
        ID로_상품_조회(product);
        Menu menu = new Menu(1L, "menu", new Price(BigDecimal.ONE), 1L, menuProducts);

        // when and then
        assertThatCode(() -> menuValidator.validate(menu))
            .doesNotThrowAnyException();
    }

    private void ID로_메뉴_그룹_존재여부_확인(boolean expected) {
        Mockito.when(menuGroupRepository.existsById(Mockito.anyLong()))
            .thenReturn(expected);
    }

    private void ID로_상품_조회(Product product) {
        Mockito.when(productRepository.findById(Mockito.anyLong()))
            .thenReturn(Optional.ofNullable(product));
    }

}