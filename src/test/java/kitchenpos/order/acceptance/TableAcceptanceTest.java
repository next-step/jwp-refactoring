package kitchenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.order.dto.TableEmptyRequest;
import kitchenpos.order.dto.TableNumberOfGuestsRequest;
import kitchenpos.order.dto.TableRequest;
import kitchenpos.order.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@DisplayName("테이블 관리 기능")
public class TableAcceptanceTest extends AcceptanceTest {
    @DisplayName("테이블을 관리한다.")
    @Test
    public void tableManager() throws Exception {
        TableRequest 테이블 = new TableRequest(5, false);

        ExtractableResponse<Response> postResponse = 테이블_등록_요청(테이블);
        테이블_등록됨(postResponse);

        ExtractableResponse<Response> getResponse = 테이블_목록조회_요청();
        테이블_목록조회됨(getResponse);

        TableNumberOfGuestsRequest 세명 = new TableNumberOfGuestsRequest(3);
        ExtractableResponse<Response> numberOfGuestsResponse = 테이블_방문한손님수_변경_요청(postResponse, 세명);
        테이블_방문한손님수_변경됨(numberOfGuestsResponse);

        TableEmptyRequest 빈테이블 = new TableEmptyRequest(true);
        ExtractableResponse<Response> emptyResponse = 테이블_빈테이블_변경_요청(postResponse, 빈테이블);
        테이블_빈테이블_변경됨(emptyResponse);
    }

    public static TableResponse 테이블_등록되어있음(TableRequest tableRequest) {
        ExtractableResponse<Response> response = 테이블_등록_요청(tableRequest);
        테이블_등록됨(response);
        return response.as(TableResponse.class);
    }

    private void 테이블_빈테이블_변경됨(ExtractableResponse<Response> emptyResponse) {
        assertHttpStatus(emptyResponse, OK);
    }

    private ExtractableResponse<Response> 테이블_빈테이블_변경_요청(ExtractableResponse<Response> postResponse, TableEmptyRequest emptyRequest) {
        return put(postResponse.header("Location") + "/empty", emptyRequest);
    }

    private void 테이블_방문한손님수_변경됨(ExtractableResponse<Response> numberOfGuestsResponse) {
        assertHttpStatus(numberOfGuestsResponse, OK);
    }

    private ExtractableResponse<Response> 테이블_방문한손님수_변경_요청(ExtractableResponse<Response> postResponse, TableNumberOfGuestsRequest numberOfGuestsRequest) {
        return put(postResponse.header("Location") + "/number-of-guests", numberOfGuestsRequest);
    }

    private void 테이블_목록조회됨(ExtractableResponse<Response> getResponse) {
        assertHttpStatus(getResponse, OK);
    }

    private ExtractableResponse<Response> 테이블_목록조회_요청() {
        return get("/api/tables");
    }

    private static void 테이블_등록됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, CREATED);
    }

    private static ExtractableResponse<Response> 테이블_등록_요청(TableRequest tableRequest) {
        return post("/api/tables", tableRequest);
    }
}
