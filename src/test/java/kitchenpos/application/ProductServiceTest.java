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

    @Test
    @DisplayName("상품을 생성할 수 있다.")
    void create() {
        //given
        Product product = new Product();
        product.setPrice(BigDecimal.TEN);
        given(productDao.save(any())).willReturn(product);

        //when
        Product result = productService.create(product);

        //then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("상품 가격이 NULL 이거나 음수면 생성할 수 없다.")
    void create_fail() {
        //given
        Product product = new Product();

        //then
        assertThatThrownBy(() -> productService.create(product)).isExactlyInstanceOf(IllegalArgumentException.class);

        //when
        product.setPrice(BigDecimal.valueOf(-1));

        //then
        assertThatThrownBy(() -> productService.create(product)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 상품을 조회할 수 있다.")
    void list() {
        //given
        given(productDao.findAll()).willReturn(Arrays.asList(new Product()));

        //when
        List<Product> products = productService.list();

        //then
        assertThat(products).isNotEmpty();
    }
}
