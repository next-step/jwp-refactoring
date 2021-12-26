package kitchenpos.tablegroup.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.acceptance.OrderAcceptanceTest;
import kitchenpos.table.acceptance.TableAcceptanceTest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.utils.Http;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단체 지정 관리 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    @DisplayName("단체 지정을 관리한다")
    @Test
    void testManagement() {
        // given
        OrderTableResponse 첫번째_테이블 = TableAcceptanceTest.주문_테이블_등록되어_있음(4, true);
        OrderTableResponse 두번째_테이블 = TableAcceptanceTest.주문_테이블_등록되어_있음(4, true);

        List<OrderTableRequest> orderTables = Arrays.asList(new OrderTableRequest(첫번째_테이블.getId()), new OrderTableRequest(두번째_테이블.getId()));
        TableGroupRequest 요청_단체테이블 = new TableGroupRequest(orderTables);

        // when
        ExtractableResponse<Response> createResponse = 단체_지정_요청(요청_단체테이블);
        // then
        TableGroupResponse 생성된_단체테이블 = 단체_지정됨(createResponse);

        // when
        ExtractableResponse<Response> unGroupResponse = 단체_지정_해제_요청(생성된_단체테이블);
        // then
        단체_지정_해제됨(unGroupResponse);
    }

    @DisplayName("주문이 생성되지 않아야 단체 지정을 해제할 수 있다")
    @Test
    void testUngroupException() {
        // given
        OrderTableResponse 첫번째_테이블 = TableAcceptanceTest.주문_테이블_등록되어_있음(4, true);
        OrderTableResponse 두번째_테이블 = TableAcceptanceTest.주문_테이블_등록되어_있음(4, true);

        List<OrderTableRequest> orderTables = Arrays.asList(new OrderTableRequest(첫번째_테이블.getId()), new OrderTableRequest(두번째_테이블.getId()));
        TableGroupRequest 요청_단체테이블 = new TableGroupRequest(orderTables);
        TableGroupResponse 생성된_단체테이블 = 단체_지정되어_있음(요청_단체테이블);

        OrderAcceptanceTest.주문_생성_되어_있음(첫번째_테이블);

        // when
        ExtractableResponse<Response> unGroupResponse = 단체_지정_해제_요청(생성된_단체테이블);

        // then
        단체_지정_해체에_실패함(unGroupResponse);
    }

    @DisplayName("비어있는 테이블만 단체지정을 할 수 있다")
    @Test
    void testGroupException() {
        // given
        OrderTableResponse 첫번째_테이블 = TableAcceptanceTest.주문_테이블_등록되어_있음(4, false);
        OrderTableResponse 두번째_테이블 = TableAcceptanceTest.주문_테이블_등록되어_있음(4, false);

        List<OrderTableRequest> orderTables = Arrays.asList(new OrderTableRequest(첫번째_테이블.getId()), new OrderTableRequest(두번째_테이블.getId()));
        TableGroupRequest 요청_단체테이블 = new TableGroupRequest(orderTables);

        // when
        ExtractableResponse<Response> createResponse = 단체_지정_요청(요청_단체테이블);

        // then
        단체_지정에_실패함(createResponse);
    }

    @DisplayName("생성된 주문 테이블로 묶어야 한다")
    @Test
    void whenNotFoundTableThenThrowException() {
        // given
        OrderTableResponse 첫번째_테이블 = TableAcceptanceTest.주문_테이블_등록되어_있음(4, false);

        List<OrderTableRequest> orderTables = Arrays.asList(new OrderTableRequest(첫번째_테이블.getId()), new OrderTableRequest(99999L));
        TableGroupRequest 요청_단체테이블 = new TableGroupRequest(orderTables);

        // when
        ExtractableResponse<Response> createResponse = 단체_지정_요청(요청_단체테이블);

        // then
        단체_지정에_실패함(createResponse);
    }

    /**
     * 요청 관련
     */
    private ExtractableResponse<Response> 단체_지정_요청(TableGroupRequest tableGroupRequest) {
        return Http.post("/api/table-groups", tableGroupRequest);
    }

    private ExtractableResponse<Response> 단체_지정_해제_요청(TableGroupResponse 생성된_단체테이블) {
        return Http.delete("/api/table-groups/" + 생성된_단체테이블.getId());
    }

    /**
     * 응답 관련
     */
    private TableGroupResponse 단체_지정됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return createResponse.as(TableGroupResponse.class);
    }

    private void 단체_지정_해제됨(ExtractableResponse<Response> unGroupResponse) {
        assertThat(unGroupResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 단체_지정_해체에_실패함(ExtractableResponse<Response> unGroupResponse) {
        assertThat(unGroupResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 단체_지정에_실패함(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * 테스트 픽스처 관련
     */
    private TableGroupResponse 단체_지정되어_있음(TableGroupRequest 요청_단체테이블) {
        return 단체_지정_요청(요청_단체테이블).as(TableGroupResponse.class);
    }
}
