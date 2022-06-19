package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @Test
    void create() {
        //given
        long id = 1L;
        String name = "product";
        BigDecimal price = BigDecimal.valueOf(1000);
        Product request = new Product(name, price);

        given(productDao.save(request)).willReturn(new Product(id, name, price));

        //when
        Product product = productService.create(request);

        //then
        상품_확인(id, name, price, product);
    }

    @Test
    void create_fail_null() {
        //given
        String name = "product";
        Product request = new Product(name, null);

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(request));
    }

    @Test
    void create_fail_negative() {
        //given
        String name = "product";
        Product request = new Product(name, BigDecimal.valueOf(-1));

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(request));
    }

    @Test
    void list() {
        //given
        given(productDao.findAll()).willReturn(Arrays.asList(new Product(1L, "product", BigDecimal.valueOf(1000))));

        //when
        List<Product> list = productService.list();

        //then
        assertEquals(1, list.size());
    }

    private void 상품_확인(long id, String name, BigDecimal price, Product product) {
        assertAll(
                () -> assertEquals(id, product.getId()),
                () -> assertEquals(name, product.getName()),
                () -> assertEquals(price, product.getPrice())
        );
    }
}