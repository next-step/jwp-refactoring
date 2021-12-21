package kitchenpos.product.application;

import kitchenpos.fixture.ProductFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product 강정치킨;
    @BeforeEach
    void setUp() {
        강정치킨 = ProductFixture.생성("강정치킨", new BigDecimal("7500"));
    }

    @DisplayName("상품을 이름, 가격으로 등록")
    @Test
    void create() {
        ProductRequest 강정치킨등록요청 = new ProductRequest(강정치킨.getName(), 강정치킨.getPrice());
        given(productRepository.save(any())).willReturn(강정치킨);

        ProductResponse create = productService.create(강정치킨등록요청);

        assertThat(create).isNotNull();
    }

    @DisplayName("상품 목록 조회")
    @Test
    void list() {
        given(productRepository.findAll()).willReturn(Arrays.asList(강정치킨));

        List<ProductResponse> products = productService.list();

        assertThat(products.size()).isEqualTo(1);
    }
}
