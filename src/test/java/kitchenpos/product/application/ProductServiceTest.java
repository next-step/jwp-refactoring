package kitchenpos.product.application;

import static kitchenpos.helper.ProductFixtures.제육덮밥;
import static kitchenpos.helper.ProductFixtures.제육덮밥_가격NULL;
import static kitchenpos.helper.ProductFixtures.제육덮밥_가격마이너스;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.dto.ProductResponse;
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
        ProductResponse result = productService.create(제육덮밥);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getPrice()).isEqualTo(제육덮밥.getPrice());
    }

    @Order(2)
    @DisplayName("상품 가격이 null 이거나 0원 미만이면 등록 할 수 없다.")
    @Test
    void create_price_null_or_less_then_zero() {
        //when then
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(()-> productService.create(제육덮밥_가격NULL)),
                () -> assertThatIllegalArgumentException().isThrownBy(()-> productService.create(제육덮밥_가격마이너스))
        );
    }

    @Order(3)
    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        //when
        List<ProductResponse> result = productService.list();

        //then
        assertThat(result.stream().map(ProductResponse::getName).collect(Collectors.toList()))
                .contains(제육덮밥.getName());
    }
}
