package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductServiceTest extends BaseTest{
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @DisplayName("상품 등록")
    @Test
    void create() {
        Product expected = productDao.findById(1L).get();

        Product actual = productService.create(expected);

        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    @DisplayName("상품 값이 없거나 0인 경우 예외")
    @Test
    void validZero() {
        Product expected = productDao.findById(1L).get();
        expected.setPrice(null);

        assertThatThrownBy(() -> {
            productService.create(expected);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
