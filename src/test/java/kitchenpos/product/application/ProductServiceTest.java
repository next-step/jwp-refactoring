package kitchenpos.product.application;

import static kitchenpos.utils.DomainFixtureFactory.createProduct;
import static kitchenpos.utils.DomainFixtureFactory.createProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;
    private Product 피자;
    private Product 스파게티;

    @BeforeEach
    void setUp() {
        피자 = createProduct(null, "피자", BigDecimal.valueOf(20000L));
        스파게티 = createProduct(2L, "스파게티", BigDecimal.valueOf(20000L));
    }

    @DisplayName("상품 생성 테스트")
    @Test
    void create() {
        ProductRequest productRequest = createProductRequest("피자", BigDecimal.valueOf(20000L));
        given(productRepository.save(productRequest.toProduct())).willReturn(피자);
        ProductResponse productResponse = productService.create(productRequest);
        assertAll(
                () -> assertThat(productResponse.getName()).isEqualTo("피자"),
                () -> assertThat(productResponse.getPrice()).isEqualTo(BigDecimal.valueOf(20000L)
                ));
    }

    @DisplayName("상품 생성시 가격이 없는 경우 테스트")
    @Test
    void createWithPriceNull() {
        ProductRequest productRequest = createProductRequest("피자", null);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(productRequest));
    }

    @DisplayName("상품 생성시 가격이 0원 아래인 경우 테스트")
    @Test
    void createWithPriceUnderZero() {
        ProductRequest productRequest = createProductRequest("피자", BigDecimal.valueOf(-100));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(productRequest));
    }

    @DisplayName("상품 목록 조회 테스트")
    @Test
    void list() {
        given(productRepository.findAll()).willReturn(Lists.newArrayList(피자, 스파게티));
        List<ProductResponse> products = productService.list();
        assertThat(products).containsExactlyElementsOf(
                Lists.newArrayList(ProductResponse.from(피자), ProductResponse.from(스파게티)));
    }

    @DisplayName("상품 찾기 테스트")
    @Test
    void findProduct() {
        given(productRepository.findById(1L)).willReturn(Optional.ofNullable(피자));
        Product product = productService.findProduct(1L);
        assertThat(product).isEqualTo(피자);
    }

    @DisplayName("상품 찾을 수 없음")
    @Test
    void findProductEmpty() {
        given(productRepository.findById(1L)).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.findProduct(1L))
                .withMessage("상품을 찾을 수 없습니다.");
    }
}
