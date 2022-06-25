package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 진매;
    private Product 진순이;

    @BeforeEach
    void setUp() {
        진매 = new Product();
        진매.setName("진라면 매운맛");
        진매.setPrice(BigDecimal.valueOf(5_000L));

        진순이 = new Product();
        진순이.setName("진라면 순한맛");
        진순이.setPrice(BigDecimal.valueOf(5_000L));
    }

    @Test
    @DisplayName("상품을 등록할 수 있다")
    void create() {
        // given
        given(productDao.save(any(Product.class))).willReturn(진매);

        //when
        Product savedProduct = productService.create(진매);

        //then
        assertThat(savedProduct).isEqualTo(진매);
    }

    @ParameterizedTest
    @DisplayName("상품의 가격이 0원 미만이면 등록할 수 없다")
    @CsvSource(value = {"-1", "null"}, nullValues = {"null"})
    void createException1(BigDecimal price) {
        // given
        Product product = new Product();
        product.setName("진라면 이상한 맛");
        product.setPrice(price);

        //when & then
        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품의 목록을 조회할 수 있다")
    void list() {
        // given
        given(productDao.findAll()).willReturn(Arrays.asList(진매, 진순이));

        // when
        List<Product> list = productService.list();

        // then
        assertThat(list).containsExactly(진매, 진순이);
    }
}
