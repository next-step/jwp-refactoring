package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void 상품_등록() {
        // given
        Product expected = new Product(1L, "떡볶이", BigDecimal.valueOf(16000));

        given(productDao.save(any()))
            .willReturn(expected);

        // when
        Product actual = productService.create(expected);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("상품의 가격이 0 원 이상이 아니면 등록할 수 없다.")
    @Test
    void 상품_생성_예외_음수_가격() {
        // given
        Product product = new Product(1L, "떡볶이", BigDecimal.valueOf(-15000));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> productService.create(product)
        );
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void 상품_목록_조회() {
        // given
        Product product1 = new Product(1L, "떡볶이", BigDecimal.valueOf(16000));
        Product product2 = new Product(2L, "로제떡볶이", BigDecimal.valueOf(17000));
        Product product3 = new Product(3L, "짜장떡볶이", BigDecimal.valueOf(17000));
        List<Product> expected = Arrays.asList(product1, product2, product3);

        given(productDao.findAll())
            .willReturn(expected);

        // when
        List<Product> actual = productService.list();

        // then
        assertThat(expected).containsAll(actual);
    }
}
