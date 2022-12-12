package kitchenpos.acceptance.ordertable;

import static kitchenpos.acceptance.order.OrderAcceptanceUtils.*;
import static kitchenpos.acceptance.ordertable.OrderTableAcceptanceUtils.*;
import static kitchenpos.acceptance.tablegroup.TableGroupAcceptanceUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.acceptance.order.OrderId;

@DisplayName("주문 테이블 인수 테스트")
public class OrderTableAcceptanceTest extends AcceptanceTest {

    /**
     * when 주문 테이블 등록 요청
     * then 주문 테이블 등록 완료
     * when 주문 테이블 목록 조회 요청
     * then 주문 테이블 목록 조회 완료
     * when 주문 테이블 테이블 상태 변경 요청
     * then 주문 테이블 상태 변경 완료
     * when 주문 테이블 손님 수 변경 요청
     * then 주문 테이블 손님 수 변경 완료
     */
    @DisplayName("주문 테이블 관리")
    @Test
    void orderTable() {
        // when
        ExtractableResponse<Response> 주문_테이블_등록_요청 = 주문_테이블_등록_요청(0, true);

        // then
        주문_테이블_등록_요청_완료(주문_테이블_등록_요청);
        OrderTableId 주문_테이블_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청);

        // when
        ExtractableResponse<Response> 주문_테이블_조회_요청 = 주문_테이블_조회_요청();

        // then
        주문_테이블_조회_요청_완료(주문_테이블_조회_요청);
        assertThat(주문_테이블_추출(주문_테이블_조회_요청)).hasSize(1);

        // when
        boolean 변경할_테이블_상태 = false;
        ExtractableResponse<Response> 주문_테이블_테이블_상태_변경_요청 = 주문_테이블_상태_변경_요청(주문_테이블_ID, 변경할_테이블_상태);

        // then
        주문_테이블_상태_변경_완료(주문_테이블_테이블_상태_변경_요청);

        // when
        int 변경할_손님수 = 4;
        ExtractableResponse<Response> 주문_테이블_손님_수_변경_요청 = 주문_테이블_손님_수_변경_요청(주문_테이블_ID, 변경할_손님수);

        // then
        주문_테이블_손님_수_변경_완료(주문_테이블_손님_수_변경_요청);
    }

    /**
     * given 주문 테이블 등록되어 있지 않음
     * when 잘못된 주문 테이블 ID로 테이블 상태 변경 요청
     * then 주문 테이블 상태 변경 실패
     */
    @DisplayName("주문 테이블 관리 - 잘못된 주문 테이블 ID로 테이블 상태 변경 실패")
    @Test
    void orderTable_empty_invalid_order_table_id_failed() {
        // given
        OrderTableId 잘못된_주문_테이블_ID = new OrderTableId(-1);
        boolean 변경할_테이블_상태 = true;

        // when
        ExtractableResponse<Response> 주문_테이블_상태_변경_요청 = 주문_테이블_상태_변경_요청(잘못된_주문_테이블_ID, 변경할_테이블_상태);

        // then
        주문_테이블_상태_변경_실패(주문_테이블_상태_변경_요청);
    }

    /**
     * given 주문 테이블1, 2 등록되어 있음
     * and 주문 테이블 단체 지정되어 있음
     * when 이미 단체 지정이 되어있는 주문 테이블 상태 변경 요청
     * then 주문 테이블 상태 변경 실패
     */
    @DisplayName("주문 테이블 관리 - 이미 단체 지정이 되어있는 주문 테이블 상태 변경 실패")
    @Test
    void orderTable_empty_failed() {
        // given
        OrderTableId 주문_테이블1_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(1, true));
        OrderTableId 주문_테이블2_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(2, true));

        단체_지정_등록_요청(Arrays.asList(주문_테이블1_ID, 주문_테이블2_ID));

        // when
        ExtractableResponse<Response> 주문_테이블_상태_변경_요청 = 주문_테이블_상태_변경_요청(주문_테이블1_ID, true);

        // then
        주문_테이블_상태_변경_실패(주문_테이블_상태_변경_요청);
    }

    /**
     * given 주문 테이블 등록되어 있음
     * and 주문 등록되어 있음(조리중)
     * when 조리중인 주문 테이블 상태 변경 요청
     * then 주문 테이블 상태 변경 실패
     * given 주문 상태 식사중으로 변경
     * when 조리중인 주문 테이블 상태 변경 요청
     * then 주문 테이블 상태 변경 실패
     */
    @DisplayName("주문 테이블 관리 - 주문 등록되어 있는 지정이 이미 되어있는 주문 테이블 상태 변경 실패")
    @Test
    void orderTable_invalid_order_status_failed() {
        // given
        OrderTableId 주문_테이블_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(1, false));
        OrderId 주문_ID = 주문_등록되어_있음(주문_테이블_ID);

        // when
        ExtractableResponse<Response> 조리중_주문_테이블_상태_변경_요청 = 주문_테이블_상태_변경_요청(주문_테이블_ID, true);

        // then
        주문_테이블_상태_변경_실패(조리중_주문_테이블_상태_변경_요청);

        // given
        주문_상태_변경_요청(주문_ID, "MEAL");

        // when
        ExtractableResponse<Response> 식사중_주문_테이블_상태_변경_요청 = 주문_테이블_상태_변경_요청(주문_테이블_ID, true);

        // then
        주문_테이블_상태_변경_실패(식사중_주문_테이블_상태_변경_요청);
    }

    /**
     * given 주문 테이블 등록되어 있음
     * when 0명 미만으로 방문한 손님 수 상태 변경 요청
     * then 주문 테이블 방문한 손님 수 변경 실패
     * when 잘못된 주문 테이블 ID로 방문한 손님 수 상태 변경 요청
     * then 주문 테이블 방문한 손님 수 변경 실패
     * given 주문 테이블 빈 테이블 상태로 변경
     * when 빈 테이블 상태의 주문 테이블 손님 수 상태 변경 요청
     * then 주문 테이블 방문한 손님 수 변경 실패
     */
    @DisplayName("주문 테이블 관리 - 테이블 방문한 손님 수 변경 실패")
    @Test
    void orderTable_number_of_guest_failed() {
        // given
        ExtractableResponse<Response> 주문_테이블_등록_요청 = 주문_테이블_등록_요청(0, false);
        OrderTableId 주문_테이블_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청);
        int 변경할_손님_수 = 4;

        // when
        int 변경할_손님_수_0명_미만 = -1;
        ExtractableResponse<Response> 손님_수_0명_미만으로_테이블_손님_수_변경_요청 = 주문_테이블_손님_수_변경_요청(주문_테이블_ID, 변경할_손님_수_0명_미만);

        // then
        주문_테이블_손님_수_변경_실패(손님_수_0명_미만으로_테이블_손님_수_변경_요청);

        // when
        OrderTableId 잘못된_주문_테이블_ID = new OrderTableId(-1);
        ExtractableResponse<Response> 잘못된_주문_테이블_ID로_테이블_손님_수_변경_요청 = 주문_테이블_손님_수_변경_요청(잘못된_주문_테이블_ID, 변경할_손님_수);

        // then
        주문_테이블_손님_수_변경_실패(잘못된_주문_테이블_ID로_테이블_손님_수_변경_요청);

        // given
        주문_테이블_상태_변경_요청(주문_테이블_ID, true);

        // when
        ExtractableResponse<Response> 빈_테이블_상태의_테이블_손님_수_변경_요청 = 주문_테이블_손님_수_변경_요청(잘못된_주문_테이블_ID, 변경할_손님_수);

        // then
        주문_테이블_손님_수_변경_실패(빈_테이블_상태의_테이블_손님_수_변경_요청);
    }
}
