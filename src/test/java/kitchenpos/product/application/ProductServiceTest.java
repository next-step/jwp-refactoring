package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.persistence.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    @DisplayName("상품가격이 없는경우 예외발생")
    @Test
    public void throwsExceptionWhenNullPrice() {
        assertThatThrownBy(() -> productService.create(new ProductRequest()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품가격이 0보다 작은경우 예외발생")
    @ParameterizedTest
    @ValueSource(longs = {-1, -2, -3, -100, -999})
    public void throwsExceptionWhenNegativePrice(long price) {
        ProductRequest product = new ProductRequest("product", BigDecimal.valueOf(price));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 추가할 경우 추가된 상품정보를 반환")
    @ParameterizedTest
    @ValueSource(longs = {1, 342, 21, 3423, 4})
    public void returnProduct(long id) {
        ProductRequest productRequest = new ProductRequest("product", BigDecimal.valueOf(1500l));

        Product mockProduct = Product.builder()
                .price(ProductPrice.of(BigDecimal.valueOf(1500)))
                .id(id)
                .build();
        doReturn(mockProduct).when(productRepository).save(any(Product.class));

        ProductResponse savedProduct = productService.create(productRequest);

        assertThat(savedProduct.getId()).isEqualTo(id);
    }

    @DisplayName("상품목록을 조회할 경우 저장된 상품목록반환")
    @Test
    public void returnProducts() {
        Product product = Product.builder()
                .id(1l)
                .price(ProductPrice.of(BigDecimal.valueOf(1000)))
                .build();
        List<Product> mockProducts = Arrays.asList(product);
        doReturn(mockProducts).when(productRepository).findAll();

        List<ProductResponse> products = productService.list();

        List<BigDecimal> productPrice = products.stream().map(ProductResponse::getPrice).collect(Collectors.toList());
        List<Long> productIds = products.stream().map(ProductResponse::getId).collect(Collectors.toList());
        assertAll(
                () -> assertThat(productPrice).allMatch(bigDecimal -> bigDecimal.intValue() >= 1000),
                () -> assertThat(productIds).containsAnyOf(1l, 2l, 3l, 4l, 5l));
    }
}
