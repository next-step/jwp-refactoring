package kitchenpos.table.acceptance;

import static kitchenpos.table.acceptance.TableAcceptanceTestHelper.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;

public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 테이블 생성")
    @Test
    void createOrderTable() {
        // when
        ExtractableResponse<Response> 주문_테이블_생성_요청_응답 = 주문_테이블_생성_요청(true, 0, null);

        // then
        주문_테이블_생성됨(주문_테이블_생성_요청_응답);
    }

    @DisplayName("주문 테이블 목록 조회")
    @Test
    void listOrderTable() {
        // when
        ExtractableResponse<Response> 주문_테이블_목록_조회_응답 = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_목록_조회됨(주문_테이블_목록_조회_응답);
    }

    @DisplayName("주문 테이블 비움처리")
    @Test
    void changeEmpty() {
        // given
        ExtractableResponse<Response> 주문_테이블_생성_요청_응답 = 주문_테이블_생성_요청(false, 7, null);
        Long 주문_테이블_아이디 = 주문_테이블_생성_요청_응답.as(OrderTable.class).getId();

        // when
        ExtractableResponse<Response> 주문_테이블_정리_요청_응답 = 주문_테이블_정리_요청(주문_테이블_아이디);

        // then
        주문_테이블_정리됨(주문_테이블_정리_요청_응답);
    }

    @DisplayName("손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        // given
        ExtractableResponse<Response> 주문_테이블_생성_요청_응답 = 주문_테이블_생성_요청(false, 7, null);
        Long 주문_테이블_아이디 = 주문_테이블_생성_요청_응답.as(OrderTable.class).getId();
        int 손님_수 = 5;

        // when
        ExtractableResponse<Response> 주문_테이블_손님_수_변경_요청_응답 = 주문_테이블_손님_수_변경_요청(주문_테이블_아이디, 손님_수);

        // then
        주문_테이블_손님_수_변경됨(주문_테이블_손님_수_변경_요청_응답, 손님_수);
    }
}
