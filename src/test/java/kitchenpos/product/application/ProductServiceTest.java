package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = 상품_생성(1L, "피자", 25000);
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        //given
        given(productRepository.save(any())).willReturn(product);

        //when
        Product createdProduct = productService.create(product);

        //then
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getName()).isEqualTo(product.getName());
        assertThat(createdProduct.getPrice()).isEqualTo(product.getPrice());
    }

    @DisplayName("가격이 0 미만인 상품은 등록에 실패한다.")
    @Test
    void create_invalidPrice() {
        //given
        product.setPrice(BigDecimal.valueOf(-1));

        //when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        //given
        given(productRepository.findAll()).willReturn(Arrays.asList(product, 상품_생성(2L, "파스타", 15000)));

        //when
        List<Product> products = productService.list();

        //then
        assertThat(products).hasSize(2);
    }

    public static Product 상품_생성(long id, String name, int price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }
}
