package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    @Test
    void create() {
        // given
        Product product = new Product.Builder("마늘치킨", 1000).build();

        given(productDao.save(any(Product.class))).willReturn(new Product.Builder().id(1L).build());

        // when
        Product created = productService.create(product);

        // then
        assertThat(created.getId()).isNotNull();

        // verify
        then(productDao).should(times(1)).save(any(Product.class));
    }

    @Test
    void create_throwException_ifWrongPrice() {
        // when
        // then
        assertAll(
                () -> assertThatThrownBy(() -> productService.create(new Product.Builder("마늘치킨", -1000).build()))
        );

        // verify
        then(productDao).should(never()).save(any(Product.class));
    }
}
