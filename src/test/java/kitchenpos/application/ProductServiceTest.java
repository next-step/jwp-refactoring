package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setup() {
        productService = new ProductService(productDao);
    }

    @DisplayName("요청한 상품의 가격이 null이거나 0원 미만인 경우 상품을 등록할 수 없다.")
    @ParameterizedTest
    @NullSource
    @MethodSource("createProductFailTestResource")
    void createProductFailTest(BigDecimal invalidPrice) {
        // given
        Product product = new Product();
        product.setPrice(invalidPrice);

        // when, then
        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }
    public static Stream<Arguments> createProductFailTestResource() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1)),
                Arguments.of(BigDecimal.valueOf(-2))
        );
    }

    @DisplayName("상품을 등록할 수 있다.")
    @ParameterizedTest
    @ValueSource(longs = { 0, 1 })
    void createProductTest(Long price) {
        // given
        String productName = "닭강정";
        Long productId = 1L;
        Product product = new Product();
        product.setName(productName);
        product.setPrice(BigDecimal.valueOf(price));

        Product saved = new Product();
        saved.setId(productId);
        saved.setName(productName);
        saved.setPrice(BigDecimal.valueOf(price));

        given(productDao.save(product)).willReturn(saved);

        // when
        Product created = productService.create(product);

        // then
        assertThat(created.getId()).isEqualTo(productId);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void getProductsTest() {
        // given
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = Arrays.asList(product1, product2);
        given(productDao.findAll()).willReturn(products);

        // when
        List<Product> foundProducts = productService.list();

        // then
        assertThat(foundProducts).contains(product1, product2);
    }
}