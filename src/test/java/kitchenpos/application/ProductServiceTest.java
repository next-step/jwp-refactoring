package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "상품", new BigDecimal(100));
    }

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void createProduct() {
        // given
        when(productDao.save(product)).thenReturn(product);

        // when
        Product actual = productService.create(product);

        // then
        assertThat(actual.getId()).isEqualTo(product.getId());
        assertThat(actual.getName()).isEqualTo(product.getName());
        assertThat(actual.getPrice()).isEqualTo(product.getPrice());
    }

    @DisplayName("상품 가격이 0 이하면 안됨")
    @Test
    void priceException() {
        // given
        product.setPrice(new BigDecimal(-1));

        // when then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.create(product);
        });
    }

    @DisplayName("상품 목록을 조회한다")
    @Test
    void selectProduct() {
        // given
        when(productDao.findAll()).thenReturn(Collections.singletonList(product));

        // when
        List<Product> actual = productService.list();

        // then
        assertThat(actual.get(0).getId()).isEqualTo(product.getId());
        assertThat(actual.get(0).getName()).isEqualTo(product.getName());
        assertThat(actual.get(0).getPrice()).isEqualTo(product.getPrice());
    }
}
