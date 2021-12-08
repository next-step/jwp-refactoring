package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductDao productDao;
    private ProductService productService;

    public static Product product(Long id, String name, BigDecimal price) {
        final Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    @BeforeEach
    void setUp() {
        productDao = mock(ProductDao.class);
        productService = new ProductService(productDao);
    }

    @Test
    void 상품_생성() {
        //given
        final Product expected = product(1L, "콜라", BigDecimal.valueOf(1000));
        when(productDao.save(any(Product.class))).thenReturn(expected);

        //when
        final Product actual = productService.create(expected);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 상품_생성_금액_오류() {
        //given
        final Product expected = product(1L, "콜라", BigDecimal.valueOf(-1));
        when(productDao.save(any(Product.class))).thenReturn(expected);

        //when
        assertThatThrownBy(() -> {
            final Product actual = productService.create(expected);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_내역_조회() {
        //given
        final Product expected = product(1L, "콜라", BigDecimal.valueOf(1000));
        final Product expected2 = product(2L, "소스", BigDecimal.valueOf(1000));
        when(productDao.findAll()).thenReturn(Lists.newArrayList(expected, expected2));

        //when
        final List<Product> actual = productService.list();

        //then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(expected, expected2)
        );
    }
}