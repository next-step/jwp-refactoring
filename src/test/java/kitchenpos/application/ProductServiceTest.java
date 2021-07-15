package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @Test
    public void 상품생성_성공() {
        //given
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(12000));
        given(productDao.save(product)).willReturn(product);

        //when
        Product savedProduct = productService.create(product);

        //then
        verify(productDao).save(product);
        assertThat(savedProduct.getPrice()).isEqualTo(BigDecimal.valueOf(12000));
    }

    @Test
    public void 상품생성_예외_금액이0보다작을때() {
        //given
        Product product = mock(Product.class);
        given(product.getPrice()).willReturn(BigDecimal.valueOf(-100));

        //when-then
        assertThatThrownBy(() ->productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 상품리스트조회_성공() {
        //given
        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);
        given(productDao.findAll()).willReturn(asList(product1, product2));

        //when
        List<Product> productList = productService.list();

        //then
        verify(productDao).findAll();
        assertThat(productList.size()).isEqualTo(2);
    }

}