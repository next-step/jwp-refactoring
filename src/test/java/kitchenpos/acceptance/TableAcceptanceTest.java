package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.RestAssuredFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블 관련 기능")
class TableAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/tables";

    @DisplayName("주문 테이블을 관리한다.")
    @Test
    void manageTable() {
        // given
        OrderTableRequest 비어있는_주문_테이블 = OrderTableRequest.of(0, true);

        // when
        ExtractableResponse<Response> 주문_테이블_생성_응답 = 주문_테이블_생성_요청(비어있는_주문_테이블);
        // then
        주문_테이블_생성됨(주문_테이블_생성_응답);

        // when
        ExtractableResponse<Response> 주문_테이블_목록_조회_응답 = 주문_테이블_목록_조회_요청();
        // then
        주문_테이블_목록_조회됨(주문_테이블_목록_조회_응답);

        // given
        boolean empty = false;
        // when
        Long 주문_테이블_ID = 주문_테이블_ID_조회(주문_테이블_생성_응답);
        ExtractableResponse<Response> 주문_테이블_수정_응답 = 주문_테이블_수정_요청(주문_테이블_ID, empty);
        // then
        주문_테이블_수정됨(주문_테이블_수정_응답, empty);

        // given
        int numberOfGuests = 4;
        // when
        ExtractableResponse<Response> 주문_테이블_손님_수_수정_응답 = 주문_테이블_손님_수_수정_요청(주문_테이블_ID, numberOfGuests);
        // then
        주문_테이블_손님_수_수정됨(주문_테이블_손님_수_수정_응답, numberOfGuests);
    }

    private static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTableRequest params) {
        return 생성_요청(API_URL, params);
    }

    private void 주문_테이블_생성됨(ExtractableResponse<Response> response) {
        생성됨_201_CREATED(response);
    }

    private ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return 목록_조회_요청(API_URL);
    }

    private void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response) {
        성공_200_OK(response);
    }

    private Long 주문_테이블_ID_조회(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> 주문_테이블_수정_요청(Long orderTableId, boolean empty) {
        String path = String.format("%s/%s/empty", API_URL, orderTableId);
        ChangeEmptyRequest params = ChangeEmptyRequest.of(empty);

        return 수정_요청(path, params);
    }

    private void 주문_테이블_수정됨(ExtractableResponse<Response> response, boolean expect) {
        assertAll(
                () -> 성공_200_OK(response)
                , () -> assertThat(response.jsonPath().getBoolean("empty")).isEqualTo(expect)
        );
    }

    private ExtractableResponse<Response> 주문_테이블_손님_수_수정_요청(Long orderTableId, int numberOfGuests) {
        String path = String.format("%s/%s/number-of-guests", API_URL, orderTableId);
        ChangeNumberOfGuestsRequest params = ChangeNumberOfGuestsRequest.of(numberOfGuests);

        return 수정_요청(path, params);
    }

    private void 주문_테이블_손님_수_수정됨(ExtractableResponse<Response> response, int expect) {
        assertAll(
                () -> 성공_200_OK(response)
                , () -> assertThat(response.jsonPath().getInt("numberOfGuests")).isEqualTo(expect)
        );
    }

    public static OrderTable 주문_테이블_등록되어_있음(int numberOfGuests, boolean empty) {
        OrderTableRequest params = OrderTableRequest.of(numberOfGuests, empty);

        ExtractableResponse<Response> response = 주문_테이블_생성_요청(params);
        return response.as(OrderTable.class);
    }
}
