package kitchenpos.Product.application;

import kitchenpos.product.application.ProductService;
import kitchenpos.common.Name;
import kitchenpos.common.Price;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;

@DisplayName("상품 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    public Product 상품1;
    public Product 상품2;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {

        상품1 = 상품_생성(1L, "반반콤보", new BigDecimal(18000));

        상품2 = 상품_생성(2L, "허니콤보", new BigDecimal(18000));
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void createProduct() {
        given(productRepository.save(any())).willReturn(상품1);

        ProductResponse createdProduct = 상품_생성_요청();

        상품_생성_요청됨(createdProduct);
    }

    @DisplayName("상품의 가격이 0원 이상일때 등록이 가능하다.")
    @Test
    void checkProductPrice() {
        상품_가격_0원_이하일_경우_예외_발생힘();
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void searchProductList() {
        given(productRepository.findAll()).willReturn(Arrays.asList(상품1, 상품2));

        List<ProductResponse> products = 상품_목록_조회_요청();

        상품_목록_조회됨(products);
    }

    public static Product 상품_생성(Long id, String name, BigDecimal price) {
        return new Product(id, new Name(name), new Price(price));
    }

    private ProductResponse 상품_생성_요청() {
        return productService.create(new ProductRequest(상품1.getName(), 상품1.getPrice()));
    }

    private void 상품_생성_요청됨(ProductResponse createdProduct) {
        assertThat(createdProduct.getId()).isEqualTo(this.상품1.getId());
        assertThat(createdProduct.getName()).isEqualTo(this.상품1.getName());
        assertThat(createdProduct.getPrice()).isEqualTo(this.상품1.getPrice());
    }

    private void 상품_가격_0원_이하일_경우_예외_발생힘() {
        assertThatThrownBy(
                () -> productService.create(new ProductRequest(상품1.getName(), new BigDecimal(-10))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void 상품_목록_조회됨(List<ProductResponse> products) {
        assertThat(products.get(0).getName()).isEqualTo(상품1.getName());
        assertThat(products.get(1).getName()).isEqualTo(상품2.getName());
    }

    private List<ProductResponse> 상품_목록_조회_요청() {
        return productService.list();
    }
}
