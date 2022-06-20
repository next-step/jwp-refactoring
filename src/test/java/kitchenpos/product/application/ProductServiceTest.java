package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.helper.Converter;
import kitchenpos.helper.ProductFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@DisplayName("상품 관련 Service 기능 테스트")
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Order(1)
    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        //when
        Product result = productService.create(ProductFixtures.제육덮밥);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getPrice()).isEqualTo(ProductFixtures.제육덮밥.getPrice());
    }

    @Order(2)
    @DisplayName("상품 가격이 null 이거나 0원 미만이면 등록 할 수 없다.")
    @Test
    void create_price_null_or_less_then_zero() {
        //given
        Product product_null = new Product(null,"제육덮밥", null);
        Product product_less_then_zero = new Product(null, "",Converter.convert(-1));

        //when then
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(()-> productService.create(product_null)),
                () -> assertThatIllegalArgumentException().isThrownBy(()-> productService.create(product_less_then_zero))
        );
    }

    @Order(3)
    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        //when
        List<Product> result = productService.list();

        //then
        assertThat(result.stream().map(Product::getName).collect(Collectors.toList()))
                .contains(ProductFixtures.제육덮밥.getName());
    }
}
