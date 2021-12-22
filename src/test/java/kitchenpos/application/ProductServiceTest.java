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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductDao productDao;

    @InjectMocks
    ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        // given
        Product product = Product.of(1L, "후라이드치킨", new BigDecimal(16000.00));
        when(productDao.save(product)).thenReturn(product);

        // when
        Product expected = productService.create(product);

        // then
        assertThat(product.getId()).isEqualTo(expected.getId());
    }

    @DisplayName("상품 가격이 없거나 음수인 상품을 생성한다.")
    @Test
    void create2() {
        // given
        Product 가격_없는_상품 = Product.of(1L, "후라이드치킨", null);
        Product 가격_음수_상품 = Product.of(1L, "후라이드치킨", new BigDecimal(-16000.00));


        //then
        assertAll(
                () -> assertThatThrownBy(
                        () -> productService.create(가격_없는_상품)
                ).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(
                        () -> productService.create(가격_음수_상품)
                ).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // given
        Product product = Product.of(1L, "후라이드치킨", new BigDecimal(16000.00));
        Product product2 = Product.of(2L, "양념치킨", new BigDecimal(16000.00));
        List<Product> actual = Arrays.asList(product, product2);
        when(productDao.findAll()).thenReturn(actual);

        // when
        List<Product> expected = productService.list();

        // then
        assertThat(actual.size()).isEqualTo(expected.size());
    }
}
