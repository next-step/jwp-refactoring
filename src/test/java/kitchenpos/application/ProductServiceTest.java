package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
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

@DisplayName("주문 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    private ProductService productService;

    private Product product;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setPrice(BigDecimal.valueOf(1000));
        productService = new ProductService(productDao);
    }

    @DisplayName("사용자는 상품을 생성할 수 있다.")
    @Test
    void crate() {
        // given

        // when
        when(productDao.save(any())).thenReturn(product);
        Product createdProduct = productService.create(this.product);
        // then
        assertThat(createdProduct).isNotNull();
    }

    @DisplayName("사용자는 상품 리스트를 조회 할 수 있다.")
    @Test
    void findAll() {
        // given

        // when
        when(productDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(product)));
        List<Product> products = productService.list();
        // then
        assertThat(products.size()).isEqualTo(1);
        assertThat(products.get(0).getPrice()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @DisplayName("요청의 상품 가격이 비어있지 않은지 0보다 낮은 가격인지 체크")
    @Test
    void createFailedByPrice() {
        // given
        product.setPrice(BigDecimal.valueOf(-100));
        // when
        // then
        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }
}