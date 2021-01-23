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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    public void createProduct() {
        Product mockProduct = mock(Product.class);
        given(productDao.save(any()))
                .willReturn(mockProduct);

        Product product = new Product();
        product.setPrice(new BigDecimal(1000));
        Product result = productService.create(product);

        assertThat(result).isEqualTo(mockProduct);
        verify(productDao, atMostOnce()).save(product);
    }

    @DisplayName("상품 생성 불가능 케이스 1 - 금액이 0원 이하인 경우")
    @Test
    public void invalidCreate() {
        Product product = new Product();
        product.setPrice(new BigDecimal(-1000));
        assertThatThrownBy(() -> {
            productService.create(product);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 생성 불가능 케이스 2 - 금액이 올바르지 않은 경우")
    @Test
    public void invalidCreate2() {
        Product product = new Product();
        assertThatThrownBy(() -> {
            productService.create(product);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 상품을 조회한다")
    @Test
    public void list() {
        List<Product> products = mock(List.class);
        given(productDao.findAll())
                .willReturn(products);

        List<Product> result = productService.list();
        assertThat(result).isEqualTo(products);
        verify(productDao, atMostOnce()).findAll();
    }

}
