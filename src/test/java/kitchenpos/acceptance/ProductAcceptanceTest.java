package kitchenpos.acceptance;

import static kitchenpos.acceptance.AcceptanceTestHelper.assertCreatedStatus;
import static kitchenpos.acceptance.AcceptanceTestHelper.assertInternalServerErrorStatus;
import static kitchenpos.acceptance.AcceptanceTestHelper.assertOkStatus;
import static kitchenpos.acceptance.ProductAcceptanceTestHelper.createProduct;
import static kitchenpos.acceptance.ProductAcceptanceTestHelper.getProducts;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    void 생성시_이름이_존재하지않는경우_생성실패() {
        assertInternalServerErrorStatus(createProduct(null, BigDecimal.ONE));
    }

    @Test
    void 생성시_가격이_0원미만일경우_생성실패() {
        assertInternalServerErrorStatus(createProduct("pasta", new BigDecimal(-1)));
    }

    @Test
    void 생성시_가격이_0원이상일경우_생성성공() {
        assertCreatedStatus(createProduct("pasta", BigDecimal.ONE));
    }

    @Test
    void 조회시_존재하는목록반환() {
        Product created = createProduct("pizza", BigDecimal.TEN).as(Product.class);
        ExtractableResponse<Response> response = getProducts();
        assertAll(
            () -> assertOkStatus(response),
            () -> assertThat(mapToProductIds(response)).contains(created.getId())
        );
    }

    private List<Long> mapToProductIds(ExtractableResponse<Response> response) {
        return response.jsonPath()
            .getList(".", Product.class)
            .stream()
            .map(Product::getId)
            .collect(Collectors.toList());
    }

}
