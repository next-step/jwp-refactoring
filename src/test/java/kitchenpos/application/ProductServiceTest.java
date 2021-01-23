package kitchenpos.application;


import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

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
        ProductResponse savedProduct = productService.create(createRequest("강정치킨", 17_000L));

        assertThat(savedProduct.getId()).isNotNull();
    }

    @DisplayName("제품을 조회한다")
    @Test
    void findAll() {
        productService.create(createRequest("후라이드치킨", 13_000L));
        productService.create(createRequest("양념치킨", 13_000L));

        List<ProductResponse> products = productService.list();

        assertThat(products.size()).isEqualTo(2);
        List<String> productNames = products.stream()
                .map(ProductResponse::getName)
                .collect(Collectors.toList());
        assertThat(productNames).contains("후라이드치킨", "양념치킨");
    }

    public static ProductRequest createRequest(String name, long price) {
        return new ProductRequest(name, price);
    }
}
