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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 햄버거;
    private Product 피자;

    @BeforeEach
    void init() {
        // given
        햄버거 = 상품_생성(1L, "햄버거", 9_500L);
        피자 = 상품_생성(2L, "피자", 21_000L);
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void createProduct() {
        // given
        given(productDao.save(any(Product.class))).willReturn(햄버거);

        // when
        Product savedProduct = productService.create(햄버거);

        // then
        assertAll(
            () -> assertThat(savedProduct).isNotNull(),
            () -> assertThat(savedProduct.getName()).isEqualTo(햄버거.getName()),
            () -> assertThat(savedProduct.getId()).isEqualTo(햄버거.getId())
        );
    }

    @Test
    @DisplayName("상품 가격이 null 이거나 음수일 경우 - 오류")
    void invalidPrice() {
        // given
        Product 떡볶이 = 상품_생성("떡볶이", -3_000L);
        Product 값이_없는_상품 = 상품_생성("떡볶이");

        // when then
        assertAll(
            () -> assertThatThrownBy(() -> productService.create(떡볶이))
                .isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> productService.create(값이_없는_상품))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void findAll() {
        // given
        given(productDao.findAll()).willReturn(Arrays.asList(햄버거, 피자));

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).containsExactly(햄버거, 피자);
    }

    public static Product 상품_생성(String name) {
        return new Product(null, name, null);
    }

    public static Product 상품_생성(String name, Long price) {
        return new Product(null, name, new BigDecimal(price));
    }

    public static Product 상품_생성(Long id, String name, Long price) {
        return new Product(id, name, new BigDecimal(price));
    }
}
