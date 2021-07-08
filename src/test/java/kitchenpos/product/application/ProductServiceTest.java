package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.utils.domain.ProductObjects;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품 서비스")
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private List<Product> products;
    private Product createProduct;

    @BeforeEach
    void setUp() {
        ProductObjects productObjects = new ProductObjects();
        createProduct = productObjects.getProduct1();
        products = productObjects.getProducts();
    }

    @Test
    @DisplayName("상품 전체 조회")
    void find_allProducts() {
        // mocking
        when(productDao.findAll()).thenReturn(products);

        // when
        List<Product> resultProducts = productService.list();

        // then
        assertThat(resultProducts.size()).isEqualTo(products.size());
    }

    @Test
    @DisplayName("상품 등록")
    void create_product() {
        // mocking
        when(productDao.save(any(Product.class))).thenReturn(createProduct);

        // when
        Product resultProduct = productService.create(createProduct);

        // then
        assertThat(resultProduct).isSameAs(createProduct);
    }

    @TestFactory
    @DisplayName("상품 등록 예외 처리")
    List<DynamicTest> create_exception() {
        return Arrays.asList(
                dynamicTest("금액이 입력되지 않은 경우 오류 발생.", () -> {
                    // given
                    createProduct.setPrice(null);

                    // then
                    assertThatThrownBy(() -> productService.create(createProduct))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("금액에 음수 입력될 경우 오류 발생.", () -> {
                    // given
                    createProduct.setPrice(BigDecimal.valueOf(-1));

                    // then
                    assertThatThrownBy(() -> productService.create(createProduct))
                            .isInstanceOf(IllegalArgumentException.class);
                })
        );
    }
}
