package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductDao productDao;

    ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @Test
    @DisplayName("상품 등록")
    void testCreateProduct() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(100));

        when(productDao.save(any()))
                .thenReturn(product);

        Product createdProduct = productService.create(product);

        assertThat(product).isEqualTo(createdProduct);
    }

    @Test
    @DisplayName("상품의 가격이 0보다 작으면 등록할 수 없음")
    void testCannotCreateProductWhenPriceBelowThanZero() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품의 가격이 존재하지 않으면 등록할 수 없음")
    void testCannotCreateProductWhenPriceNotExists() {
        Product product = new Product();

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 목록")
    void testGetProductList() {
        List<Product> expectedProducts = Lists.newArrayList(new Product(), new Product(), new Product());
        when(productDao.findAll())
            .thenReturn(expectedProducts);

        List<Product> actualProducts = productService.list();

        assertThat(actualProducts).containsExactlyInAnyOrderElementsOf(expectedProducts);
    }
}
