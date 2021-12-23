package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @Test
    @DisplayName("상품을 등록한다.")
    void create() {
        // given
        Product product = new Product();
        product.setPrice(new BigDecimal(16_000));

        given(productDao.save(any())).willReturn(product);

        // when
        Product result = productService.create(product);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPrice()).isEqualTo(product.getPrice());
    }

    @Test
    @DisplayName("상품 가격이 0원 이상이 아니면 상품을 등록할 수 없다.")
    void create_null_or_empty() {
        // given
        Product 가격이_null인_상품 = new Product();
        Product 가격이_0원인_상품 = new Product();
        가격이_0원인_상품.setPrice(new BigDecimal(-1));

        // when, then
        assertThatThrownBy(() -> productService.create(가격이_null인_상품))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> productService.create(가격이_0원인_상품))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void list() {
        // given
        List<Product> products = new ArrayList<>(Arrays.asList(new Product(), new Product()));
        given(productDao.findAll()).willReturn(products);

        // when
        List<Product> result = productService.list();

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(products.size());
    }
}
