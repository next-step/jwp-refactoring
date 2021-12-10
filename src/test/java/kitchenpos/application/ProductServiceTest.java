package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 서비스")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void create() {
        //given
        Product productRequest = productCreateRequest("후라이드치킨", BigDecimal.ONE);

        //when
        productService.create(productRequest);

        //then
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productDao, only()).save(productCaptor.capture());
        assertThat(productCaptor.getValue())
            .extracting(Product::getName, Product::getPrice)
            .containsExactly(productRequest.getName(), productRequest.getPrice());
    }

    @Test
    @DisplayName("등록하려는 상품의 가격은 반드시 존재해야 한다.")
    void create_nullPrice_thrownException() {
        //given
        Product productRequest = productCreateRequest("후라이드치킨", null);

        //when
        ThrowingCallable createCallable = () -> productService.create(productRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 상품의 가격은 0원 이상이어야 한다.")
    void create_priceLessThanZero_thrownException() {
        //given
        Product productRequest = productCreateRequest("후라이드치킨", BigDecimal.valueOf(-1));

        //when
        ThrowingCallable createCallable = () -> productService.create(productRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("상품들을 조회할 수 있다.")
    void list() {
        //when
        productService.list();

        //then
        verify(productDao, only()).findAll();
    }

    private Product productCreateRequest(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
