package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품 관련 기능")
public class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;
    private Product product;

    @BeforeEach
    void setUp() {
        product = 상품_등록(1L, "짜장면", 5000);
    }

    @Test
    @DisplayName("상품을 등록한다.")
    void createProduct() {
        // given
        //given(productDao.save(any())).willReturn(product);

        given(productRepository.save(any())).willReturn(product);

        // when
        Product createProduct = productService.create(product);

        // then
        assertThat(createProduct).isNotNull();
    }

    @ParameterizedTest(name = "가격이 null 또는 음수인 경우 등록 실패한다.")
    @NullSource
    @ValueSource(strings = {"-100"})
    void createProductWithInValidPrice(BigDecimal price) {
        // given
        product.setPrice(price);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            productService.create(product);
        });
    }

    @Test
    @DisplayName("상품을 조회한다.")
    void getProduct() {
        // given
        given(productDao.findAll()).willReturn(Arrays.asList(product));

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).isNotNull();
    }

    public static Product 상품_등록(Long id, String name, int price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(new BigDecimal(price));
        return product;
    }
}
