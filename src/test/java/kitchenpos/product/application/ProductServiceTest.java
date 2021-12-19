package kitchenpos.product.application;

import kitchenpos.fixture.ProductTestFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.ProductRepository;
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
        강정치킨 = ProductTestFixture.생성("강정치킨", new BigDecimal("7500"));
        강정치킨.setName("강정치킨");
    }

    @DisplayName("상품을 이름, 가격으로 등록")
    @Test
    void create() {
        given(productRepository.save(강정치킨)).willReturn(강정치킨);

        Product createProduct = productService.create(강정치킨);

        assertThat(createProduct).isNotNull();
    }

    @DisplayName("상품 목록 조회")
    @Test
    void list() {
        given(productRepository.findAll()).willReturn(Arrays.asList(강정치킨));

        List<Product> products = productService.list();

        assertThat(products.size()).isEqualTo(1);
    }
}
