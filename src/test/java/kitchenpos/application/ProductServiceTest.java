package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductDao productDao;

    @InjectMocks
    ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    public void create() {
        //given
        Product 강정치킨 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
        given(productDao.save(any())).willReturn(강정치킨);

        //when
        Product actual = productService.create(강정치킨);

        //then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(강정치킨.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(강정치킨.getPrice())
        );
    }

    @DisplayName("상품은 가격은 null 일 수 없다.")
    @Test
    void productPriceNull() {
        //given
        Product 강정치킨 = new Product();
        강정치킨.setName("강정치킨");

        //when & then
        assertThatThrownBy(() -> productService.create(강정치킨))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("상품은 가격은 0원 미만 일 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = {-1, -100, -1000})
    void productPriceZero(long price) {
        //given
        Product 강정치킨 = new Product(1L, "강정치킨", BigDecimal.valueOf(price));

        //when & then
        assertThatThrownBy(() -> productService.create(강정치킨))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        Product 강정치킨 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
        Product 양념치킨 = new Product(2L, "양념치킨", BigDecimal.valueOf(19000));

        given(productDao.findAll()).willReturn(Arrays.asList(강정치킨, 양념치킨));

        //when
        List<Product> actual = productService.list();

        //then
        assertThat(actual).containsExactly(강정치킨, 양념치킨);
    }
}
