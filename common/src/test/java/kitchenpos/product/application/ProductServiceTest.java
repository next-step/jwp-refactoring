package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.KitchenposException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.dto.ProductResponses;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 생성")
    @Test
    void create() {
        // given
        Product product = new Product("name", BigDecimal.valueOf(10000));
        Mockito.when(productRepository.save(Mockito.any()))
            .thenReturn(product);

        ProductRequest request = new ProductRequest("name", BigDecimal.valueOf(10000));

        // when
        ProductResponse result = productService.create(request);

        // then
        assertThat(result).isEqualTo(ProductResponse.from(product));
    }

    @DisplayName("가격이 null인 상품 생성시 에러")
    @Test
    void createErrorWhenPriceNull() {
        // given
        ProductRequest request = new ProductRequest("name", null);

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> productService.create(request));
    }

    @DisplayName("가격이 음수인 상품 생성시 에러")
    @Test
    void createErrorWhenPriceLessThanZero() {
        // given
        ProductRequest request = new ProductRequest("name", BigDecimal.valueOf(-1));

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> productService.create(request));
    }

    @DisplayName("상품 목록 조회")
    @Test
    void list() {
        // given
        Product product1 = new Product("name1", BigDecimal.valueOf(10000));
        Product product2 = new Product("name2", BigDecimal.valueOf(20000));
        ProductResponses expected = ProductResponses.from(Arrays.asList(product1, product2));

        Mockito.when(productRepository.findAll())
            .thenReturn(Arrays.asList(product1, product2));

        // when
        ProductResponses actual = productService.list();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}