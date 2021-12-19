package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.menu.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void createProduct() {
        // given
        Product product = 상품_생성("후라이드", 16000);
        given(productRepository.save(product))
            .willReturn(product);

        // when
        ProductResponse savedProduct = productService.create(
            new ProductRequest(product.getName(), product.getPrice()));

        // then
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다.")
    @Test
    void createProductWrongPrice() {
        // given
        Product zeroPriceProduct = 상품_생성("후라이드", -1);
        Product nullPriceProduct = 상품_생성("후라이드");

        ProductRequest zeroPriceProductRequest =
            new ProductRequest(zeroPriceProduct.getName(), zeroPriceProduct.getPrice());
        ProductRequest nullPriceProductRequest =
            new ProductRequest(nullPriceProduct.getName(), null);

        // when && then
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> productService.create(zeroPriceProductRequest)),
            () -> assertThrows(IllegalArgumentException.class, () -> productService.create(nullPriceProductRequest))
        );
    }

    @DisplayName("상품의 목록을 가져온다.")
    @Test
    void getProducts() {
        // given
        List<Product> products = Arrays.asList(
            상품_생성("후라이드", 16000),
            상품_생성("양념치킨", 16000));
        given(productRepository.findAll())
            .willReturn(products);

        // when
        List<ProductResponse> findProducts = productService.list();

        // then
        assertThat(findProducts)
            .extracting("name")
            .containsExactlyElementsOf(products.stream().map(Product::getName).collect(Collectors.toList()));
        assertThat(findProducts)
            .extracting("price")
            .containsExactlyElementsOf(products.stream().map(Product::getPrice).collect(Collectors.toList()));
    }

    static Product 상품_생성(String name) {
        return new Product(name, null);
    }

    static Product 상품_생성(String name, int price) {
        return new Product(name, BigDecimal.valueOf(price));
    }

    static Product 상품_생성(Long id, String name, int price) {
        return new Product(id, name, BigDecimal.valueOf(price));
    }
}
