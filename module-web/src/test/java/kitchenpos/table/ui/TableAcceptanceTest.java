package kitchenpos.table.ui;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.common.AcceptanceFixture.*;
import static kitchenpos.fixture.OrderTableAcceptanceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 테이블 관련(인수 테스트)")
class TableAcceptanceTest extends AcceptanceTest {
    private OrderTableRequest 주문_테이블_첫번째;
    private OrderTableResponse 주문_테이블_첫번째_등록됨;
    private OrderTableResponse 주문_테이블_두번째_등록됨;

    @DisplayName("주문 테이블 조회")
    @Test
    void 주문_테이블_조회() {
        주문_테이블_첫번째_등록됨 = 주문_테이블_생성_요청(OrderTableRequest.of(2, false)).as(OrderTableResponse.class);
        주문_테이블_두번째_등록됨 = 주문_테이블_생성_요청(OrderTableRequest.of(5, false)).as(OrderTableResponse.class);
        ExtractableResponse<Response> 주문_테이블_조회_요청됨 = 주문_테이블_조회_요청();
        OK_응답_잘_받았음(주문_테이블_조회_요청됨);
        주문_테이블_조회_확인(주문_테이블_조회_요청됨, 주문_테이블_첫번째_등록됨, 주문_테이블_두번째_등록됨);
    }


    @DisplayName("주문 테이블 생성 하기")
    @Test
    void createTest() {
        주문_테이블_첫번째 = OrderTableRequest.of(2, false);
        ExtractableResponse<Response> 주문_테이블_생성_요청됨 = 주문_테이블_생성_요청(주문_테이블_첫번째);
        CREATE_응답_잘_받음(주문_테이블_생성_요청됨);
        주문_테이블_등록_확인(주문_테이블_생성_요청됨, 주문_테이블_첫번째);
    }

    @DisplayName("방문한 손님 수 변경 하기")
    @Test
    void changeTest() {
        OrderTableResponse 테이블_6인_요청_생성됨 = 주문_테이블_생성_요청(테이블_6인_요청).as(OrderTableResponse.class);
        OrderTableRequest 변경_요청 = OrderTableRequest.of(2, false);
        ExtractableResponse<Response> 주문_테이블_방문한_손님_수_변경_요청됨 = 주문_테이블_방문한_손님_수_변경_요청(변경_요청, 테이블_6인_요청_생성됨.getId());
        OK_응답_잘_받았음(주문_테이블_방문한_손님_수_변경_요청됨);
        주문_테이블_방문_손님_변경_확인(주문_테이블_방문한_손님_수_변경_요청됨, 변경_요청);
    }

    @DisplayName("주문 테이블 없는 방문한 손님 수 변경하면 실패함")
    @Test
    void failTest1() {
        주문_테이블_생성_요청(테이블_6인_요청).as(OrderTableResponse.class);
        OrderTableRequest 변경_요청 = OrderTableRequest.of(2, false);
        BAD_REQUEST_응답_잘_받았음(주문_테이블_방문한_손님_수_변경_요청(변경_요청, 100));
    }

    @DisplayName("방문한 손님 수가 음수이면 실패함")
    @Test
    void failTest2() {
        OrderTableResponse 테이블_6인_요청_생성됨 = 주문_테이블_생성_요청(테이블_6인_요청).as(OrderTableResponse.class);
        OrderTableRequest 변경_요청 = OrderTableRequest.of(-1, false);
        ExtractableResponse<Response> actual = 주문_테이블_방문한_손님_수_변경_요청(변경_요청, 테이블_6인_요청_생성됨.getId());
        BAD_REQUEST_응답_잘_받았음(actual);
    }

    private void 주문_테이블_등록_확인(ExtractableResponse<Response> actual, OrderTableRequest orderTableRequest) {
        OrderTableResponse response = actual.as(OrderTableResponse.class);
        assertThat(response).isNotNull();
    }

    private void 주문_테이블_방문_손님_변경_확인(ExtractableResponse<Response> actual, OrderTableRequest orderTableRequest) {
        OrderTableResponse response = actual.as(OrderTableResponse.class);
        assertThat(response.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
    }

    private void 주문_테이블_조회_확인(ExtractableResponse<Response> actual, OrderTableResponse... expected) {
        List<Long> expectedIds = Arrays.stream(expected)
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());

        List<Long> response = actual.jsonPath()
            .getList(".", OrderTableResponse.class)
            .stream()
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());

        assertThat(response).containsAll(expectedIds);
    }
}
