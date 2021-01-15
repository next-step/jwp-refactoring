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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private Product 강정치킨;
    private Product 순살치킨;
    private Product 양념치킨;

    private List<Product> 상품리스트 = new ArrayList<>();
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    public void setUp() {
        강정치킨 = new Product("강정치킨", new BigDecimal(17000));
        순살치킨 = new Product("순살치킨", new BigDecimal(15000));
        양념치킨 = new Product("양념치킨", new BigDecimal(11000));
        상품리스트.add(강정치킨);
        상품리스트.add(순살치킨);
        상품리스트.add(양념치킨);
        productService = new ProductService(productDao);
    }

    @DisplayName("상품 등록 테스트")
    @Test
    void createProduct() {
        when(productDao.save(강정치킨)).thenReturn(강정치킨);

        Product resultProduct = productService.create(강정치킨);

        assertThat(resultProduct.getName()).isEqualTo(강정치킨.getName());
        assertThat(resultProduct.getPrice()).isEqualTo(강정치킨.getPrice());
    }

    @DisplayName("상품목록 조회 테스트")
    @Test
    void findProductList() {
        when(productDao.findAll()).thenReturn(상품리스트);
        assertThat(productService.list().size()).isEqualTo(상품리스트.size());
        List<String> productNames = productService.list().stream()
                .map(product -> product.getName())
                .collect(Collectors.toList());
        List<String> expectedProductNames = 상품리스트.stream()
                .map(product -> product.getName())
                .collect(Collectors.toList());

        assertThat(productNames).containsExactlyElementsOf(expectedProductNames);
    }

    @DisplayName("상품등록 예외테스트: 가격이 음수 또는 null 일경우")
    @Test
    void invalidPrice() {
        강정치킨.setPrice(null);
        Throwable nullPriceException = assertThrows(IllegalArgumentException.class,
                () -> productService.create(강정치킨)
        );
        assertThat(nullPriceException.getMessage()).isEqualTo("가격정보가 잘못되었습니다.");

        강정치킨.setPrice(new BigDecimal(-5000));
        Throwable minusPriceException = assertThrows(IllegalArgumentException.class,
                () -> productService.create(강정치킨)
        );
        assertThat(minusPriceException.getMessage()).isEqualTo("가격정보가 잘못되었습니다.");
    }
}
