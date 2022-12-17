package kitchenpos.product.application;

import kitchenpos.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품")
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Product 상품1;
    private Product 상품2;
    private Product 상품3;

    @BeforeEach
    void setUp() {
        상품1 = generateProduct("product1", 가격(1000));
        상품2 = generateProduct("product2", 가격(1500));
        상품3 = generateProduct("product3", 가격(1300));
    }

    @Test
    @DisplayName("전체 상품을 조회할 수 있다.")
    void projectTest1() {
        List<Product> 상품들 = 상품들_생성();

        given(productRepository.findAll()).willReturn(상품들);

        List<ProductResponse> 조회된_상품들 = productService.list();

        assertThat(조회된_상품들.size()).isEqualTo(상품들.size());
    }

    @Test
    @DisplayName("새로운 상품을 추가할 수 있다.")
    void projectTest2() {
        given(productRepository.save(any(Product.class))).willReturn(상품1);

        ProductRequest 추가할_상품 = new ProductRequest(상품1.getName(), 상품1.getPrice());
        ProductResponse 추가된_상품 = productService.create(추가할_상품);

        assertThat(추가된_상품.getName()).isEqualTo(상품1.getName());
    }

    @Test
    @DisplayName("새로운 상품 추가 : 상품 가격은 필수값이며, 음수여서는 안된다.")
    void projectTest3() {
        Product 가격이_NULL인_상품 = generateProduct("product1", null);
        Product 가격이_음수인_상품 = generateProduct("product2", 가격(-1));

        assertThatThrownBy(() -> productService.create(new ProductRequest(가격이_NULL인_상품.getName(), 가격이_NULL인_상품.getPrice())))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> productService.create(new ProductRequest(가격이_음수인_상품.getName(), 가격이_음수인_상품.getPrice())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static Product generateProduct(String name, BigDecimal price) {
        return new Product(name, new Price(price));
    }

    private List<Product> 상품들_생성() {
        return Arrays.asList(상품1, 상품2, 상품3);
    }

    private BigDecimal 가격(int price) {
        return new BigDecimal(price);
    }

}
