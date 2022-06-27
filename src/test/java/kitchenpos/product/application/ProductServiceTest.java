package kitchenpos.product.application;

import static kitchenpos.helper.ProductFixtures.제육덮밥_가격NULL_요청;
import static kitchenpos.helper.ProductFixtures.제육덮밥_가격마이너스_요청;
import static kitchenpos.helper.ProductFixtures.제육덮밥_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("상품 관련 Service 기능 테스트")
@DataJpaTest
@Import(ProductService.class)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        //when
        ProductResponse result = productService.create(제육덮밥_요청);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getPrice()).isEqualTo(제육덮밥_요청.getPrice());
    }

    @DisplayName("상품 가격이 null 이거나 0원 미만이면 등록 할 수 없다.")
    @Test
    void create_price_null_or_less_then_zero() {
        //when then
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(()-> productService.create(제육덮밥_가격NULL_요청)),
                () -> assertThatIllegalArgumentException().isThrownBy(()-> productService.create(제육덮밥_가격마이너스_요청))
        );
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        //when
        List<ProductResponse> result = productService.findAllProducts();

        //then
        assertThat(result.stream().map(ProductResponse::getName).collect(Collectors.toList()))
                .contains("후라이드", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨");
    }
}
