package application;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kitchenpos.product.application.ProductService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성한다")
    @Test
    void create() {
        Product product = new Product(1L, "닭", BigDecimal.valueOf(1000L));
        given(productRepository.save(any(Product.class))).willReturn(product);

        ProductResponse response = productService.create(new ProductRequest("닭", BigDecimal.valueOf(1000L)));

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("닭");
    }

    @DisplayName("가격이 0원 이상이 아니면, 상품을 생성할 수 없다.")
    @Test
    void create_invalid_price() {
        assertThatThrownBy(() -> productService.create(new ProductRequest("닭", BigDecimal.valueOf(-1))))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.INVALID_PRICE.getMessage());
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        Product firstProduct = new Product(1L, "닭", BigDecimal.valueOf(1000L));
        Product secondProduct = new Product(2L, "콜라", BigDecimal.valueOf(500L));
        given(productRepository.findAll()).willReturn(Arrays.asList(firstProduct, secondProduct));

        List<ProductResponse> response = productService.list();

        assertThat(response).hasSize(2);
    }
}
