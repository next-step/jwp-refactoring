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
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuValidator menuValidator;

    private MenuGroup menuGroup;
    private MenuProduct menuProduct;
    private Product product;
    private MenuProducts menuProducts;
    private MenuProductRequest menuProductRequest;

    @BeforeEach
    void setUp() {
        menuProductRequest = new MenuProductRequest(1L, 2);
        menuProduct = new MenuProduct(1L, 2);
        product = new Product(1L, "product", BigDecimal.ONE);
        menuGroup = new MenuGroup(1L, "menuGroup");
        menuProducts = new MenuProducts(Arrays.asList(menuProductRequest));
    }

    @DisplayName("메뉴 상품 가격의 합과 입력받은 가격 비교하여 입력받은 가격이 더 크면 에러")
    @Test
    void validateErrorWhenPriceIsBiggerThanSum() {
        // given
        ID로_상품_조회(Optional.of(product));
        Menu menu = new Menu(1L, "menu", new Price(BigDecimal.valueOf(3)), menuGroup, menuProducts);

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> menuValidator.validate(menu))
            .withMessage("각 상품 가격의 합보다 많은 가격입니다.");
    }

    @DisplayName("메뉴 상품이 없을 시 에러")
    @Test
    void validateErrorWhenProductNotExists() {
        // given
        ID로_상품_조회(Optional.empty());
        Menu menu = new Menu(1L, "menu", new Price(BigDecimal.ONE), menuGroup, menuProducts);

        // when and then
        assertThatExceptionOfType(KitchenposNotFoundException.class)
            .isThrownBy(() -> menuValidator.validate(menu));
    }

    @DisplayName("메뉴 상품 유효성 검사 성공")
    @Test
    void validate() {
        // given
        ID로_상품_조회(Optional.of(product));
        Menu menu = new Menu(1L, "menu", new Price(BigDecimal.ONE), menuGroup, menuProducts);

        // when and then
        assertThatCode(() -> menuValidator.validate(menu))
            .doesNotThrowAnyException();
    }

    private void ID로_상품_조회(Optional<Product> product) {
        Mockito.when(productRepository.findById(Mockito.anyLong()))
            .thenReturn(product);
    }

}