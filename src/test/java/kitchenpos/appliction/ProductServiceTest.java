package kitchenpos.appliction;

import kitchenpos.application.ProductService;
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

import static kitchenpos.domain.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품 테스트")
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성한다")
    @Test
    void 상품_생성() {
        // given
        Product 알리오올리오 = 상품(1L, "알리오올리오", new BigDecimal(17000));
        given(productDao.save(any())).willReturn(알리오올리오);

        // when
        Product product = productService.create(알리오올리오);

        // then
        verify(productDao).save(any());
        assertThat(product.getName()).isEqualTo("알리오올리오");
    }

    @DisplayName("전체 상품 목록을 조회한다")
    @Test
    void 전체_상품_목록_조회() {
        // given
        Product 알리오올리오 = 상품(1L, "알리오올리오", new BigDecimal(17000));
        Product 쉬림프로제 = 상품(2L, "쉬림프로제", new BigDecimal(22000));
        given(productDao.findAll()).willReturn(Arrays.asList(알리오올리오, 쉬림프로제));

        // when
        List<Product> products = productService.list();

        // then
        assertAll(
                () -> assertThat(products).hasSize(2),
                () -> assertThat(products).containsExactly(알리오올리오, 쉬림프로제)
        );
    }

    @DisplayName("가격이 음수인 상품을 생성한다")
    @Test
    void 가격이_음수인_상품_생성() {
        // given
        Product 알리오올리오 = 상품(1L, "알리오올리오", new BigDecimal(-17000));

        // when & then
        assertThatThrownBy(
                () -> productService.create(알리오올리오)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격 정보가 없는 상품을 생성한다")
    @Test
    void 가격_정보가_없는_상품_생성() {
        // given
        Product 알리오올리오 = 상품(1L, "알리오올리오", null);

        // when & then
        assertThatThrownBy(
                () -> productService.create(알리오올리오)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
