package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_등록성공;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_등록_요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_조회_요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_조회성공;

@DisplayName("주문테이블 관련")
public class OrderTableAcceptanceTest extends AcceptanceTest {

    @Test
    void 주문테이블_등록_성공() {
        ExtractableResponse<Response> 주문테이블_등록_결과 = 주문테이블_등록_요청(true,5);

        주문테이블_등록성공(주문테이블_등록_결과);
    }

    @Test
    void 주문테이블_조회_성공() {
        ExtractableResponse<Response> 주문테이블_조회_결과 = 주문테이블_조회_요청();

        주문테이블_조회성공(주문테이블_조회_결과);
    }


}
