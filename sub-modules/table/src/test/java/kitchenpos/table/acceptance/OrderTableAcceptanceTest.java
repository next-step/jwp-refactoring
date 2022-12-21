package kitchenpos.table.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.TableAcceptanceConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.table.fixture.OrderTableTestFixture.*;

@DisplayName("주문테이블 관련 기능 인수 테스트")
public class OrderTableAcceptanceTest extends TableAcceptanceConfig {
    /**
     * When : 주문 테이블을 생성 요청하면
     * Then : 주문 테이블이 생성된다
     */
    @DisplayName("주문 테이블 생성 인수 테스트")
    @Test
    void createOrderTable() {
        // when
        ExtractableResponse<Response> response = 주문테이블_생성_요청(0, true);

        // then
        주문테이블_생성됨(response);
    }

    /**
     * Given : 주문 테이블이 생성되어 있다.
     * When : 주문 테이블의 목록 조회를 요청하면
     * Then : 주문 테이블의 목록을 응답한다
     */
    @DisplayName("주문 테이블 조회 인수 테스트")
    @Test
    void findOrderTables() {
        // given
        주문테이블_생성_요청(0, true);

        // when
        ExtractableResponse<Response> response = 주문테이블_조회_요청();

        // then
        주문테이블_조회됨(response);
    }

    /**
     * Given : 주문 테이블이 생성되어 있음
     * When : 주문 테이블의 상태를 빈 테이블에서 비어있지 않음으로 변경하면
     * Then : 주문 테이블의 상태가 변경된다.
     */
    @DisplayName("빈 테이블의 상태를 변경하는 인수 테스트 (true -> false)")
    @Test
    void changeEmptyTable() {
        // given
        Long orderTableId = 주문테이블_생성_요청(0, true).jsonPath().getLong("id");

        // when
        boolean empty = false;
        ExtractableResponse<Response> response = 주문테이블_빈테이블_여부_수정_요청(orderTableId, empty);

        // then
        주문테이블_수정됨(response);
        주문테이블_empty_확인됨(response, empty);
    }

    /**
     * Given : 주문 테이블이 생성되어 있음
     * When : 주문 테이블의 손님의 수를 변경 요청한다
     * Then : 주문 테이블의 손님의 수가 변경된다.
     */
    @DisplayName("주문 테이블의 손님의 수를 변경 요청하는 인수 테스트")
    @Test
    void changeNumberOfGuests() {
        // given
        Long orderTableId = 주문테이블_생성_요청(0, false).jsonPath().getLong("id");

        // when
        int numberOfGuests = 4;
        ExtractableResponse<Response> response = 주문테이블_손님수_수정_요청(orderTableId, numberOfGuests);

        // then
        주문테이블_수정됨(response);
        주문테이블_손님수_확인됨(response, numberOfGuests);
    }

    /**
     * Given : 주문 테이블이 빈 테이블로 생성되어 있음
     * When : 주문 테이블의 손님의 수를 변경 요청한다.
     * Then : 주문 테이블의 손님의 수가 변경되지 않는다.
     */
    @DisplayName("주문 테이블이 빈 테이블인 경우 손님의 수를 변경할 때 예외 발생")
    @Test
    void changeNumberOfGuestsExceptionBy_빈테이블() {
        // given
        Long orderTableId = 주문테이블_생성_요청(0, true).jsonPath().getLong("id");

        // when
        int numberOfGuests = 4;
        ExtractableResponse<Response> response = 주문테이블_손님수_수정_요청(orderTableId, numberOfGuests);

        // then
        주문테이블_수정_실패함(response);
    }

    /**
     * Given : 주문 테이블이 생성되어 있음
     * When : 주문 테이블의 손님의 수를 음수로 변경 요청한다.
     * Then : 주문 테이블의 손님의 수가 변경되지 않는다.
     */
    @DisplayName("주문 테이블의 손님의 수를 음수로 변경 요청하는 경우 예외 발생")
    @Test
    void changeNumberOfGuestsExceptionBy_음수의_손님의수() {
        // given
        Long orderTableId = 주문테이블_생성_요청(0, false).jsonPath().getLong("id");

        // when
        int numberOfGuests = -1;
        ExtractableResponse<Response> response = 주문테이블_손님수_수정_요청(orderTableId, numberOfGuests);

        // then
        주문테이블_수정_실패함(response);
    }
}
