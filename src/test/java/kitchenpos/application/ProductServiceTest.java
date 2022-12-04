package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@DisplayName("ProductService 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 스테이크;

    @BeforeEach
    void setUp() {
        스테이크 = new Product(1L, "스테이크", null);
    }

    @Test
    void 상품의_가격이_null이면_상품을_등록할_수_없음() {
        스테이크.setPrice(null);

        assertThatThrownBy(() -> {
            productService.create(스테이크);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, -2, -5, -10 })
    void 상품의_가격이_0보다_작으면_상품을_등록할_수_없음(int price) {
        스테이크.setPrice(new BigDecimal(price));

        assertThatThrownBy(() -> {
            productService.create(스테이크);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_등록() {
        스테이크.setPrice(new BigDecimal(25000));
        given(productDao.save(스테이크)).willReturn(스테이크);

        Product savedProduct = productService.create(스테이크);

        assertThat(savedProduct).satisfies(pd -> {
          assertEquals(스테이크.getId(), pd.getId());
          assertEquals(스테이크.getName(), pd.getName());
          assertEquals(스테이크.getPrice(), pd.getPrice());
        });
    }

    @Test
    void 상품_목록_조회() {
        Product 스파게티 = new Product(2L, "스파게티", new BigDecimal(18000));

        given(productDao.findAll()).willReturn(Arrays.asList(스테이크, 스파게티));

        List<Product> products = productService.list();

        assertAll(
                () -> assertThat(products).hasSize(2),
                () -> assertThat(products).containsExactly(스테이크, 스파게티)
        );
    }
}
