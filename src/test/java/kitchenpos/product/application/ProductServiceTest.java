package kitchenpos.product.application;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductDao productDao;
    @InjectMocks
    ProductService productService;

    Product 양념치킨;
    Product 후라이드치킨;

    @BeforeEach
    void setUp() {
        양념치킨 = new Product();
        양념치킨.setPrice(BigDecimal.valueOf(19000));

        후라이드치킨 = new Product();
        후라이드치킨.setPrice(BigDecimal.valueOf(18000));
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {
        //given
        when(productDao.save(양념치킨)).thenReturn(양념치킨);

        //when
        Product createdProduct = productService.create(양념치킨);

        //then
        assertThat(createdProduct.getPrice()).isEqualTo(양념치킨.getPrice());
    }

    @Test
    @DisplayName("상품가격이 0원 미만일 경우 상품 생성을 실패한다.")
    void create_with_exception_when_price_smaller_than_zero() {
        //given
        양념치킨.setPrice(BigDecimal.valueOf(-1));

        //when
        assertThatThrownBy(() -> productService.create(양념치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 상품을 조회한다.")
    void list() {
        //given
        when(productDao.findAll()).thenReturn(Arrays.asList(양념치킨, 후라이드치킨));

        //when
        List<Product> foundProducts = productService.list();

        //then
        assertThat(foundProducts).containsExactly(양념치킨, 후라이드치킨);
    }
}