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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 관련 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductDao productDao;

    @InjectMocks
    ProductService productService;

    @DisplayName("상품 생성")
    @Test
    void create() {
        // given
        Product request = new Product(null, "초밥", BigDecimal.valueOf(20000));
        Product 예상값 = new Product(1L, "초밥", BigDecimal.valueOf(20000));
        given(productDao.save(request)).willReturn(예상값);

        // when
        Product 상품_생성_결과 = 상품_생성(request);

        // then
        상품_값_비교(상품_생성_결과, 예상값);
    }

    @DisplayName("상품 생성 - price 값이 없는 상품일 경우")
    @Test
    void create_exception1() {
        // given
        Product request = new Product(1L, "초밥", null);

        // when && then
        assertThatThrownBy(() -> 상품_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 생성 - price 값이 0 미만인 상품일 경우")
    @Test
    void create_exception2() {
        // given
        Product request = new Product(1L, "초밥", BigDecimal.valueOf(-1));

        // when && then
        assertThatThrownBy(() -> 상품_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록 조회")
    @Test
    void list() {
        // given
        List<Product> 예상값 = Arrays.asList(
                new Product(1L, "초밥", BigDecimal.valueOf(20000)),
                new Product(2L, "김밥", BigDecimal.valueOf(2000))
        );
        given(productDao.findAll()).willReturn(예상값);

        // when
        List<Product> 상품_조회_결과 = productService.list();

        // then
        assertAll(
                () -> 상품_값_비교(상품_조회_결과.get(0), 예상값.get(0)),
                () -> 상품_값_비교(상품_조회_결과.get(1), 예상값.get(1))
        );
    }

    private Product 상품_생성(Product request) {
        return productService.create(request);
    }

    private void 상품_값_비교(Product result, Product expectation) {
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(expectation.getId()),
                () -> assertThat(result.getName()).isEqualTo(expectation.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(expectation.getPrice())
        );
    }
}
