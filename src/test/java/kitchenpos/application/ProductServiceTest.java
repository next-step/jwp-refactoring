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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    ProductService productService;

    private Product 아메리카노;
    private Product 아이스티;

    @BeforeEach
    void setUp() {
        아메리카노 = new Product(1L, "아메리카노", BigDecimal.valueOf(3000L));
        아이스티 = new Product(2L, "아이스티", BigDecimal.valueOf(2000L));
    }

    @Test
    @DisplayName("상품을 등록한다.")
    void 상품_등록() {
        // given
        given(productDao.save(아메리카노)).willReturn(아메리카노);

        // when
        Product saveProduct = productService.create(아메리카노);

        // then
        assertThat(saveProduct.getId()).isEqualTo(아메리카노.getId());
        assertThat(saveProduct.getName()).isEqualTo(아메리카노.getName());
        assertThat(saveProduct.getPrice()).isEqualTo(아메리카노.getPrice());
    }

    @Test
    @DisplayName("상품의 가격이 0원 미만이면 오류 발생한다.")
    void error_상품_등록_가격_zero_미만() {
        // given
        Product 가격zero미만 = new Product(3L, "가격zero미만", BigDecimal.valueOf(-3000L));

        // then
        assertThrows(IllegalArgumentException.class, () -> productService.create(가격zero미만));
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void 상품_목록_조회() {
        // given
        List<Product> productList = Arrays.asList(아메리카노, 아이스티);
        given(productDao.findAll()).willReturn(productList);

        // when
        List<Product> searchProduct = productService.list();

        // then
        assertThat(searchProduct).containsExactly(아메리카노, 아이스티);
    }
}
