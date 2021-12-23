package kitchenpos.table;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.utils.RestTestApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("단체 지정 인수 테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {

    private static final String URI = "/api/table-groups";

    OrderTableRequest 단체_지정할_테이블1;
    OrderTableRequest 단체_지정할_테이블2;

    OrderTableResponse 단체_테이블1;
    OrderTableResponse 단체_테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        단체_지정할_테이블1 = new OrderTableRequest(3, true);
        단체_지정할_테이블2 = new OrderTableRequest(3, true);

        단체_테이블1 = TableAcceptanceTest.테이블_등록되어_있음(단체_지정할_테이블1)
            .as(OrderTableResponse.class);
        단체_테이블2 = TableAcceptanceTest.테이블_등록되어_있음(단체_지정할_테이블2)
            .as(OrderTableResponse.class);
    }

    @DisplayName("단체를 지정한다.")
    @Test
    void create() {
        // when
        TableGroupRequest 단체_지정_요청 = new TableGroupRequest(Arrays.asList(단체_테이블1.getId(), 단체_테이블2.getId()));
        ExtractableResponse<Response> 단체_지정_응답 = 단체_지정_등록_요청(단체_지정_요청);

        // then
        단체_지정됨(단체_지정_응답);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        TableGroupRequest 단체_지정_요청 = new TableGroupRequest(Arrays.asList(단체_테이블1.getId(), 단체_테이블2.getId()));
        ExtractableResponse<Response> 단체_지정_응답 = 단체_지정_등록_요청(단체_지정_요청);
        단체_지정됨(단체_지정_응답);

        // when
        ExtractableResponse<Response> 단체_지정_해제_응답 = 단체_지정_해제_요청(단체_지정_응답);

        // then
        단체_지정_해제됨(단체_지정_해제_응답);
    }

    private ExtractableResponse<Response> 단체_지정_해제_요청(ExtractableResponse<Response> response) {
        return RestTestApi.delete(response.header("Location"));
    }

    private void 단체_지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 단체_지정_등록_요청(TableGroupRequest tableGroupRequest) {
        return RestTestApi.post(URI, tableGroupRequest);
    }

    private void 단체_지정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
