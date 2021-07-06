package kitchenpos.application.unit;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.repository.ProductRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 등록")
    @Test
    public void 상품_등록_확인() throws Exception {
        //given
        Product returnProduct = 상품_등록됨(1L, "후라이드", BigDecimal.valueOf(18_000));
        given(productRepository.save(any(Product.class))).willReturn(returnProduct);

        //when
        ProductRequest productRequest = new ProductRequest("후라이드", BigDecimal.valueOf(18_000));
        ProductResponse saveProduct = productService.create(productRequest);

        //then
        assertThat(saveProduct.getId()).isNotNull();
    }

    @DisplayName("상품 등록 예외 - 가격 입력을 안했을 경우")
    @Test
    public void 가격입력안했을경우_상품등록_예외확인() throws Exception {
        //given
        ProductRequest productRequest = new ProductRequest("후라이드", null);

        //when
        //then
        assertThatThrownBy(() -> productService.create(productRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 등록 예외 - 가격이 음수인 경우")
    @Test
    public void 가격이음수인경우_상품등록_예외확인() throws Exception {
        //given
        ProductRequest productRequest = new ProductRequest("후라이드", BigDecimal.valueOf(-1));

        //when
        //then
        assertThatThrownBy(() -> productService.create(productRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록 조회")
    @Test
    public void 상품_목록_조회() throws Exception {
        //given
        Product product1 = 상품_등록됨(1L, "후라이드1", BigDecimal.valueOf(18_000));
        Product product2 = 상품_등록됨(2L, "후라이드2", BigDecimal.valueOf(18_000));
        Product product3 = 상품_등록됨(3L, "후라이드3", BigDecimal.valueOf(18_000));
        given(productRepository.findAll()).willReturn(Arrays.asList(product1, product2, product3));

        //when
        List<ProductResponse> productResponses = productService.list();

        //then
        assertThat(productResponses.size()).isEqualTo(3);
    }

    public static Product 상품_생성(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static Product 상품_등록됨(Long id, String name, BigDecimal price) {
        Product product = 상품_생성(name, price);
        product.setId(id);
        return product;
    }
}
