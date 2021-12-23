package kitchenpos.product.ui;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.exception.InputProductDataErrorCode;
import kitchenpos.product.exception.InputProductDataException;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("상품 인수테스트")
@ExtendWith(MockitoExtension.class)
class ProductRestControllerTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품을 등록한다.")
    void createProductTest() {
        //given
        Product product = mock(Product.class);
        given(product.getPrice())
                .willReturn(new BigDecimal("10000"));
        //when
        productService.create(product);
        //then
        상품_생성_검증(product);
    }

    @Test
    @DisplayName("상품을 조회한다.")
    void findAllTest(){
        //given
        Product product = mock(Product.class);
        given(productDao.findAll())
                .willReturn(Arrays.asList(product));

        //when
        List<Product> products = productDao.findAll();

        //then
        assertThat(products.size()).isEqualTo(1);
        assertThat(products).contains(product);
    }

    @Test
    @DisplayName("잘못된 가격이 들어가면 상품을 등록할때 에러 처리")
    void saveWrongPriceTest(){
        //given
        Product product = mock(Product.class);
        given(product.getPrice())
                .willReturn(new BigDecimal(-10000));

        //when
        assertThatThrownBy(() -> {
            productService.create(product);
        }).isInstanceOf(InputProductDataException.class)
                .hasMessageContaining(InputProductDataErrorCode.IT_CAN_NOT_INPUT_PRICE_LESS_THAN_ZERO.errorMessage());
    }

    private void 상품_생성_검증(Product product) {
        verify(product).getPrice();
    }

}
