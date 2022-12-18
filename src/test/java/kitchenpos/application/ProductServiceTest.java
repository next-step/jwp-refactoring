package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
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

import static kitchenpos.domain.ProductTestFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 허니콤보;
    private Product 레드윙;

    @BeforeEach
    void setUp() {
        허니콤보 = createProduct(1L, "허니콤보", BigDecimal.valueOf(18000));
        레드윙 = createProduct(2L, "레드윙", BigDecimal.valueOf(19000));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void 상품_생성() {
        // given
        when(productDao.save(허니콤보)).thenReturn(허니콤보);
        // when
        Product result = productService.create(허니콤보);
        // then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo(허니콤보.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(허니콤보.getPrice())
        );
    }

    @DisplayName("상품의 전체 목록을 조회한다.")
    @Test
    void 상품_전체_목록_조회() {
        // given
        List<Product> products = Arrays.asList(허니콤보, 레드윙);
        when(productDao.findAll()).thenReturn(products);
        // when
        List<Product> findProducts = productService.list();
        // then
        assertAll(
                () -> assertThat(findProducts).hasSize(products.size()),
                () -> assertThat(findProducts).containsExactly(허니콤보, 레드윙)
        );
    }

    @DisplayName("가격이 비어있는 상품 등록은 예외 발생")
    @Test
    void 상품_생성_예외_테스트1() {
        // given
        Product product = createProduct(3L, "콜라", BigDecimal.valueOf(-1));
        // when
        Assertions.assertThatThrownBy(
                () -> productService.create(product)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격에 null이 들어가있을 시 예외 발생")
    @Test
    void 상품_생성_예외_테스트2() {
        // given
        Product product = createProduct(3L, "콜라", null);
        // when
        Assertions.assertThatThrownBy(
                () -> productService.create(product)
        ).isInstanceOf(IllegalArgumentException.class);
    }

}
