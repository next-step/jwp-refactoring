package kitchenpos.tablegroup.acceptance;

import static kitchenpos.order.acceptance.OrderAcceptanceUtils.*;
import static kitchenpos.table.acceptance.OrderTableAcceptanceUtils.*;
import static kitchenpos.tablegroup.acceptance.TableGroupAcceptanceUtils.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.utils.AcceptanceTest;
import kitchenpos.order.acceptance.OrderId;
import kitchenpos.table.acceptance.OrderTableId;

@DisplayName("단체 지정 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    /**
     * given 주문 테이블1, 2 등록되어 있음
     * when 단체 지정 등록 요청
     * then 단체 지정 등록 완료
     * when 단체 지정 해제 요청
     * then 단체 지정 해제 완료
     */
    @DisplayName("단체 지정 관리")
    @Test
    void tableGroup() {
        // given
        OrderTableId 주문_테이블1_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(1, true));
        OrderTableId 주문_테이블2_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(2, true));

        // when
        ExtractableResponse<Response> 단체_지정_등록_요청 = 단체_지정_등록_요청(Arrays.asList(주문_테이블1_ID, 주문_테이블2_ID));

        // then
        단체_지정_등록_완료(단체_지정_등록_요청);

        // when
        TableGroupId 단체_지정_ID = 단체_지정_ID_추출(단체_지정_등록_요청);
        ExtractableResponse<Response> 단체_지정_해제_요청 = 단체_지정_해제_요청(단체_지정_ID);

        // then
        단체_지정_해제_완료(단체_지정_해제_요청);
    }

    /**
     * when 비어있는 주문 테이블 목록으로 단체 지정 등록 요청
     * then 단체 지정 등록 실패
     */
    @DisplayName("단체 지정 관리 - 비어있는 주문 테이블 목록으로 단체 지정 등록 요청")
    @Test
    void tableGroup_empty_order_tables_failed() {
        // when
        List<OrderTableId> 비어있는_주문_테이블_목록 = Collections.emptyList();
        ExtractableResponse<Response> 단체_지정_등록_요청 = 단체_지정_등록_요청(비어있는_주문_테이블_목록);

        // then
        단체_지정_등록_실패(단체_지정_등록_요청);
    }

    /**
     * given 주문 테이블 1개 등록되어 있음
     * when 한개짜리 주문 테이블 목록으로 단체 지정 등록 요청
     * then 단체 지정 등록 실패
     */
    @DisplayName("단체 지정 관리 - 한개짜리 주문 테이블 목록으로 단체 지정 등록 요청")
    @Test
    void tableGroup_single_order_tables_failed() {
        // given
        OrderTableId 주문_테이블1_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(1, true));

        // when
        List<OrderTableId> 한개짜리_주문_테이블_목록 = Collections.singletonList(주문_테이블1_ID);
        ExtractableResponse<Response> 단체_지정_등록_요청 = 단체_지정_등록_요청(한개짜리_주문_테이블_목록);

        // then
        단체_지정_등록_실패(단체_지정_등록_요청);
    }

    /**
     * given 주문 테이블 1개 등록되어 있음
     * and 등록되어 있지 않은 주문 테이블 1개 등록되어 있음
     * when 미리 등록되어 있지 않은 주문 테이블 목록으로 단체 지정 등록 요청
     * then 단체 지정 등록 실패
     */
    @DisplayName("단체 지정 관리 - 미리 등록되어 있지 않은 주문 테이블 목록으로 단체 지정 등록 요청")
    @Test
    void tableGroup_invalid_order_table_id_failed() {
        // given
        OrderTableId 주문_테이블1_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(1, true));
        OrderTableId 등록되어_있지_않은_주문_테이블_ID = new OrderTableId(-1);

        // when
        List<OrderTableId> 등록되어_있지_않은_주문_테이블_목록 = Arrays.asList(주문_테이블1_ID, 등록되어_있지_않은_주문_테이블_ID);
        ExtractableResponse<Response> 단체_지정_등록_요청 = 단체_지정_등록_요청(등록되어_있지_않은_주문_테이블_목록);

        // then
        단체_지정_등록_실패(단체_지정_등록_요청);
    }

    /**
     * given 주문 테이블 1개 등록되어 있음
     * and 비어있지 않은 주문 테이블 등록되어 있음
     * when 비어있지 않은 주문 테이블 목록으로 단체 지정 등록 요청
     * then 단체 지정 등록 실패
     */
    @DisplayName("단체 지정 관리 - 단체 지정 등록 실패")
    @Test
    void tableGroup_not_empty_order_table_failed() {
        // given
        OrderTableId 주문_테이블1_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(1, true));
        OrderTableId 비어있지_않은_주문_테이블_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(2, false));

        // when
        List<OrderTableId> 비어있지_않은_주문_테이블_목록 = Arrays.asList(주문_테이블1_ID, 비어있지_않은_주문_테이블_ID);
        ExtractableResponse<Response> 단체_지정_등록_요청 = 단체_지정_등록_요청(비어있지_않은_주문_테이블_목록);

        // then
        단체_지정_등록_실패(단체_지정_등록_요청);
    }

    /**
     * given 주문 테이블1, 2 등록되어 있음
     * and 주문 테이블1, 2 단체 지정 완료
     * and 주문 테이블 3 등록되어 있음
     * when 이미 단체 지정되어있는 주문 테이블 2로 단체 지정 등록 요청
     * then 단체 지정 등록 실패
     */
    @DisplayName("단체 지정 관리 - 단체 지정 등록 실패")
    @Test
    void tableGroup_create_failed() {
        // given
        OrderTableId 주문_테이블1_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(1, true));
        OrderTableId 주문_테이블2_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(2, true));
        단체_지정_등록_요청(Arrays.asList(주문_테이블1_ID, 주문_테이블2_ID));
        OrderTableId 주문_테이블3_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(3, true));

        // when
        ExtractableResponse<Response> 단체_지정_등록_요청 = 단체_지정_등록_요청(Arrays.asList(주문_테이블2_ID, 주문_테이블3_ID));

        // then
        단체_지정_등록_실패(단체_지정_등록_요청);
    }

    /**
     * given 주문 테이블1, 2 등록되어 있음
     * and 단체 지정 등록 요청
     * and 주문 테이블1로 주문 등록되어 있음(주문 상태 조리중)
     * when 단체 지정 해제 요청
     * then 단체 지정 해제 실패
     * given 주문 상태 식사중으로 변경
     * when 단체 지정 해제 요청
     * then 단체 지정 해제 실패
     * given 주문 상태 완료로 변경
     * when 단체 지정 해제 요청
     * when 단체 지정 해제 성공
     */
    @DisplayName("단체 지정 관리 - 주문 상태 조리중/식사중 단체 지정 해제 실패")
    @Test
    void tableGroup_delete() {
        // given
        OrderTableId 주문_테이블1_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(1, true));
        OrderTableId 주문_테이블2_ID = 주문_테이블_ID_추출(주문_테이블_등록_요청(2, true));

        TableGroupId 단체_지정_ID = 단체_지정_ID_추출(단체_지정_등록_요청(Arrays.asList(주문_테이블1_ID, 주문_테이블2_ID)));
        OrderId 주문_ID = 주문_등록되어_있음(주문_테이블1_ID);

        // when
        ExtractableResponse<Response> 조리중_주문_상태_단체_지정_해제_요청 = 단체_지정_해제_요청(단체_지정_ID);

        // then
        단체_지정_해제_실패(조리중_주문_상태_단체_지정_해제_요청);

        // given
        주문_상태_변경_요청(주문_ID, "MEAL");

        // when
        ExtractableResponse<Response> 식사중_주문_상태_단체_지정_해제_요청 = 단체_지정_해제_요청(단체_지정_ID);

        // then
        단체_지정_해제_실패(식사중_주문_상태_단체_지정_해제_요청);

        // given
        주문_상태_변경_요청(주문_ID, "COMPLETION");

        // when
        ExtractableResponse<Response> 완료_주문_상태_단체_지정_해제_요청 = 단체_지정_해제_요청(단체_지정_ID);

        // then
        단체_지정_해제_완료(완료_주문_상태_단체_지정_해제_요청);
    }
}
