package kitchenpos.product;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품 생성하기")
    @Test
    void createProduct() {

        //given
        final String productName = "후라이드";
        final BigDecimal productPrice = new BigDecimal("16000");
        Product expectedProduct = Product.create(productName, productPrice);

        //when
        Product actualProduct = productRepository.save(expectedProduct);

        //then
        assertThat(actualProduct).isNotNull();
        assertThat(actualProduct.getId()).isGreaterThan(0L);
        assertThat(actualProduct.getName()).isEqualTo(productName);
        assertThat(actualProduct.getPriceProduct()).isEqualTo(new ProductPrice(productPrice));
    }

    @DisplayName("상품 리스트 조회")
    @Test
    void getProducts() {

        //given
        List<Product> products = Arrays.asList(
                Product.create("후라이드", new BigDecimal("16000")),
                Product.create("콜라", new BigDecimal("2000"))
        );

        productRepository.saveAll(products);

        //when
        List<Product> findProducts = productRepository.findAll();

        //then
        assertThat(findProducts).isNotEmpty();
        assertThat(findProducts).extracting(Product::getName).contains("후라이드", "콜라");
    }
}
