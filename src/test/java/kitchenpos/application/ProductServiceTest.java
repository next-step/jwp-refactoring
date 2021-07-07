package kitchenpos.application;

import kitchenpos.product.domain.ProductDao;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        Product product = new Product(1L, "신상품", BigDecimal.valueOf(15000));
        given(productDao.save(any())).willReturn(product);

        Product created = productService.create(product);

        assertThat(created).isEqualTo(product);
    }

    @DisplayName("상품을 등록에 실패한다. - 상품 등록시 가격값이 null 이거나 0보다 작으면 등록 실패한다.")
    @Test
    void fail_create() {
        Product product1 = new Product(1L, "신상품", BigDecimal.valueOf(-1));
        Product product2 = new Product(1L, "신상품", null);

        assertThatThrownBy(() -> productService.create(product1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> productService.create(product2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 리스트를 조회한다.")
    @Test
    void list() {
        Product product1 = new Product(1L, "신상품1", BigDecimal.valueOf(15000));
        Product product2 = new Product(2L, "신상품2", BigDecimal.valueOf(17000));
        List<Product> products = Arrays.asList(product1, product2);
        given(productDao.findAll()).willReturn(products);

        List<Product> findProducts = productService.list();

        Assertions.assertThat(findProducts).containsExactly(product1, product2);
    }
}
