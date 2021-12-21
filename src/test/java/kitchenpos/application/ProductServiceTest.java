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

import static kitchenpos.fixture.ProductFixture.강정치킨;
import static kitchenpos.fixture.ProductFixture.페퍼로니피자;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @DisplayName("상품 생성 성공 테스트")
    @Test
    void create_success() {
        // given
        Product 요청_상품 = new Product();
        요청_상품.setName("강정치킨");
        요청_상품.setPrice(BigDecimal.valueOf(17_000));

        given(productDao.save(any(Product.class))).willReturn(강정치킨);

        // when
        Product 생성된_상품 = productService.create(요청_상품);

        // then
        assertThat(생성된_상품).isEqualTo(강정치킨);
    }

    @DisplayName("상품 생성 실패 테스트 - 상품 가격이 0보다 작음")
    @Test
    void create_failure_invalidPrice() {
        // given
        Product 요청_상품 = new Product();
        요청_상품.setName("강정치킨");
        요청_상품.setPrice(BigDecimal.valueOf(-1));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(요청_상품));
    }

    @DisplayName("상품 목록 조회 테스트")
    @Test
    void list() {
        // given
        given(productDao.findAll()).willReturn(Arrays.asList(강정치킨, 페퍼로니피자));

        // when
        List<Product> 조회된_상품_목록 = productService.list();

        // then
        assertThat(조회된_상품_목록).containsExactly(강정치킨, 페퍼로니피자);
    }
}
