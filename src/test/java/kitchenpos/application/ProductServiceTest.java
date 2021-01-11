package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 비즈니스 로직을 처리하는 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("후라이드");
        product.setPrice(new BigDecimal(19000));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void 상품_생성() {
        given(productDao.save(product)).willReturn(product);

        final Product createdProduct = productService.create(this.product);

        assertThat(createdProduct.getId()).isEqualTo(product.getId());
        assertThat(createdProduct.getName()).isEqualTo(product.getName());
        assertThat(createdProduct.getPrice()).isEqualTo(product.getPrice());
    }

    @DisplayName("상품 가격이 음수인 경우 상품을 생성할 수 없다.")
    @Test
    void 가격이_음수인_경우_상품_생성() {
        product.setPrice(new BigDecimal(-1));

        assertThatThrownBy(() -> {
            final Product createdProduct = productService.create(this.product);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void 상품_조회() {
        given(productDao.findAll()).willReturn(Collections.singletonList(product));

        final List<Product> responseProducts = productService.list();

        assertThat(responseProducts.get(0).getId()).isEqualTo(product.getId());
        assertThat(responseProducts.get(0).getName()).isEqualTo(product.getName());
        assertThat(responseProducts.get(0).getPrice()).isEqualTo(product.getPrice());
    }
}
