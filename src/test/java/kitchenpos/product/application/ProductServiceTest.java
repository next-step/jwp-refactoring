package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.utils.domain.ProductObjects;

@DisplayName("상품 서비스")
//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private List<Product> products;
    private Product createProduct;
    private ProductRequest createRequest;

    @BeforeEach
    void setUp() {
        ProductObjects productObjects = new ProductObjects();
        createProduct = productObjects.getProduct1();
        createRequest = productObjects.getProductRequest1();
        products = productObjects.getProducts();
    }

    @Test
    @DisplayName("상품 전체 조회")
    void find_allProducts() {
        // mocking
        when(productRepository.findAll()).thenReturn(products);

        // when
        List<ProductResponse> resultProducts = productService.findAllProducts();

        // then
        assertThat(resultProducts.size()).isEqualTo(products.size());
    }

//    @Test
//    @DisplayName("상품 전체 조회")
//    void find_allProducts1() {
//        // mocking
//        when(productDao.findAll()).thenReturn(products);
//
//        // when
//        List<Product> resultProducts = productService.list();
//
//        // then
//        assertThat(resultProducts.size()).isEqualTo(products.size());
//    }

    @Test
    @DisplayName("상품 등록")
    void create_product() {
        // given
        given(productRepository.save(any(Product.class))).willReturn(createProduct);

        // when
        ProductResponse productResponse = productService.create(createRequest);

        // then
        assertThat(productResponse.getId()).isEqualTo(createProduct.getId());
    }

//    @Test
//    @DisplayName("상품 등록")
//    void create_product1() {
//        // mocking
//        when(productDao.save(any(Product.class))).thenReturn(createProduct);
//
//        // when
//        Product resultProduct = productService.create(createProduct);
//
//        // then
//        assertThat(resultProduct).isSameAs(createProduct);
//    }

    // move to ProductTest
//    @TestFactory
//    @DisplayName("상품 등록 예외 처리")
//    List<DynamicTest> create_exception() {
//        return Arrays.asList(
//                dynamicTest("금액이 입력되지 않은 경우 오류 발생.", () -> {
//                    // given
//                    ProductRequest productRequest = new ProductRequest("A", null);
//
//                    // then
//                    assertThatThrownBy(() -> productService.create1(productRequest))
//                            .isInstanceOf(IllegalArgumentException.class)
//                            .hasMessage("금액은 Null이 아닌 0 이상의 금액이어야합니다.");
//                }),
//                dynamicTest("금액에 음수 입력될 경우 오류 발생.", () -> {
//                    // given
//                    ProductRequest productRequest = new ProductRequest("A", BigDecimal.valueOf(-1));
//
//                    // then
//                    assertThatThrownBy(() -> productService.create1(productRequest))
//                            .isInstanceOf(IllegalArgumentException.class)
//                            .hasMessage("금액은 Null이 아닌 0 이상의 금액이어야합니다.");
//                })
//        );
//    }
}
