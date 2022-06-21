package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.dao.ProductDao;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.application.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 서비스에 대한 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 후라이드_치킨;
    private Product 감자튀김;

    @BeforeEach
    void setUp() {
        후라이드_치킨 = Product.of(1L, "후라이드 치킨", BigDecimal.valueOf(10000L));
        감자튀김 = Product.of(1L, "감자튀김", BigDecimal.valueOf(5000L));
    }

    @DisplayName("상품을 등록하면 정상적으로 등록되어야 한다")
    @Test
    void create_test() {
        // given
        when(productDao.save(후라이드_치킨))
            .thenReturn(후라이드_치킨);

        // when
        Product result = productService.create(후라이드_치킨);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(후라이드_치킨.getId()),
            () -> assertThat(result.getName()).isEqualTo(후라이드_치킨.getName()),
            () -> assertThat(result.getPrice()).isEqualTo(후라이드_치킨.getPrice())
        );
    }

    @DisplayName("상품의 가격이 없거나 0미만이면 예외가 발생해야 한다")
    @Test
    void create_exception_test() {
        // given
        후라이드_치킨.setPrice(BigDecimal.valueOf(-50L));

        // then
        assertThatThrownBy(() -> {
            productService.create(후라이드_치킨);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품목록을 조회한다")
    @Test
    void findAll_test() {
        // given
        when(productDao.findAll())
            .thenReturn(Arrays.asList(후라이드_치킨, 감자튀김));

        // when
        List<Product> result = productService.list();

        // then
        assertThat(result).hasSize(2);
    }
}
