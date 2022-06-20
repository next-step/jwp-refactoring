package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.product.application.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 관련 Service 단위 테스트 - Stub")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;
    private ProductService productService;

    @BeforeEach
    void setUp(){
        productService = new ProductService(productDao);
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        //given
        long generateProductId = 1;
        Product request = new Product(BigDecimal.valueOf(19000));
        doAnswer(invocation -> new Product(generateProductId, request.getName(), request.getPrice()))
                .when(productDao).save(request);
        //when
        Product result = productService.create(request);

        //then
        assertThat(result.getId()).isEqualTo(generateProductId);
        assertThat(result.getPrice()).isEqualTo(request.getPrice());
    }

    @DisplayName("상품 가격이 null 이거나 0원 미만이면 등록 할 수 없다.")
    @Test
    void create_price_null_or_less_then_zero() {
        //given
        Product request_price_less_then_zero = new Product(BigDecimal.valueOf(-1));
        Product request_price_null = new Product();

        //when then
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(()-> productService.create(request_price_less_then_zero)),
                () -> assertThatIllegalArgumentException().isThrownBy(()-> productService.create(request_price_null))
        );
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        //given
        Product product1 = new Product(BigDecimal.valueOf(19000));
        Product product2 = new Product(BigDecimal.valueOf(15000));
        Product product3 = new Product(BigDecimal.valueOf(13000));
        given(productDao.findAll()).willReturn(Arrays.asList(product1, product2, product3));

        //when
        List<Product> result = productService.list();

        //then
        assertThat(result)
                .containsExactlyInAnyOrderElementsOf(Arrays.asList(product1, product2, product3));
    }
}
