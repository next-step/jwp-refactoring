package kitchenpos.menu.product;

import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @DisplayName("상품을 등록한다.")
    @Test
    void registerProduct() {

        //given
        Product product = new Product();
        product.setId(1L);
        product.setName("후라이드");
        product.setPrice(new BigDecimal("16000"));

        when(productDao.save(any())).thenReturn(product);

        //when
        Product savedProduct = productService.create(product);

        //then
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0L);
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
    }

    @DisplayName("상품 리스트를 조회한다.")
    @Test
    void getProducts() {

        //given
        List<Product> products = new ArrayList<>();
        Product 후라이드 = new Product();
        후라이드.setId(1L);
        후라이드.setPrice(new BigDecimal("16000"));
        후라이드.setName("후라이드");

        Product 치즈버거 = new Product();
        치즈버거.setId(2L);
        치즈버거.setPrice(new BigDecimal("8000"));
        치즈버거.setName("치즈버거");

        products.add(후라이드);
        products.add(치즈버거);

        when(productDao.findAll()).thenReturn(products);

        //when
        List<Product> findProducts = productService.list();

        //then
        assertThat(findProducts).isNotEmpty();
        assertThat(findProducts.size()).isEqualTo(products.size());
        assertThat(findProducts).extracting(Product::getName).containsExactly("후라이드", "치즈버거");
        assertThat(findProducts.stream()
                .map(Product::getPrice)
                .reduce((a, b) -> a.add(b))
                .orElse(BigDecimal.ZERO))
                .isEqualTo(new BigDecimal("24000"));
    }


}
