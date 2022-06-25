package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.helper.ReflectionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private ProductService productService;
    private Product product;
    @Mock
    private ProductDao productDao;

    @BeforeEach
    public void init() {
        productService = new ProductService(productDao);
        product = new Product();
        product.setPrice(BigDecimal.valueOf(18000));
        product.setName("치킨");
        ReflectionHelper.SetProductId(1L, product);

    }

    @Test
    @DisplayName("상품 저장 정상로직")
    void create() {
        //given
        when(productDao.save(product)).thenReturn(product);

        //when
        Product product_save = productService.create(product);

        assertAll(
            () -> assertThat(product.getName()).isEqualTo(product_save.getName()),
            () -> assertThat(product.getPrice()).isEqualTo(product_save.getPrice()),
            () -> assertThat(product_save.getId()).isNotNull()
        );
    }

    @Test
    @DisplayName("가격 음수일때 상품 저장 불가")
    void createProductWithMinusPriceThrowError() {
        //given
        Product minusProduct = new Product();
        minusProduct.setPrice(BigDecimal.valueOf(-1));
        minusProduct.setName("마이너스상품");

        //when
        assertThatThrownBy(() -> productService.create(minusProduct)).isInstanceOf(
            RuntimeException.class);
    }

    @Test
    @DisplayName("가격이 NULL일때 상품 저장 불가")
    void createProductWithNullPriceThrowError() {
        //given
        Product minusProduct = new Product();
        minusProduct.setPrice(null);
        minusProduct.setName("널상품");

        //when
        assertThatThrownBy(() -> productService.create(minusProduct)).isInstanceOf(
            RuntimeException.class);
    }

}