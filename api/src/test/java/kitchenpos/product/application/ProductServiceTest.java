package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.domian.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.repository.ProductDao;

@DisplayName("주문 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setup() {
        product = Product.of("순대", 1000);
    }

    @DisplayName("사용자는 상품을 생성할 수 있다.")
    @Test
    void crate() {
        // given

        // when
        when(productDao.save(any())).thenReturn(product);
        ProductResponse createdProduct = productService.create(this.product);
        // then
        assertThat(createdProduct).isNotNull();
    }

    @DisplayName("사용자는 상품 리스트를 조회 할 수 있다.")
    @Test
    void findAll() {
        // given

        // when
        when(productDao.findAll()).thenReturn(Arrays.asList(product));
        List<Product> products = productService.list();
        // then
        assertThat(products.size()).isEqualTo(1);
        assertThat(products.get(0).getPrice()).isEqualTo(new Price(1000));
    }
}