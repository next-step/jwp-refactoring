package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.TableRequest;
import kitchenpos.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 관리")
public class TableAcceptanceTest extends AcceptanceTest {
    @DisplayName("테이블을 관리한다")
    @Test
    void manage() {
        TableResponse response = 테이블_생성();
        테이블_조회();
        테이블_고객_수_변경(response);
    }

    private TableResponse 테이블_생성() {
        ExtractableResponse<Response> createdResponse = 생성_요청();

        생성됨(createdResponse);
        return  createdResponse.as(TableResponse.class);
    }

    private void 테이블_조회() {
        ExtractableResponse<Response> selectedResponse = 조회_요청();

        조회됨(selectedResponse);
    }

    private void 테이블_고객_수_변경(TableResponse createdOrderTable) {
        TableRequest updateRequest = createRequest(4);
        ExtractableResponse<Response> updatedResponse = 고객_수_변경_요청(createdOrderTable.getId(), updateRequest);

        고객_수_변경됨(updatedResponse, updateRequest);
    }

    public static TableRequest createRequest(int numberOfGuests) {
        return new TableRequest(numberOfGuests);
    }

    public static ExtractableResponse<Response> 생성_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static void 생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static void 조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<TableResponse> tableResponses = Arrays.asList(response.as(TableResponse[].class));
        assertThat(tableResponses.size()).isEqualTo(1);
    }

    public static ExtractableResponse<Response> 고객_수_변경_요청(long tableId, TableRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put("/api/tables/{orderTableId}", tableId)
                .then().log().all()
                .extract();
    }

    public static void 고객_수_변경됨(ExtractableResponse<Response> response, TableRequest request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        TableResponse table = response.as(TableResponse.class);
        assertThat(table.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }
}
