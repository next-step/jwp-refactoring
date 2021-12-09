package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@DisplayName("상품 관리")
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
    void findAllProduct() {
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

    @Nested
    @DisplayName("상품 생성")
    class CreateProduct {
        @Test
        @DisplayName("성공")
        void createSuccess() {
            //given
            final Product expected = product(1L, "콜라", BigDecimal.valueOf(1000));
            when(productDao.save(any(Product.class))).thenReturn(expected);

            //when
            final Product actual = productService.create(expected);

            //then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("실패 - 잘못된 상품 금액")
        void createFailIllegalPrice() {
            //given
            final Product expected = product(1L, "콜라", BigDecimal.valueOf(-1));
            when(productDao.save(any(Product.class))).thenReturn(expected);

            //when
            assertThatThrownBy(() -> {
                final Product actual = productService.create(expected);
            }).isInstanceOf(IllegalArgumentException.class);
        }
    }

}
