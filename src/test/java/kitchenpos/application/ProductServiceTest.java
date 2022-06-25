package kitchenpos.application;

import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.application.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.domain.ProductTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 생성 시 0원 미만이면 예외가 발생해야 한다")
    @Test
    void createProductByMinusAmountTest() {
        // given
        Product 상품 = 상품_생성("상품", -1);

        // then
        상품_생성_실패됨(() -> productService.create(상품));
    }

    @DisplayName("상품 생성 시 정상")
    @Test
    void createProductTest() {
        // given
        Product 상품 = 상품_생성("상품", 1_000);
        when(productDao.save(상품)).thenReturn(상품);

        // when
        Product 상품_생성_결과 = productService.create(상품);

        // then
        상품_생성_성공됨(상품_생성_결과, 상품);
    }

    @DisplayName("상품 리스트 조회 시 정상 조회되어야 한다")
    @Test
    void findProductsTest() {
        // given
        List<Product> 상품_리스트 = Arrays.asList(
                상품_생성("상품 1", 1_000),
                상품_생성("상품 2", 1_000),
                상품_생성("상품 3", 1_000),
                상품_생성("상품 41", 1_000)
        );
        when(productDao.findAll()).thenReturn(상품_리스트);

        // when
        List<Product> 상품_조회_결과 = productService.list();

        // then
        assertThat(상품_조회_결과.size()).isGreaterThanOrEqualTo(상품_리스트.size());
        assertThat(상품_조회_결과).containsAll(상품_리스트);
    }

    void 상품_생성_실패됨(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 상품_생성_성공됨(Product source, Product target) {
        assertThat(source.getName()).isEqualTo(target.getName());
        assertThat(source.getPrice()).isEqualTo(target.getPrice());
    }
}
