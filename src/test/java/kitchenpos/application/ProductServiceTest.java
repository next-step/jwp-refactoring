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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductDao productDao;

    @InjectMocks
    ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    void 상품_생성_성공() {
        // given
        Product 뿌링클윙 = 상품_생성(1L, "뿌링클윙", 20_000);
        given(productDao.save(any(Product.class))).willReturn(뿌링클윙);

        // when
        Product saved = productService.create(뿌링클윙);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo(뿌링클윙.getName());
        assertThat(saved.getPrice()).isEqualTo(뿌링클윙.getPrice());
    }

    @DisplayName("상품 생성에 실패한다.")
    @Test
    void 상품_생성_예외() {
        Product 뿌링클순살 = 상품_생성(1L, "뿌링클 순살", -7);

        assertThatThrownBy(() -> productService.create(뿌링클순살))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void 상품_목록_조회() {
        // given
        Product 팔당보쌈 = 상품_생성(1L, "팔당보쌈", 28_000);
        Product 파파존스피자 = 상품_생성(2L, "파파존스 피자", 22_000);
        given(productDao.findAll()).willReturn(Arrays.asList(팔당보쌈, 파파존스피자));

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).containsExactly(팔당보쌈, 파파존스피자);
    }

    public static Product 상품_생성(long id, String name, int price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }
}
