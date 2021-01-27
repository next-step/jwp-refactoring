package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품등록")
    void create() {
        //given
        Product newProduct = new Product();
        newProduct.setId(1L);
        newProduct.setName("강정치킨");
        newProduct.setPrice(new BigDecimal(17000));

        //when
        when(productDao.save(any())).thenReturn(newProduct);

        //then
        assertThat(productService.create(newProduct)).isNotNull();
    }

    @Test
    @DisplayName("상품가격 없거나 0이하이면 등록할 수 없음")
    void callIllegalArgumentException() {
        //given
        Product newProduct = new Product();
        newProduct.setId(1L);
        newProduct.setName("강정치킨");
        newProduct.setPrice(new BigDecimal(-1));

        assertThatThrownBy(() -> {
            productService.create(newProduct);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품조회")
    void list() {
        Product newProduct = new Product();
        newProduct.setId(1L);
        newProduct.setName("강정치킨");
        newProduct.setPrice(new BigDecimal(17000));

        when(productDao.findAll()).thenReturn(Arrays.asList(newProduct));

        assertThat(productService.list().size()).isEqualTo(1);
    }
}