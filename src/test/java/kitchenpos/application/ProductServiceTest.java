package kitchenpos.application;

import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.exception.NegativePriceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private Product 양념치킨;
    private Product 순살치킨;
    private List<Product> list;

    @Mock
    private ProductRepository mockDao;

    @BeforeEach
    void setUp() {
        양념치킨 = new Product();
        양념치킨.setName("양념치킨");
        양념치킨.setPrice(Price.from(16_000));
        list = new ArrayList<>();
        list.add(양념치킨);
    }

    @DisplayName("상품을 생성한다")
    @Test
    public void createTest() {
        // given
        given(mockDao.save(양념치킨)).willReturn(양념치킨);

        // when
        ProductService productService = new ProductService(mockDao);
        Product returnedProduct = productService.create(양념치킨);

        // then
        assertThat(returnedProduct.getName()).isEqualTo("양념치킨");
    }

    @DisplayName("가격이 없는 상품을 생성한다")
    @Test
    public void createNullProceProductTest() {
        // given
        Product product = new Product();
        product.setName("양념치킨");

        // when
        ProductService productService = new ProductService(mockDao);

        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 음수인 상품을 생성한다")
    @Test
    public void createMinusProceProductTest() {
        // given
        순살치킨 = new Product();
        순살치킨.setName("순살치킨");

        // then
        assertThatThrownBy(() -> 순살치킨.setPrice(Price.from(-15_000))).isInstanceOf(NegativePriceException.class);
    }

    @DisplayName("상품을 조회한다")
    @Test
    public void listTest() {
        // given
        when(mockDao.findAll()).thenReturn(list);
        순살치킨 = new Product();
        순살치킨.setName("순살치킨");
        순살치킨.setPrice(Price.from(15_000));
        list.add(순살치킨);

        // when
        ProductService productService = new ProductService(mockDao);
        List<Product> products = productService.list();

        // then
        assertThat(products).containsExactlyElementsOf(Arrays.asList(양념치킨, 순살치킨));
    }
}
