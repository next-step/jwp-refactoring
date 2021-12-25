package kitchenpos.product;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {

    @DisplayName("상품 생성")
    @Test
    void registerProduct() {

        //given
        final String productName = "후라이드";
        final BigDecimal productPrice = new BigDecimal("16000");

        //when
        Product product = Product.create(productName, productPrice);

        //then
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo(productName);
        assertThat(product.getPriceProduct()).isEqualTo(new ProductPrice(productPrice));
    }
}
