package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("제품 서비스")
public class ProductServiceTest extends ServiceTestBase {
    private final ProductService productService;

    @Autowired
    public ProductServiceTest(ProductService productService) {
        this.productService = productService;
    }

    @DisplayName("제품을 등록한다")
    @Test
    void create() {
        Product product = createProduct("강정치킨", 17_000L);
        Product savedProduct = productService.create(product);

        assertThat(savedProduct.getId()).isNotNull();
    }

    @DisplayName("가격이 부적합한 제품을 등록한다")
    @ParameterizedTest
    @MethodSource
    void createWithIllegalArguments(Long price) {
        Product product = createProduct("강정치킨", price);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(product));
    }

    private static Stream<Arguments> createWithIllegalArguments() {
        return Stream.of(
                Arguments.of((Object)null),
                Arguments.of(-1L)
        );
    }

    @DisplayName("제품을 조회한다")
    @Test
    void findAll() {
        productService.create(createProduct("후라이드치킨", 13_000L));
        productService.create(createProduct("양념치킨", 13_000L));

        List<Product> products = productService.list();

        assertThat(products.size()).isEqualTo(2);
        List<String> productNames = products.stream()
                .map(Product::getName)
                .collect(Collectors.toList());
        assertThat(productNames).contains("후라이드치킨", "양념치킨");
    }

    public static Product createProduct(String name, Long price) {
        Product product = new Product();
        product.setName(name);
        if (price != null) {
            product.setPrice(new BigDecimal(price));
        }

        return product;
    }
}
