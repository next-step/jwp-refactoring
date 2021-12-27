package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.dto.ProductResponses;
import kitchenpos.exception.KitchenposException;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 생성")
    @Test
    void create() {
        // given
        ProductRequest request = new ProductRequest("name", BigDecimal.valueOf(10000));
        Product product = new Product(1L, "name", BigDecimal.valueOf(10000));
        ProductResponse expected = ProductResponse.from(product);

        Mockito.when(productDao.save(Mockito.any()))
            .thenReturn(product);

        // when
        ProductResponse result = productService.create(request);

        // then
        assertThat(result).isEqualTo(expected);
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
        Product product1 = new Product(1L, "name1", BigDecimal.valueOf(10000));
        Product product2 = new Product(2L, "name2", BigDecimal.valueOf(20000));
        ProductResponses expected = ProductResponses.from(Arrays.asList(product1, product2));

        Mockito.when(productDao.findAll())
            .thenReturn(Arrays.asList(product1, product2));

        // when
        ProductResponses actual = productService.list();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}