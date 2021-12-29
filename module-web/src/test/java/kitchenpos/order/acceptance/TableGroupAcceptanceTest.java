package kitchenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.acceptance.RestAssuredApi;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.dto.TableRequest;
import kitchenpos.order.dto.TableResponse;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.order.acceptance.TableAcceptanceTest.*;
import static kitchenpos.order.dto.OrderStatusRequest.completion;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 그룹 인수 테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {

    private TableResponse 테이블1;
    private TableResponse 테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        테이블1 = 주문_테이블_등록_요청(TableRequest.from(2)).as(TableResponse.class);
        테이블2 = 주문_테이블_등록_요청(TableRequest.from(4)).as(TableResponse.class);
    }

    @Test
    @DisplayName("테이블 그룹 정상 시나리오")
    void normalScenario() {
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(테이블1.getId(), 테이블2.getId()));
        ExtractableResponse<Response> response = 테이블_그룹_등록_요청(request);
        String createdLocationUri = 테이블_그룹_등록됨(response);

        TableGroupResponse 테이블그룹 = response.as(TableGroupResponse.class);
        테이블_그룹_ID_일치됨(주문_테이블_목록_조회_요청(), Arrays.asList(테이블그룹.getId(), 테이블그룹.getId()));

        주문_상태_변경_요청("/api/tables/" + 테이블1.getId(), completion());
        주문_상태_변경_요청("/api/tables/" + 테이블2.getId(), completion());

        테이블_그룹_해제됨(테이블_그룹_해제_요청(createdLocationUri));
        테이블_그룹_ID_일치됨(주문_테이블_목록_조회_요청(), Arrays.asList(null, null));
    }

    @Test
    @DisplayName("테이블 그룹 예외 시나리오")
    void exceptionScenario() {
        TableGroupRequest request1 = new TableGroupRequest(Collections.emptyList());
        TableGroupRequest request2 = new TableGroupRequest(Collections.singletonList(테이블1.getId()));

        테이블_그룹_등록_실패됨(테이블_그룹_등록_요청(request1));
        테이블_그룹_등록_실패됨(테이블_그룹_등록_요청(request2));
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

    private void 테이블_그룹_ID_일치됨(ExtractableResponse<Response> response, List<Long> excepted) {
        assertThat(response.jsonPath().getList("tableGroupId", Long.class))
                .isEqualTo(excepted);
    }

    public static void 테이블_그룹_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 테이블_그룹_등록_실패됨(ExtractableResponse<Response> response) {
        AssertionsForClassTypes.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
