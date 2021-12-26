package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 저장한다")
    @Test
    void testCreate() {
        // given
        ProductRequest 볶음짜장면 = new ProductRequest("볶음짜장면", 8000L);
        Product expectedProduct = new Product(1L, "볶음짜장면", 8000L);
        given(productRepository.save(any(Product.class))).willReturn(expectedProduct);

        // when
        ProductResponse product = productService.create(볶음짜장면);

        // then
        assertThat(product).isEqualTo(ProductResponse.of(expectedProduct));
    }

    @DisplayName("상품 가격은 0원 보다 커야한다")
    @Test
    void givenZeroPriceWhenSaveThenThrowException() {
        // given
        ProductRequest 볶음짜장면 = new ProductRequest("볶음짜장면", 0L);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> productService.create(볶음짜장면);

        // then
        assertThatThrownBy(callable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 상품을 조회한다")
    @Test
    void testFindAll() {
        // given
        List<Product> expectedProducts = Arrays.asList(new Product(1L, "볶음짜장면", 8000L));
        given(productRepository.findAll()).willReturn(expectedProducts);

        // when
        List<ProductResponse> products = productService.list();

        // then
        assertThat(products).isEqualTo(ProductResponse.ofList(expectedProducts));
    }
}
