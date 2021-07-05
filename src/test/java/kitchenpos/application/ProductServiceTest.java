package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    private ProductService productService;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("치킨");
        product.setPrice(BigDecimal.valueOf(10000));

        productService = new ProductService(productDao);
    }

    @Test
    @DisplayName("상품 가격이 0 보다 작은 경우 예외가 발생한다")
    void notValidPriceTest() {

        // given
        product.setPrice(BigDecimal.valueOf(-1));

        // then
        Assertions.assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품을 생성한다")
    void createTest() {

        // given
        when(productDao.save(any())).thenReturn(product);

        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isEqualTo(product.getId());
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
    }

    @Test
    @DisplayName("상품 목록을 조회한다")
    void listTest() {

        // when
        when(productDao.findAll()).thenReturn(Arrays.asList(new Product(), new Product()));

        // then
        assertThat(productService.list()).hasSize(2);
    }

}