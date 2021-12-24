package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.testfixtures.ProductTestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        // given
        String name = "타코야끼";
        BigDecimal price = BigDecimal.valueOf(12000);
        Product product = new Product(1L, name, new ProductPrice(price));
        ProductTestFixtures.상품_생성_결과_모킹(productRepository, product);

        //when
        ProductResponse savedProduct = productService.create(new ProductRequest(name, price));

        //then
        assertThat(product.getId()).isEqualTo(savedProduct.getId());
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        List<Product> products = Arrays.asList(
            new Product("타코야끼", new ProductPrice(BigDecimal.valueOf(12000))),
            new Product("뿌링클", new ProductPrice(BigDecimal.valueOf(22000))));
        ProductTestFixtures.상품_전체_조회_모킹(productRepository, products);

        //when
        List<ProductResponse> findProducts = productService.list();

        //then
        assertThat(findProducts.size()).isEqualTo(products.size());
        상품목록_검증(findProducts, products);
    }

    private void 상품목록_검증(List<ProductResponse> findProducts, List<Product> products) {
        List<Long> findProductIds = findProducts.stream()
            .map(ProductResponse::getId)
            .collect(Collectors.toList());
        List<Long> expectProductIds = products.stream()
            .map(Product::getId)
            .collect(Collectors.toList());
        assertThat(findProductIds).containsAll(expectProductIds);
    }
}
