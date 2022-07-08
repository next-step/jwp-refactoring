package acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static acceptance.RestAssuredMethods.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TableAcceptanceMethods {
    public static ExtractableResponse<Response> 테이블_등록_요청(OrderTableRequest params) {
        return post("/api/tables", params);
    }

    public static ExtractableResponse<Response> 테이블_목록_조회_요청() {
        return get("/api/tables");
    }

    public static ExtractableResponse<Response> 테이블_비어있음_수정_요청(ExtractableResponse<Response> response, OrderTableRequest params) {
        String uri = response.header("Location") + "/empty";
        return put(uri, params);
    }

    public static ExtractableResponse<Response> 테이블_손님수_수정_요청(ExtractableResponse<Response> response, OrderTableRequest params) {
        String uri = response.header("Location") + "/number-of-guests";
        return put(uri, params);
    }

    public static void 테이블_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 테이블_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 테이블_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedOrderTableIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultOrderTableIds = response.jsonPath().getList(".", OrderTableResponse.class).stream()
                .map(OrderTableResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultOrderTableIds).containsAll(expectedOrderTableIds);
    }

    public static void 테이블_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 테이블_등록되어_있음(int numberOfGuests, boolean empty) {
        return 테이블_등록_요청(OrderTableRequest.of(numberOfGuests, empty));
    }
}
