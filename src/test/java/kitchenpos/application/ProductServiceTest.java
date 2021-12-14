package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void createProduct() {
        // given
        Product product = new Product("후라이드", BigDecimal.valueOf(16000));
        given(productDao.save(any()))
            .willReturn(product);

        // when
        Product savedProduct = productService.create(product);

        // then
        assertEquals(product, savedProduct);
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다.")
    @Test
    void createProductWrongPrice() {
        // given
        Product zeroPriceProduct = new Product("후라이드", BigDecimal.valueOf(-1));
        Product nullPriceProduct = new Product("후라이드", null);

        // when && then
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> productService.create(zeroPriceProduct)),
            () -> assertThrows(IllegalArgumentException.class, () -> productService.create(nullPriceProduct))
        );
    }

    @DisplayName("상품의 목록을 가져온다.")
    @Test
    void getProducts() {
        // given
        List<Product> products = Arrays.asList(
            new Product("후라이드", BigDecimal.valueOf(16000)),
            new Product("양념치킨", BigDecimal.valueOf(16000)));
        given(productDao.findAll())
            .willReturn(products);

        // when
        List<Product> findProducts = productService.list();

        // then
        assertThat(findProducts)
            .containsExactlyElementsOf(products);
    }

}
