package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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
import static org.mockito.BDDMockito.given;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    private Product saveProduct;

    @BeforeEach
    public void setUp() {
        saveProduct = new Product();
        saveProduct.setName("후라이드");
        saveProduct.setPrice(BigDecimal.valueOf(18_000));
    }

    @DisplayName("상품 등록")
    @Test
    public void 상품_등록_확인() throws Exception {
        //given
        Product returnProduct = 상품_등록됨(1L, "후라이드", BigDecimal.valueOf(18_000));
        Product createProduct = 상품_생성("후라이드", BigDecimal.valueOf(18_000));
        given(productDao.save(createProduct)).willReturn(returnProduct);

        //when
        Product saveProduct = productService.create(createProduct);

        //then
        assertThat(saveProduct.getId()).isNotNull();
    }

    @DisplayName("상품 등록 예외 - 가격 입력을 안했을 경우")
    @Test
    public void 가격입력안했을경우_상품등록_예외확인() throws Exception {
        //given
        saveProduct.setPrice(null);

        //when
        //then
        assertThatThrownBy(() -> productService.create(saveProduct)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 등록 예외 - 가격이 음수인 경우")
    @Test
    public void 가격이음수인경우_상품등록_예외확인() throws Exception {
        //given
        saveProduct.setPrice(BigDecimal.valueOf(-1));

        //when
        //then
        assertThatThrownBy(() -> productService.create(saveProduct)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록 조회")
    @Test
    public void 상품_목록_조회() throws Exception {
        //given
        Product product1 = 상품_등록됨(1L, "이름1", BigDecimal.valueOf(1_000));
        Product product2 = 상품_등록됨(2L, "이름2", BigDecimal.valueOf(2_000));
        Product product3 = 상품_등록됨(3L, "이름3", BigDecimal.valueOf(3_000));
        given(productDao.findAll()).willReturn(Arrays.asList(product1, product2, product3));

        //when
        List<Product> products = productService.list();

        //then
        assertThat(products.size()).isEqualTo(3);
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
