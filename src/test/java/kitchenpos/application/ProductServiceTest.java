package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final Product request = ProductFixture.ofRequest("후라이드", BigDecimal.valueOf(16_000));
        final Product expected = ProductFixture.of(1L, "후라이드", BigDecimal.valueOf(16_000));
        given(productDao.save(request)).willReturn(expected);

        // when
        final Product actual = productService.create(request);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("상품 가격이 null 이거나 0보다 작은 경우 상품을 등록할 수 없다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-16000"})
    void create_fail_invalidPrice(final BigDecimal price) {
        // given
        final Product request = ProductFixture.ofRequest("후라이드", price);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> productService.create(request);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final List<Product> expected = Arrays.asList(
            ProductFixture.of(1L, "후라이드", BigDecimal.valueOf(16_000)),
            ProductFixture.of(2L, "양념치킨", BigDecimal.valueOf(16_000))
        );
        given(productDao.findAll()).willReturn(expected);

        // when
        final List<Product> actual = productService.list();

        // then
        assertThat(actual).containsExactlyElementsOf(expected);
    }
}
