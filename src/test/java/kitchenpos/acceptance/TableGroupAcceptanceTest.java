package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.dto.OrderTableRequest;
import kitchenpos.domain.dto.OrderTableResponse;
import kitchenpos.domain.dto.TableGroupRequest;
import kitchenpos.util.RestAssuredApi;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.acceptance.OrderTableAcceptanceTest.주문_테이블_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 그룹 인수 테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 테이블1;
    private OrderTableResponse 테이블2;
    private OrderTableResponse 주문테이블;

    @BeforeEach
    void setUp() {
        super.setUp();

        테이블1 = 주문_테이블_등록_요청(OrderTableRequest.group(2)).as(OrderTableResponse.class);
        테이블2 = 주문_테이블_등록_요청(OrderTableRequest.group(4)).as(OrderTableResponse.class);
        주문테이블 = 주문_테이블_등록_요청(OrderTableRequest.of(2)).as(OrderTableResponse.class);
    }

    @Test
    @DisplayName("테이블 그룹 정상 시나리오")
    void normalScenario() {
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(테이블1.getId(), 테이블2.getId()));
        ExtractableResponse<Response> 테이블_그룹 = 테이블_그룹_등록_요청(request);
        String createdLocationUri = 테이블_그룹_등록됨(테이블_그룹);
        테이블_그룹_일치됨(테이블_그룹, Arrays.asList(2, 4));

        테이블_그룹_해제됨(테이블_그룹_해제_요청(createdLocationUri));
    }

    @Test
    @DisplayName("테이블 그룹 예외 시나리오")
    void exceptionScenario() {
        TableGroupRequest request1 = new TableGroupRequest(Collections.emptyList());
        TableGroupRequest request2 = new TableGroupRequest(Collections.singletonList(주문테이블.getId()));

        테이블_그룹_등록_실패됨(테이블_그룹_등록_요청(request1));
        테이블_그룹_등록_실패됨(테이블_그룹_등록_요청(request2));

        // TODO 테이블 그룹 해제
    }

    public static ExtractableResponse<Response> 테이블_그룹_등록_요청(TableGroupRequest request) {
        return RestAssuredApi.post("/api/table-groups", request);
    }

    public static ExtractableResponse<Response> 테이블_그룹_해제_요청(String uri) {
        return RestAssuredApi.delete(uri);
    }

    public static String 테이블_그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.header("Location");
    }

    private void 테이블_그룹_일치됨(ExtractableResponse<Response> response, List<Integer> excepted) {
        assertThat(response.jsonPath().getList("orderTableResponses.numberOfGuests", Integer.class))
                .isEqualTo(excepted);
    }

    public static void 테이블_그룹_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 테이블_그룹_등록_실패됨(ExtractableResponse<Response> response) {
        AssertionsForClassTypes.assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 테이블_그룹_해제_실패됨(ExtractableResponse<Response> response) {
        AssertionsForClassTypes.assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
