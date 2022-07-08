package kitchenpos.ordertable.acceptance;

import acceptance.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ordertable.dto.OrderTableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static acceptance.TableAcceptanceMethods.*;

@DisplayName("테이블 관련 기능 인수테스트")
public class TableAcceptanceTest extends AcceptanceTest {
    private OrderTableRequest 테이블1_orderTableRequest;
    private OrderTableRequest 테이블2_orderTableRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();

        테이블1_orderTableRequest = OrderTableRequest.of(3, false);
        테이블2_orderTableRequest = OrderTableRequest.of(4, false);
    }

    /**
     * Feature: 테이블 관련 기능
     *
     *   Scenario: 테이블을 관리
     *     When 테이블1(not empty) 등록 요청
     *     Then 테이블1 등록됨
     *     When 테이블2(not empty) 등록 요청
     *     Then 테이블2 등록됨
     *     When 메뉴 조회 요청
     *     Then 테이블1, 테이블2 조회됨
     *     When 테이블1 '비어있음' 업데이트 (not empty → empty)
     *     Then 테이블1 업데이트됨
     *     When 테이블1 '비어있음' 업데이트 (empty → not empty)
     *     Then 테이블1 업데이트됨
     *     When 테이블2 손님 수 업데이트 (4명 → 5명)
     *     Then 테이블2 업데이트됨
     */
    @DisplayName("테이블을 관리한다")
    @Test
    void 테이블_관리_정상_시나리오() {
        ExtractableResponse<Response> 테이블1_등록 = 테이블_등록_요청(테이블1_orderTableRequest);
        테이블_등록됨(테이블1_등록);

        ExtractableResponse<Response> 테이블2_등록 = 테이블_등록_요청(테이블2_orderTableRequest);
        테이블_등록됨(테이블2_등록);

        ExtractableResponse<Response> 테이블_목록_조회 = 테이블_목록_조회_요청();
        테이블_목록_응답됨(테이블_목록_조회);
        테이블_목록_포함됨(테이블_목록_조회, Arrays.asList(테이블1_등록, 테이블2_등록));

        OrderTableRequest request_EMPTY = OrderTableRequest.of(0, true);
        ExtractableResponse<Response> 테이블_업데이트_to_EMPTY = 테이블_비어있음_수정_요청(테이블1_등록, request_EMPTY);
        테이블_수정됨(테이블_업데이트_to_EMPTY);

        OrderTableRequest request_NOTEMPTY = OrderTableRequest.of(1, false);
        ExtractableResponse<Response> 테이블_업데이트_to_NOTEMPTY = 테이블_비어있음_수정_요청(테이블1_등록, request_NOTEMPTY);
        테이블_수정됨(테이블_업데이트_to_NOTEMPTY);

        OrderTableRequest request_guest = OrderTableRequest.of(5, false);
        ExtractableResponse<Response> 테이블_업데이트_손님수 = 테이블_손님수_수정_요청(테이블2_등록, request_guest);
        테이블_수정됨(테이블_업데이트_손님수);
    }
}
