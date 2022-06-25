package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.acceptance.helper.KitchenPosBehaviors;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ProductAcceptanceTest extends AcceptanceTest {
    @Test
    void 상품을_등록하고_조회한다() {
        ExtractableResponse<Response> createResponse = KitchenPosBehaviors.상품_생성_요청("강정치킨", 17000);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<Product> products = KitchenPosBehaviors.상품목록_조회();
        assertThat(products).hasSize(1);
    }
}
