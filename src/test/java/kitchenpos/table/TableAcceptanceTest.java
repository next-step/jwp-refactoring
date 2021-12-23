package kitchenpos.table;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.utils.RestTestApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class TableAcceptanceTest extends AcceptanceTest {

    private static final String URI = "/api/tables";

    private OrderTableRequest 추가_테이블_1번;
    private OrderTableRequest 추가_테이블_2번;

    @BeforeEach
    public void setUp() {
        super.setUp();

        추가_테이블_1번 = new OrderTableRequest(0, true);
        추가_테이블_2번 = new OrderTableRequest(0, true);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createOrderTable() {
        // when
        ExtractableResponse<Response> response = 테이블_생성_요청(추가_테이블_1번);

        // then
        테이블_생성됨(response);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        // given
        ExtractableResponse<Response> 추가_테이블_1번_응답 = 테이블_생성_요청(추가_테이블_1번);
        ExtractableResponse<Response> 추가_테이블_2번_응답 = 테이블_생성_요청(추가_테이블_2번);

        // when
        ExtractableResponse<Response> 테이블_목록_응답 = 테이블_목록_요청();

        // then
        테이블_목록_응답됨(테이블_목록_응답);
        테이블_목록_포함됨(테이블_목록_응답, Arrays.asList(추가_테이블_1번_응답, 추가_테이블_2번_응답));
    }

    @DisplayName("빈 테이블로 변경한다.")
    @Test
    void changeEmpty() {
        // given
        OrderTableRequest 비어있지않은_테이블 = new OrderTableRequest(1, false);
        ExtractableResponse<Response> 비어있지않은_테이블_응답 = 테이블_생성_요청(비어있지않은_테이블);
        테이블_생성됨(비어있지않은_테이블_응답);

        // when
        ExtractableResponse<Response> response = 테이블_비움_요청(비어있지않은_테이블_응답);

        // then
        테이블_상태_변경_응답됨(response);
        테이블_비워짐(response);
    }

    @DisplayName("테이블에 방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTableRequest 손님수를_변경할_테이블 = new OrderTableRequest(1, false);
        ExtractableResponse<Response> 손님수를_변경할_테이블_응답 = 테이블_생성_요청(손님수를_변경할_테이블);
        테이블_생성됨(손님수를_변경할_테이블_응답);

        // when
        OrderTableRequest 변경할_손님수 = new OrderTableRequest(3);
        ExtractableResponse<Response> response = 테이블_손님수_변경_요청(손님수를_변경할_테이블_응답, 변경할_손님수);

        // then
        테이블_손님수_변경_응답됨(response);
        테이블_손님수_변경됨(response, 변경할_손님수.getNumberOfGuests());
    }

    private ExtractableResponse<Response> 테이블_손님수_변경_요청(ExtractableResponse<Response> 손님수를_변경할_테이블_응답,
        OrderTableRequest 변경할_손님수) {
        return RestTestApi.put(
            손님수를_변경할_테이블_응답.header("Location") + "/number-of-guests", 변경할_손님수);
    }

    private void 테이블_손님수_변경_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 테이블_손님수_변경됨(ExtractableResponse<Response> response, int expectedGuests) {
        OrderTableResponse actualResponse = response.as(OrderTableResponse.class);
        assertThat(actualResponse.getNumberOfGuests()).isEqualTo(expectedGuests);
    }

    private ExtractableResponse<Response> 테이블_비움_요청(ExtractableResponse<Response> 비어있지않은_테이블_응답) {
        return RestTestApi.put(
            비어있지않은_테이블_응답.header("Location") + "/empty");
    }

    private void 테이블_상태_변경_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 테이블_비워짐(ExtractableResponse<Response> response) {
        OrderTableResponse actualResponse = response.as(OrderTableResponse.class);
        assertThat(actualResponse.isEmpty()).isTrue();
    }

    private ExtractableResponse<Response> 테이블_목록_요청() {
        return RestTestApi.get(URI);
    }

    private void 테이블_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 테이블_목록_포함됨(ExtractableResponse<Response> response,
        List<ExtractableResponse<Response>> expectedResponses) {

        List<Long> responseIds = response.jsonPath().getList(".", OrderTableResponse.class).stream()
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedIds = expectedResponses.stream()
            .map(expectedResponse -> expectedResponse.as(OrderTableResponse.class))
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());

        assertThat(responseIds).containsAll(expectedIds);
    }

    private ExtractableResponse<Response> 테이블_생성_요청(OrderTableRequest request) {
        return RestTestApi.post(URI, request);
    }

    private void 테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
