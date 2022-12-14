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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품을 등록 할 수 있다")
    void createProduct() {
        Product 스테이크 = new Product(1L, "스테이크", BigDecimal.valueOf(10_000));

        when(productDao.save(any())).thenReturn(스테이크);

        Product result = productService.create(스테이크);

        assertThat(result.getId()).isEqualTo(스테이크.getId());
        assertThat(result.getPrice()).isEqualTo(스테이크.getPrice());
        assertThat(result.getName()).isEqualTo(스테이크.getName());
    }

    @Test
    @DisplayName("상품의 가격은 0원 이상이여야한다")
    void createProductPriceZero() {
        Product 스테이크 = new Product(1L, "스테이크", BigDecimal.valueOf(-1));

        assertThatThrownBy(() ->
                productService.create(스테이크)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 리스트를 받을 수 있다")
    void getProductList() {
        Product 스테이크 = new Product(1L, "스테이크", BigDecimal.valueOf(200));
        Product 감자튀김 = new Product(2L, "감자튀김", BigDecimal.valueOf(300));

        when(productDao.findAll()).thenReturn(Arrays.asList(스테이크, 감자튀김));

        List<Product> result = productService.list();

        assertThat(result).hasSize(2);
        assertThat(result).contains(스테이크, 감자튀김);
    }
}
