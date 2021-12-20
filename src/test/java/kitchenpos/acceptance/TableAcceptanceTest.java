package kitchenpos.acceptance;

import static kitchenpos.acceptance.step.TableAcceptanceStep.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.order.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 관리 기능")
class TableAcceptanceTest extends AcceptanceTest {


    @Test
    @DisplayName("주문테이블 관리")
    void 주문테이블_관리() {
        // when
        ExtractableResponse<Response> 주문테이블_등록_결과 = 주문테이블_등록_요청(new OrderTable());
        // then
        OrderTable 등록된_주문테이블 = 주문테이블_등록_검증(주문테이블_등록_결과);

        // when
        ExtractableResponse<Response> 주문테이블_목록조회_결과 = 주문테이블_목록조회();
        주문테이블_목록조회_검증(주문테이블_목록조회_결과, 등록된_주문테이블);

        // when
        ExtractableResponse<Response> 사용중_테이블_변경_결과 = 주문테이블_빈테이블_상태_변경_요청(등록된_주문테이블.getId(),
            OrderTable.of(1, false));
        // then
        OrderTable 사용중_테이블 = 빈테이블_변경_검증(사용중_테이블_변경_결과, false);

        // when
        사용중_테이블.setNumberOfGuests(5);
        ExtractableResponse<Response> 방문한_손님_수_변경_결과 = 주문테이블_방문손님수_변경_요청(등록된_주문테이블.getId(),
            사용중_테이블);
        // then
        방문한_손님_수_변경_검증(방문한_손님_수_변경_결과, 사용중_테이블);
    }
}
