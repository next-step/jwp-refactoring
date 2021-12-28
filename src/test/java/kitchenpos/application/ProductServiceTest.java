package kitchenpos.application;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.exception.NullPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @DisplayName("가격이 없는 상품을 생성한다")
    @Test
    public void createNullProceProductTest() {
        ProductRequest nullPriceProductRequest = new ProductRequest("양념치킨", null);
        ProductService productService = new ProductService(productRepository);

        assertThatThrownBy(() -> productService.create(nullPriceProductRequest)).isInstanceOf(NullPriceException.class);
    }
}
