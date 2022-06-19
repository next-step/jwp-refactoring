package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.http.HttpStatus;

class TableAcceptanceTestMethod extends AcceptanceTest {

    private static final String TABLE_PATH = "/api/tables";
    private static final String TABLE_EMPTY_PATH_FORMAT = "/api/tables/%s/empty";
    private static final String TABLE_GUEST_PATH_FORMAT = "/api/tables/%s/number-of-guests";
    private static final String DOT = ".";

    public static ExtractableResponse<Response> 테이블_등록_요청(OrderTableRequest params) {
        return post(TABLE_PATH, params);
    }

    public static ExtractableResponse<Response> 테이블_등록되어_있음(OrderTableRequest params) {
        return 테이블_등록_요청(params);
    }

    public static ExtractableResponse<Response> 빈_테이블_변경_요청(Long id, OrderTableRequest params) {
        return put(String.format(TABLE_EMPTY_PATH_FORMAT, id), params);
    }

    public static ExtractableResponse<Response> 테이블_손님_수_변경_요청(Long id, OrderTableRequest params) {
        return put(String.format(TABLE_GUEST_PATH_FORMAT, id), params);
    }

    public static ExtractableResponse<Response> 테이블_목록_조회_요청() {
        return get(TABLE_PATH);
    }

    public static void 테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(parseURIFromLocationHeader(response)).isNotBlank();
    }

    public static void 테이블_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 빈_테이블_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 테이블_손님_수_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 테이블_목록_포함됨(ExtractableResponse<Response> response,
                                    List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedIds = createdResponses.stream()
                .map(AcceptanceTest::parseIdFromLocationHeader)
                .collect(Collectors.toList());

        List<Long> actualIds = response.jsonPath().getList(DOT, OrderTableResponse.class)
                .stream()
                .map(OrderTableResponse::getId)
                .collect(Collectors.toList());

        assertThat(actualIds).containsAll(expectedIds);
    }
}