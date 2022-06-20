package kitchenpos.product.application;

import static kitchenpos.helper.ProductFixtures.제육덮밥;
import static kitchenpos.helper.ProductFixtures.제육덮밥_가격NULL;
import static kitchenpos.helper.ProductFixtures.제육덮밥_가격마이너스;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 관련 Service 단위 테스트 - Stub")
@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

    @Mock
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        //given
        long generateProductId = 1;
        ProductRequest request = 제육덮밥;
        doAnswer(invocation -> new Product(generateProductId, 제육덮밥.getName(), 제육덮밥.getPrice()))
                .when(productRepository).save(any());

        //when
        ProductResponse result = productService.create(제육덮밥);

        //then
        assertThat(result.getId()).isEqualTo(generateProductId);
        assertThat(result.getPrice()).isEqualTo(request.getPrice());
    }

    @DisplayName("상품 가격이 null 이거나 0원 미만이면 등록 할 수 없다.")
    @Test
    void create_price_null_or_less_then_zero() {
        //when then
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(() -> productService.create(제육덮밥_가격NULL)),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> productService.create(제육덮밥_가격마이너스))
        );
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        //given
        Product product1 = new Product("돈까스",9000);
        Product product2 = new Product("나베",15000);
        Product product3 = new Product("김치찌개",13000);
        given(productRepository.findAll()).willReturn(Arrays.asList(product1, product2, product3));

        //when
        List<ProductResponse> results = productService.list();

        //then
        assertThat(results.stream().map(ProductResponse::getName).collect(Collectors.toList()))
                .contains("돈까스","나베","김치찌개");
    }
}
