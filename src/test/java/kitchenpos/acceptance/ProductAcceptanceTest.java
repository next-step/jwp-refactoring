package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.domain.Product;
import kitchenpos.utils.KitchenPosBehaviors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ProductAcceptanceTest extends AcceptanceTest {
    @Test
    @DisplayName("상품 생성 및 조회 기능 인수테스트")
    void productAcceptanceTest() {
        ExtractableResponse<Response> createResponse = KitchenPosBehaviors.상품_생성_요청("강정치킨", 17000);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<Product> products = KitchenPosBehaviors.상품목록_조회();
        assertThat(products).hasSize(1);
    }
}
