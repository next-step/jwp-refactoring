package kitchenpos.product.service;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품을 생성 한다")
    public void createProduct() {
        //given
        String name = "치킨";
        BigDecimal price = new BigDecimal(20000);
        Product product = new Product(name, price);

        //when
        Product createProduct = productService.create(product);

        //then
        assertThat(createProduct.getName().equals(name)).isTrue();
        assertThat(createProduct.getPrice().compareTo(price)).isSameAs(0);
    }


    @Test
    @DisplayName("상품을 생성 실패 - 가격이 음수")
    public void createProductFailByPriceMinus() {
        //given
        String name = "피자";
        BigDecimal price = new BigDecimal(-10000);
        Product product = new Product( name, price);

        //when
        //then
        assertThrows(IllegalArgumentException.class, () ->  productService.create(product));
    }

    @Test
    @DisplayName("상품 리스트를 가져온다")
    public void selectProductList() {
        //when
        List<Product> products = productService.list();

        //then
        for (Product product : products) {
            assertThat(product.getId()).isNotNull();
            assertThat(product.getName()).isNotNull();
            assertThat(product.getPrice()).isNotNull();
        }
    }
}



















