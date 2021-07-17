package kitchenpos.tablegroup.acceptance;

import kitchenpos.OrderApplication;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static kitchenpos.table.acceptance.TableAcceptanceTest.테이블_등록되어있음;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@ContextConfiguration(classes = OrderApplication.class)
@DisplayName("단체지정 관리 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    private TableResponse 일번테이블;
    private TableResponse 이번테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();
        일번테이블 = 테이블_등록되어있음(new TableRequest(3, true));
        이번테이블 = 테이블_등록되어있음(new TableRequest(3, true));
    }

    @DisplayName("단체지정을 관리한다.")
    @Test
    public void tableGroupManager() throws Exception {
        TableGroupRequest 테이블묶음 = new TableGroupRequest(Arrays.asList(일번테이블.getId(), 이번테이블.getId()));
        ExtractableResponse<Response> postResponse = 단체지정_등록_요청(테이블묶음);
        단체지정_등록됨(postResponse);

        ExtractableResponse<Response> deleteResponse = 단체지정_해제_요청(postResponse);
        단체지정_해제됨(deleteResponse);
    }

    public static TableGroupResponse 단체지정_되어있음(TableGroupRequest tableGroupRequest) {
        ExtractableResponse<Response> response = 단체지정_등록_요청(tableGroupRequest);
        단체지정_등록됨(response);
        return response.as(TableGroupResponse.class);
    }

    private void 단체지정_해제됨(ExtractableResponse<Response> deleteReponse) {
        assertHttpStatus(deleteReponse, NO_CONTENT);
    }

    private ExtractableResponse<Response> 단체지정_해제_요청(ExtractableResponse<Response> postResponse) {
        return delete(postResponse.header("Location"));
    }

    private static void 단체지정_등록됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, CREATED);
    }

    private static ExtractableResponse<Response> 단체지정_등록_요청(TableGroupRequest tableGroupRequest) {
        return post("/api/table-groups", tableGroupRequest);
    }
}
