package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.factory.ProductFixtureFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.factory.ProductFixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    ProductService productService;

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void 상품_등록(){
        //given
        Product 치킨 = createProduct( 1L, "치킨", BigDecimal.valueOf(15000L));
        given(productDao.save(any(Product.class))).willReturn(치킨);

        //when
        ProductResponse savedProduct = productService.create(ProductRequest.from(치킨));

        //then
        assertThat(savedProduct.getId()).isEqualTo(치킨.getId());
    }


    @DisplayName("상품의 가격은 0 이상이다")
    @Test
    void 상품_가격_검증(){
        //given
        Product invalidProduct = createProduct("치킨", BigDecimal.valueOf(-15000L));

        //then
       assertThrows(IllegalArgumentException.class, () -> productService.create(ProductRequest.from(invalidProduct)));
    }

    @DisplayName("상품의 목록을 조회할 수 있다")
    @Test
    void 상품_목록_조회() {
        //given
        Product 치킨 = createProduct( 1L, "치킨", BigDecimal.valueOf(15000L));
        Product 피자 = createProduct(2L, "피자", BigDecimal.valueOf(20000L));
        given(productDao.findAll()).willReturn(Arrays.asList(치킨, 피자));

        //when
        List<ProductResponse> list = productService.list();

        //then
        assertThat(list).containsExactly(ProductResponse.from(치킨), ProductResponse.from(피자));
    }
}