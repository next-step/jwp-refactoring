package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private Product product1;
    private Product product2;
    private Product product3;

    private List<Product> products = new ArrayList<>();
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    public void setUp() {
        product1 = new Product("강정치킨", new BigDecimal(17000));
        product2 = new Product("순살치킨", new BigDecimal(15000));
        product3 = new Product("양념치킨", new BigDecimal(11000));
        products.add(product1);
        products.add(product2);
        products.add(product3);
        productService = new ProductService(productDao);
    }

    @Test
    @DisplayName("상품 등록 테스트")
    void createProduct() {
        mockSaveProduct(product1);
        mockSaveProduct(product2);
        mockSaveProduct(product3);

        checkProduct(productService.create(product1), product1);
        checkProduct(productService.create(product2), product2);
        checkProduct(productService.create(product3), product3);
    }

    @Test
    @DisplayName("상품목록 조회 테스트")
    void findProductList() {
        when(productDao.findAll()).thenReturn(products);
        assertThat(productService.list().size()).isEqualTo(products.size());
        List<String> productNames = productService.list().stream()
                .map(product -> product.getName())
                .collect(Collectors.toList());
        List<String> expectedProductNames = products.stream()
                .map(product -> product.getName())
                .collect(Collectors.toList());
        assertThat(productNames).containsExactlyElementsOf(expectedProductNames);
    }

    private void checkProduct(Product resultProduct, Product product) {
        assertThat(resultProduct.getName()).isEqualTo(product.getName());
        assertThat(resultProduct.getPrice()).isEqualTo(product.getPrice());
    }

    private void mockSaveProduct(Product product) {
        when(productDao.save(product)).thenReturn(product);
    }

}