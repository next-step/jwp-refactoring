package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
        OrderTable 첫번째_테이블 = TableAcceptanceTest.주문_테이블_등록되어_있음(4, true);
        OrderTable 두번째_테이블 = TableAcceptanceTest.주문_테이블_등록되어_있음(4, true);

        List<OrderTable> orderTables = Arrays.asList(첫번째_테이블, 두번째_테이블);
        TableGroup 요청_단체테이블 = new TableGroup(orderTables);

        // when
        ExtractableResponse<Response> createResponse = 단체_지정_요청(요청_단체테이블);
        // then
        TableGroup 생성된_단체테이블 = 단체_지정됨(createResponse);

        // when
        ExtractableResponse<Response> unGroupResponse = 단체_지정_해제_요청(생성된_단체테이블);
        // then
        단체_지정_해제됨(unGroupResponse);
    }

    /**
     * 요청 관련
     */
    private ExtractableResponse<Response> 단체_지정_요청(TableGroup 요청_단체테이블) {
        return Http.post("/api/table-groups", 요청_단체테이블);
    }

    private ExtractableResponse<Response> 단체_지정_해제_요청(TableGroup 생성된_단체테이블) {
        return Http.delete("/api/table-groups/" + 생성된_단체테이블.getId());
    }

    /**
     * 응답 관련
     */
    private TableGroup 단체_지정됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return createResponse.as(TableGroup.class);
    }

    private void 단체_지정_해제됨(ExtractableResponse<Response> unGroupResponse) {
        assertThat(unGroupResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
