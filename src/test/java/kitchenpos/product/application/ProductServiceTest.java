package kitchenpos.product.application;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.generator.BuilderArbitraryGenerator;
import kitchenpos.product.domain.Money;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.persistence.ProductDao;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductDao productDao;
    public static FixtureMonkey fixtureMonkey;
    public static FixtureMonkey builderFixtureMonkey;

    @BeforeAll
    public static void setup() {
        builderFixtureMonkey = FixtureMonkey.builder()
                .defaultGenerator(BuilderArbitraryGenerator.INSTANCE)
                .build();
        fixtureMonkey = FixtureMonkey.create();
    }

    @DisplayName("상품가격이 없는경우 예외발생")
    @Test
    public void throwsExceptionWhenNullPrice() {
        assertThatThrownBy(() -> productService.create(new ProductRequest()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품가격이 0보다 작은경우 예외발생")
    @ParameterizedTest
    @ValueSource(longs = {-1, -2, -3, -100, -999})
    public void throwsExceptionWhenNegativePrice(long price) {
        ProductRequest product = new ProductRequest("product", price);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 추가할 경우 추가된 상품정보를 반환")
    @ParameterizedTest
    @ValueSource(longs = {1, 342, 21, 3423, 4})
    public void returnProduct(long id) {
        ProductRequest productRequest = new ProductRequest("product", 1500l);

        Product mockProduct = Product.builder()
                .money(Money.of(1500l))
                .id(id)
                .build();
        doReturn(mockProduct).when(productDao).save(any(Product.class));

        Product savedProduct = productService.create(productRequest);

        assertThat(savedProduct.getId()).isEqualTo(id);
    }

    @DisplayName("상품목록을 조회할 경우 저장된 상품목록반환")
    @Test
    public void returnProducts() {
        List<Product> mockProducts = builderFixtureMonkey
                .giveMeBuilder(Product.class)
                .set("id", Arbitraries.longs().between(1, 5))
                .set("money", Money.of(Arbitraries.longs().between(1000, 1500).sample()))
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
