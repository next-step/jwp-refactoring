package kitchenpos.product.application;

import kitchenpos.exception.IllegalPriceException;
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

import java.util.Arrays;
import java.util.List;

import static kitchenpos.utils.fixture.ProductFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    ProductService productService;

    private Product 치킨;
    private Product 피자;

    @BeforeEach
    void setUp() {
        치킨 = createProduct( 1L, "치킨", 15000);
        피자 = createProduct(2L, "피자", 20000);
    }

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void 상품_등록(){
        //given
        given(productRepository.save(any(Product.class))).willReturn(치킨);

        //when
        ProductRequest productRequest = ProductRequest.of(치킨.getName(), 치킨.getPrice());
        ProductResponse savedProduct = productService.create(productRequest);

        //then
        assertThat(savedProduct).isEqualTo(ProductResponse.from(치킨));

    }


    @DisplayName("상품의 가격은 0 이상이다")
    @Test
    void 상품_가격_검증(){
        //given
        ProductRequest invalidProductRequest = ProductRequest.of(치킨.getName(), -15000);

        //then
        assertThrows(IllegalPriceException.class, () -> productService.create(invalidProductRequest));
    }

    @DisplayName("상품의 목록을 조회할 수 있다")
    @Test
    void 상품_목록_조회() {
        //given
        given(productRepository.findAll()).willReturn(Arrays.asList(치킨, 피자));

        //when
        List<ProductResponse> list = productService.list();

        //then
        assertThat(list).containsExactly(ProductResponse.from(치킨), ProductResponse.from(피자));
    }
}