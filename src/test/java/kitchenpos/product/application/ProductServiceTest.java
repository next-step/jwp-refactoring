package kitchenpos.product.application;

import com.navercorp.fixturemonkey.FixtureMonkey;
import kitchenpos.product.persistence.ProductDao;
import kitchenpos.product.domain.Product;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductDao productDao;
    public static FixtureMonkey fixtureMonkey;

    @BeforeAll
    public static void setup() {
        fixtureMonkey = FixtureMonkey.create();
    }

    @DisplayName("상품가격이 없는경우 예외발생")
    @Test
    public void throwsExceptionWhenNullPrice() {
        assertThatThrownBy(() -> productService.create(new Product()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품가격이 0보다 작은경우 예외발생")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -3, -100, -999})
    public void throwsExceptionWhenNegativePrice(int price) {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(price));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 추가할 경우 추가된 상품정보를 반환")
    @ParameterizedTest
    @ValueSource(longs = {1, 342, 21, 3423, 4})
    public void returnProduct(long id) {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(1500));
        Product mockProduct = new Product();
        mockProduct.setPrice(BigDecimal.valueOf(1500));
        mockProduct.setId(id);
        doReturn(mockProduct).when(productDao).save(product);

        Product savedProduct = productService.create(product);

        assertThat(savedProduct.getId()).isEqualTo(id);
    }

    @DisplayName("상품목록을 조회할 경우 저장된 상품목록반환")
    @Test
    public void returnProducts() {
        List<Product> mockProducts = fixtureMonkey.giveMeBuilder(Product.class)
                .set("price", BigDecimal.valueOf(Arbitraries.integers().greaterOrEqual(1000).sample()))
                .set("id", Arbitraries.longs().between(1, 5))
                .sampleList(5);
        doReturn(mockProducts).when(productDao).findAll();

        List<Product> products = productService.list();

        List<BigDecimal> productPrice = products.stream().map(Product::getPrice).collect(Collectors.toList());
        List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());
        assertAll(
                () -> assertThat(productPrice).allMatch(bigDecimal -> bigDecimal.intValue() >= 1000),
                () -> assertThat(productIds).containsAnyOf(1l, 2l, 3l, 4l, 5l));
    }
}
