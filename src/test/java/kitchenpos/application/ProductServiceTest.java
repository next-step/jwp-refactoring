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
import static org.mockito.Mockito.when;

@DisplayName("상품 기능 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 가격 유효성 테스트")
    void 상품_가격_유효성_테스트() {
        // given
        // 유효하지 않은 가격이 등록 됨
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(-100));

        // then
        // 예외 발생
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 등록 테스트")
    void 상품_등록_테스트() {
        // given
        // 정상 상품 생성되어 있음
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(1000));

        // when
        // 등록 요청함
        when(productDao.save(product)).thenReturn(product);
        Product expected = productService.create(product);

        // then
        // 상품 정상 등록 됨
        assertThat(expected.getName()).isEqualTo(product.getName());
    }

    @Test
    @DisplayName("상품 목록을 조회함")
    void 상품_목록_조회() {
        // given
        // 정상 상품 생성되어 있음
        Product firstProduct = new Product();
        Product secondProduct = new Product();

        // when
        // 상품 등록되어 있음
        when(productDao.findAll()).thenReturn(Arrays.asList(firstProduct, secondProduct));
        List<Product> expected = productService.list();

        // then
        // 상품 목록이 조회됨
        assertThat(expected.size()).isEqualTo(2);
    }
}
