package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductDao productDao;

    @InjectMocks
    ProductService productService;

    Product 스낵랩;

    @BeforeEach
    void setUp() {
        스낵랩 = new Product(1L, "스낵랩", BigDecimal.valueOf(3000));
    }

    @Test
    @DisplayName("상품 등록 시 가격 null 예외처리")
    public void saveProductPriceNull() {
        Product product = new Product();
        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 등록 시 가격 0미만 예외처리")
    public void saveProductPriceZero() {
        스낵랩.setPrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> productService.create(스낵랩)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 정상 등록")
    public void saveSuccess() {
        given(productDao.save(스낵랩)).willReturn(스낵랩);

        assertThat(productService.create(스낵랩).getId()).isEqualTo(스낵랩.getId());
    }

    @Test
    public void list() {
        Product 맥모닝 = new Product(1L, "맥모닝", BigDecimal.valueOf(4000));
        given(productDao.findAll()).willReturn(Arrays.asList(스낵랩, 맥모닝));

        assertThat(productService.list()).contains(스낵랩, 맥모닝);
    }
}