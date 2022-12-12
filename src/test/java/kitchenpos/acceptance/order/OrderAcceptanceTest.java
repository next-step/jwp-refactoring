package kitchenpos.acceptance.order;

import static kitchenpos.acceptance.menu.MenuAcceptanceUtils.*;
import static kitchenpos.acceptance.order.OrderAcceptanceUtils.*;
import static kitchenpos.acceptance.ordertable.OrderTableAcceptanceUtils.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.acceptance.menu.MenuId;
import kitchenpos.acceptance.ordertable.OrderTableId;

@DisplayName("주문 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    /**
     * given 주문 테이블 등록되어 있음
     * and 메뉴 등록되어 있음
     * when 주문 등록 요청
     * then 주문 등록 완료
     * when 주문 목록 조회 요청
     * then 주문 목록 조회 완료
     */
    @DisplayName("주문 관리")
    @Test
    void order() {
        // given
        OrderTableId 주문_테이블_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(1, false));
        MenuId 메뉴_ID = 메뉴_등록되어_있음();
        List<OrderLineItemParam> 주문_항목_목록 = Arrays.asList(new OrderLineItemParam(메뉴_ID, 1));

        // when
        ExtractableResponse<Response> 주문_등록_요청 = 주문_등록_요청(주문_테이블_ID, 주문_항목_목록);

        // then
        주문_등록_완료(주문_등록_요청);

        // when
        ExtractableResponse<Response> 주문_목록_조회_요청 = 주문_목록_조회_요청();

        // then
        주문_목록_조회_완료(주문_목록_조회_요청);
    }

    /**
     * given 주문 테이블 등록되어 있음
     * when 빈 주문 항목 목록으로 주문 등록 요청
     * then 주문 등록 실패
     */
    @DisplayName("주문 관리 - 빈 주문 테이블 목록으로 주문 등록 요청")
    @Test
    void order_empty_order_tables_failed() {
        // given
        OrderTableId 주문_테이블_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(1, false));
        List<OrderLineItemParam> 빈_주문_항목_목록 = Collections.emptyList();

        // when
        ExtractableResponse<Response> 주문_등록_요청 = 주문_등록_요청(주문_테이블_ID, 빈_주문_항목_목록);

        // then
        주문_등록_실패(주문_등록_요청);
    }

    /**
     * given 주문 테이블 등록되어 있음
     * and 메뉴 등록되어 있지 않음
     * when 등록되어 있지 않은 메뉴 ID로 주문 등록 요청
     * then 주문 등록 실패
     */
    @DisplayName("주문 관리 - 등록되어 있지 않은 메뉴로 주문 등록 요청")
    @Test
    void order_invalid_menu_order_tables_failed() {
        // given
        OrderTableId 주문_테이블_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(1, false));
        MenuId 등록되어_있지_않은_메뉴_ID = new MenuId(-1);
        List<OrderLineItemParam> 주문_항목_목록 = Arrays.asList(new OrderLineItemParam(등록되어_있지_않은_메뉴_ID, 1));

        // when
        ExtractableResponse<Response> 주문_등록_요청 = 주문_등록_요청(주문_테이블_ID, 주문_항목_목록);

        // then
        주문_등록_실패(주문_등록_요청);
    }

    /**
     * given 주문 테이블 등록되어 있지 않음
     * and 메뉴 등록되어 있음
     * when 존재하지 않는 주문 테이블 ID로 주문 등록 요청
     * then 주문 등록 실패
     */
    @DisplayName("주문 관리 - 존재하지 않는 주문 테이블 ID로 주문 등록 요청")
    @Test
    void order_invalid_order_table_id_failed() {
        // given
        OrderTableId 주문_테이블_ID = new OrderTableId(-1);
        MenuId 메뉴_ID = 메뉴_등록되어_있음();
        List<OrderLineItemParam> 주문_항목_목록 = Arrays.asList(new OrderLineItemParam(메뉴_ID, 1));

        // when
        ExtractableResponse<Response> 주문_등록_요청 = 주문_등록_요청(주문_테이블_ID, 주문_항목_목록);

        // then
        주문_등록_실패(주문_등록_요청);
    }

    /**
     * given 비어있는 상태의 주문 테이블 등록되어 있음
     * and 메뉴 등록되어 있음
     * when 비어있는 상태의 주문 테이블  ID로 주문 등록 요청
     * then 주문 등록 실패
     */
    @DisplayName("주문 관리 - 존재하지 않는 주문 테이블 ID로 주문 등록 요청")
    @Test
    void order_empty_order_table_failed() {
        // given
        OrderTableId 주문_테이블_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(1, true));
        MenuId 메뉴_ID = 메뉴_등록되어_있음();
        List<OrderLineItemParam> 주문_항목_목록 = Arrays.asList(new OrderLineItemParam(메뉴_ID, 1));

        // when
        ExtractableResponse<Response> 주문_등록_요청 = 주문_등록_요청(주문_테이블_ID, 주문_항목_목록);

        // then
        주문_등록_실패(주문_등록_요청);
    }

    /**
     * given 주문이 등록되어 있음
     * when 주문 상태 변경 요청
     * then 주문 상태 변경 완료
     */
    @DisplayName("주문 관리 - 주문 상태 변경")
    @Test
    void order_status() {
        // given
        OrderId 주문_ID = 주문_등록되어_있음();

        // when
        String orderStatus = "COMPLETION";
        ExtractableResponse<Response> 주문_상태_변경_요청 = 주문_상태_변경_요청(주문_ID, orderStatus);

        // then
        주문_상태_변경_완료(주문_상태_변경_요청);
    }

    /**
     * given 주문이 등록되어 있지 않음
     * when 잘못된 주문 ID로 주문 상태 변경 요청
     * then 주문 상태 변경 실패
     */
    @DisplayName("주문 관리 - 잘못된 주문 ID로 주문 상태 변경 요청")
    @Test
    void order_status_invalid_order_id_failed() {
        // given
        OrderId 잘못된_주문_ID = new OrderId(-1);

        // when
        String orderStatus = "COMPLETION";
        ExtractableResponse<Response> 주문_상태_변경_요청 = 주문_상태_변경_요청(잘못된_주문_ID, orderStatus);

        // then
        주문_상태_변경_실패(주문_상태_변경_요청);
    }

    /**
     * given 주문이 등록되어 있음
     * and 주문 상태가 이미 COMPLETION으로 변경되었음
     * when 주문 ID로 주문 상태 변경 요청
     * then 주문 상태 변경 실패
     */
    @DisplayName("주문 관리 - 이미 완료된 주문 상태 변경 요청")
    @Test
    void order_already_completed_failed() {
        // given
        OrderId 주문_ID = 주문_등록되어_있음();
        주문_상태_변경_요청(주문_ID, "COMPLETION");

        // when
        String orderStatus = "MEAL";
        ExtractableResponse<Response> 주문_상태_변경_요청 = 주문_상태_변경_요청(주문_ID, orderStatus);

        // then
        주문_상태_변경_실패(주문_상태_변경_요청);
    }
}
