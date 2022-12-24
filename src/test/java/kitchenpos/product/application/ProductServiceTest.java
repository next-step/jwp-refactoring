package kitchenpos.product.application;

import kitchenpos.ServiceTest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.common.vo.Price.PRICE_MINIMUM_EXCEPTION_MESSAGE;
import static kitchenpos.common.vo.Price.PRICE_NOT_NULL_EXCEPTION_MESSAGE;
import static kitchenpos.product.domain.fixture.ProductFixture.productA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 서비스")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest extends ServiceTest {


    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        super.setUp();
        productService = new ProductService(productRepository);
    }

    @DisplayName("상품 생성")
    @Test
    void create() {

        Product product = productA();

        given(productRepository.save(any())).willReturn(product);

        ProductResponse response = productService.create(new ProductCreateRequest(product.getName().getName(), product.getPrice()));

        assertAll(
                () -> assertEquals(0, response.getPrice().compareTo(product.getPrice())),
                () -> assertThat(response.getName()).isEqualTo(product.getName())
        );
    }

    @DisplayName("상품 생성 / 가격을 필수로 갖는다.")
    @Test
    void create_fail_priceNull() {
        assertThatThrownBy(() -> productService.create(new ProductCreateRequest("상품A", null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("상품 생성 / 가격은 0원보다 작을 수 없다.")
    @Test
    void create_fail_minimumPrice() {
        assertThatThrownBy(() -> productService.create(new ProductCreateRequest("상품A", BigDecimal.valueOf(-1))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_MINIMUM_EXCEPTION_MESSAGE);
    }

    @DisplayName("상품 목록 조회")
    @Test
    void list() {
        given(productRepository.findAll()).willReturn(Collections.singletonList(productA()));
        assertThat(productService.list()).hasSize(1);
    }
}

