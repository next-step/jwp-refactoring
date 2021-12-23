package kitchenpos.acceptance;

import static kitchenpos.acceptance.step.TableAcceptanceStep.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.order.OrderTableRequest;
import kitchenpos.dto.order.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 관리 기능")
class TableAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("주문테이블 관리")
    void 주문테이블_관리() {
        // when
        ExtractableResponse<Response> 주문테이블_등록_결과 = 주문테이블_등록_요청(0, true);
        // then
        Long 등록된_주문테이블_번호 = 주문테이블_등록_검증(주문테이블_등록_결과);

        // when
        ExtractableResponse<Response> 주문테이블_목록조회_결과 = 주문테이블_목록조회();
        주문테이블_목록조회_검증(주문테이블_목록조회_결과, 등록된_주문테이블_번호);

        // when
        ExtractableResponse<Response> 방문한_손님_수_변경_결과 = 주문테이블_방문손님수_변경_요청(등록된_주문테이블_번호, 방문한_손님_요청());
        // then
        방문한_손님_수_변경_검증(방문한_손님_수_변경_결과, 방문한_손님_요청());

        // when
        ExtractableResponse<Response> 사용중_테이블_변경_결과 = 주문테이블_빈테이블_상태_변경_요청(등록된_주문테이블_번호,
            빈테이블_비활성화_요청());
        // then
        OrderTableResponse 사용중_테이블 = 빈테이블_변경_검증(사용중_테이블_변경_결과, 빈테이블_비활성화_요청());


    }

    private OrderTableRequest 빈테이블_비활성화_요청() {
        return OrderTableRequest.of(0, false);
    }

    private OrderTableRequest 방문한_손님_요청() {
        return OrderTableRequest.of(5, false);
    }
}
