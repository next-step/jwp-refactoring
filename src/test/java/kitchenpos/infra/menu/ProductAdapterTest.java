package kitchenpos.infra.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.ProductPrice;
import kitchenpos.domain.menu.exceptions.InvalidMenuPriceException;
import kitchenpos.domain.menu.exceptions.ProductEntityNotFoundException;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductAdapterTest {
    private ProductAdapter productAdapter;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        productAdapter = new ProductAdapter(productRepository);
    }

    @DisplayName("안전하게 존재하는 상품의 가격을 받아올 수 있다.")
    @Test
    void getProductPriceTest() {
        // given
        Long productId = 1L;
        BigDecimal price = BigDecimal.ONE;

        given(productRepository.findById(productId)).willReturn(Optional.of(new Product("product", price)));

        // when
        BigDecimal productPrice = productAdapter.getProductPrice(productId);

        // then
        assertThat(productPrice).isEqualTo(price);
    }

    @DisplayName("존재하지 않는 상품의 가격을 찾으려 할 경우 예외 발생")
    @Test
    void getProductPriceFailTest() {
        // given
        Long notExistId = 4L;

        // when, then
        assertThatThrownBy(() -> productAdapter.getProductPrice(notExistId))
                .isInstanceOf(ProductEntityNotFoundException.class)
                .hasMessage("존재하지 않는 상품입니다.");
    }

    @DisplayName("메뉴 상품들의 가격 총합보다 메뉴의 가격이 비싸면 예외 발생")
    @Test
    void isValidMenuPriceTest() {
        // given
        BigDecimal menuPrice = BigDecimal.valueOf(10000000);
        List<MenuProduct> menuProducts = Arrays.asList(MenuProduct.of(1L, 1L), MenuProduct.of(2L, 2L));
        Menu menu = Menu.of("menu", menuPrice, 1L, menuProducts);

        given(productRepository.findById(1L)).willReturn(Optional.of(new Product("product", BigDecimal.ONE)));
        given(productRepository.findById(2L)).willReturn(Optional.of(new Product("product", BigDecimal.ONE)));

        // when, then
        assertThatThrownBy(() -> productAdapter.isValidMenuPrice(menu))
                .isInstanceOf(InvalidMenuPriceException.class)
                .hasMessage("메뉴의 가격은 구성된 메뉴 상품들의 가격 합보다 비쌀 수 없습니다.");
    }

    @DisplayName("제시된 상품 ID들의 상품 가격들을 반환받을 수 있다.")
    @Test
    void getProductPricesTest() {
        Product product1 = new Product("product1", BigDecimal.ONE);
        Product product2 = new Product("product2", BigDecimal.TEN);
        ReflectionTestUtils.setField(product1, "id", 1L);
        ReflectionTestUtils.setField(product2, "id", 2L);
        // given
        List<Long> productIds = Arrays.asList(1L, 2L);
        given(productRepository.findAllById(productIds)).willReturn(Arrays.asList(product1, product2));

        // when
        List<ProductPrice> productPrices = productAdapter.getProductPrices(productIds);

        // then
        assertThat(productPrices).contains(
                new ProductPrice(1L, BigDecimal.ONE), new ProductPrice(2L, BigDecimal.TEN));
    }
}