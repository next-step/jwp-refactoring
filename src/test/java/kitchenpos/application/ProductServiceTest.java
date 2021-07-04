package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    void given_Product_when_CreateProduct_thenSaveExecuted() {
        // given
        final Product product = new Product();
        product.setPrice(BigDecimal.ZERO);

        // when
        productService.create(product);

        // then
        verify(productDao).save(product);
    }

    @Test
    void given_InvalidProduct_when_CreateProduct_thenThrownException() {
        // given
        final Product minusPrice = new Product();
        minusPrice.setPrice(new BigDecimal(-1));
        // when
        final Throwable minusPriceExcpetion = catchThrowable(() -> productService.create(minusPrice));
        // then
        assertThat(minusPriceExcpetion).isInstanceOf(IllegalArgumentException.class);

        // given
        final Product nullPrice = new Product();
        nullPrice.setPrice(null);
        // when
        final Throwable nullPriceException = catchThrowable(() -> productService.create(nullPrice));
        // then
        assertThat(nullPriceException).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_List_then_FindAllExecuted() {
        // when
        productService.list();

        // then
        verify(productDao).findAll();
    }
}
