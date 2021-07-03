package kitchenpos.product.application;

import static kitchenpos.util.TestDataSet.강정치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품을 정상 생성 케이스")
    void create() {
        //given
        given(productDao.save(any())).willReturn(강정치킨);

        //when
        Product result = productService.create(강정치킨);

        // then
        assertThat(result.getName()).isEqualTo(강정치킨.getName());
        assertThat(result.getPrice()).isEqualTo(강정치킨.getPrice());

        verify(productDao, times(1)).save(result);
    }

    @Test
    @DisplayName("가격이 없거나 0보다 작으면 실패한다.")
    void createMinusPrice() {
        //when
        Product 가격_음수 = new Product(1L, "음수", BigDecimal.valueOf(-1));
        Product 가격_0 = new Product(2L, "0", BigDecimal.valueOf(0));
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.create(가격_음수);
            productService.create(가격_0);
        });
    }

}
