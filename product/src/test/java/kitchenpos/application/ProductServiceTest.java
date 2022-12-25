package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 등록 API")
    @Test
    void create_price() {
        // given
        String name = "상품";
        BigDecimal price = new BigDecimal(17000);
        ProductRequest productRequest = productRequest(name, price);
        given(productRepository.save(any())).willReturn(savedProduct(1L, name, price));

        // when
        ProductResponse actual = productService.create(productRequest);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(name),
            () -> assertThat(actual.getPrice()).isEqualTo(price)
        );
    }

    @DisplayName("상품 목록 조회 API")
    @Test
    void list() {
        // given
        Product savedProduct1 = savedProduct(1L, "상품1", new BigDecimal(17000));
        Product savedProduct2 = savedProduct(2L, "상품2", new BigDecimal(17000));
        given(productRepository.findAll()).willReturn(Arrays.asList(savedProduct1, savedProduct2));

        // when
        List<Product> products = productRepository.findAll();

        // then
        assertThat(products).hasSize(2);
    }
}
