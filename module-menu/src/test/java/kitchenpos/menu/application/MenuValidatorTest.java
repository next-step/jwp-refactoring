package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    private MenuValidator menuValidator;
    private Menu menu;
    private Product product;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        menuValidator = new MenuValidator(productRepository);
        menu = new Menu.Builder("menu")
                .setPrice(Price.of(1_000L))
                .build();
    }

    @Test
    @DisplayName("제품의 합이 메뉴 가격보다 크거나 같은지 검증")
    void verifyValidProductsTotalPrice() {
        // given
        product = new Product(1L, "product", Price.of(1_100L));
        menu.addProduct(product.getId(), Quantity.of(1L));
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));
        // when && then
        menuValidator.validateProductsTotalPrice(menu);
    }

    @Test
    @DisplayName("제품의 합이 메뉴 가격보다 작으면 에러 발생")
    void verifyInvalidProductsTotalPrice() {
        // given
        product = new Product(1L, "product", Price.of(900L));
        menu.addProduct(product.getId(), Quantity.of(1L));
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuValidator.validateProductsTotalPrice(menu));
    }

    @Test
    @DisplayName("메뉴를 구성하는 상품들이 존재하지 않으면 에러 발생")
    void invalidNotExistProduct() {
        // given
        when(productRepository.findById(any())).thenReturn(Optional.empty());
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuValidator.findProductId(1L));
    }
}
