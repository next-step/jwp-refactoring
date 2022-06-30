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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductDao productDao;

    @InjectMocks
    ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void createProduct() {
        // given
        Product 생성할_상품 = new Product("짬뽕", BigDecimal.valueOf(9000));
        given(productDao.save(생성할_상품))
                .willReturn(new Product(1L, "짬뽕", BigDecimal.valueOf(9000)));

        // when
        Product 생성된_상품 = productService.create(생성할_상품);

        // then
        상품_생성_성공(생성된_상품, 생성할_상품);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void listProduct() {
        // given
        List<Product> 조회할_상품_목록 = Arrays.asList(
                new Product(1L, "짬뽕", BigDecimal.valueOf(9000)),
                new Product(2L, "볶음밥", BigDecimal.valueOf(8000)),
                new Product(3L, "탕수육", BigDecimal.valueOf(7000))
        );
        given(productDao.findAll())
                .willReturn(조회할_상품_목록);

        // when
        List<Product> 조회된_상품_목록 = productService.list();

        // then
        상품_목록_조회_성공(조회된_상품_목록, 조회할_상품_목록);
    }

    private void 상품_생성_성공(Product actual, Product expected) {
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice())
        );
    }

    private void 상품_목록_조회_성공(List<Product> 조회된_상품_목록, List<Product> 조회할_상품_목록) {
        assertThat(조회된_상품_목록)
                .containsExactlyElementsOf(조회할_상품_목록);
    }
}
