package kitchenpos.product.application;

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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductResponse 강정치킨;
    private ProductResponse 후라이드;

    @BeforeEach
    void setUp() {
        강정치킨 = new ProductResponse(1L, "강정치킨", BigDecimal.valueOf(17000));
        후라이드 = new ProductResponse(2L, "후라이드", BigDecimal.valueOf(16000));
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createTest() {
        // given
        Product 강정치킨 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
        given(productRepository.save(any())).willReturn(강정치킨);

        // when
        ProductResponse createdProduct = productService.create(new ProductRequest("강정치킨", BigDecimal.valueOf(17000)));

        // then
        assertThat(createdProduct.getId()).isEqualTo(this.강정치킨.getId());
        assertThat(createdProduct.getName()).isEqualTo(this.강정치킨.getName());
        assertThat(createdProduct.getPrice()).isEqualTo(this.강정치킨.getPrice());
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다 : 상품의 가격은 0 원 이상이어야 한다.")
    @Test
    void createTest_wrongPrice() {
        // when & then
        assertThatThrownBy(() -> productService.create(new ProductRequest("후라이드", BigDecimal.valueOf(-1000))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void listTest() {
        // given
        Product 강정치킨 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
        Product 후라이드 = new Product(2L, "후라이드", BigDecimal.valueOf(16000));
        given(productRepository.findAll()).willReturn(Arrays.asList(강정치킨, 후라이드));

        // when
        List<ProductResponse> products = productService.list();

        // then
        assertAll(
                () -> assertThat(products).hasSize(2),
                () -> assertThat(products.get(0).getId()).isEqualTo(this.강정치킨.getId()),
                () -> assertThat(products.get(0).getName()).isEqualTo(this.강정치킨.getName()),
                () -> assertThat(products.get(0).getPrice()).isEqualTo(this.강정치킨.getPrice()),
                () -> assertThat(products.get(1).getId()).isEqualTo(this.후라이드.getId()),
                () -> assertThat(products.get(1).getName()).isEqualTo(this.후라이드.getName()),
                () -> assertThat(products.get(1).getPrice()).isEqualTo(this.후라이드.getPrice())
        );
    }
}
