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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 서비스")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void 상품_생성() {
        // given
        Product product = new Product("떡볶이", new BigDecimal(1000));
        given(productDao.save(any(Product.class))).willReturn(new Product(1L, "떡볶이", new BigDecimal(1000)));

        // when
        Product savedProduct = productService.create(product);

        // then
        assertAll(() -> assertThat(savedProduct).isNotNull(), () -> assertThat(savedProduct.getId()).isNotNull());
    }

    @DisplayName("상품 가격이 0보자 작으면 생성할 수 없다.")
    @Test
    void 상품_가격이_0보다_작으면_생성_실패() {
        // given
        Product product = new Product("떡볶이", new BigDecimal(-1));

        // when / then
        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 없는 상품은 생성할 수 없다.")
    @Test
    void 가격이_없는_상품_생성_실패() {
        // given
        Product product = new Product("떡볶이");

        // when / then
        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 상품을 조회할 수 있다.")
    @Test
    void 전체_상품_조회() {
        // given
        Product 떡볶이 = new Product("떡볶이");
        Product 순대 = new Product("순대");
        given(productDao.findAll()).willReturn(Arrays.asList(떡볶이, 순대));

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).hasSize(2);
    }
}