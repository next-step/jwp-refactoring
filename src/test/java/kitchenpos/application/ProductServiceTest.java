package kitchenpos.application;

import kitchenpos.dao.ProductRepository;
import kitchenpos.dto.ProductRequest;
import kitchenpos.exception.NullPriceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private ProductRequest productRequest;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest("양념치킨", new BigDecimal(16_000));
    }

    @DisplayName("가격이 없는 상품을 생성한다")
    @Test
    public void createNullProceProductTest() {
        productRequest = new ProductRequest("양념치킨", null);
        ProductService productService = new ProductService(productRepository);

        assertThatThrownBy(() -> productService.create(productRequest)).isInstanceOf(NullPriceException.class);
    }
}
