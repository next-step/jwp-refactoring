package kitchenpos.table.acceptance;

import static kitchenpos.table.acceptance.TableAcceptanceTestMethod.빈_테이블_변경_요청;
import static kitchenpos.table.acceptance.TableAcceptanceTestMethod.빈_테이블_변경됨;
import static kitchenpos.table.acceptance.TableAcceptanceTestMethod.테이블_등록_요청;
import static kitchenpos.table.acceptance.TableAcceptanceTestMethod.테이블_등록되어_있음;
import static kitchenpos.table.acceptance.TableAcceptanceTestMethod.테이블_등록됨;
import static kitchenpos.table.acceptance.TableAcceptanceTestMethod.테이블_목록_응답됨;
import static kitchenpos.table.acceptance.TableAcceptanceTestMethod.테이블_목록_조회_요청;
import static kitchenpos.table.acceptance.TableAcceptanceTestMethod.테이블_목록_포함됨;
import static kitchenpos.table.acceptance.TableAcceptanceTestMethod.테이블_손님_수_변경_요청;
import static kitchenpos.table.acceptance.TableAcceptanceTestMethod.테이블_손님_수_변경됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.acceptance.code.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("테이블 관련 인수테스트")
class TableAcceptanceTest extends AcceptanceTest {

    private OrderTableRequest orderTableRequest1;
    private OrderTableRequest orderTableRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        orderTableRequest1 = OrderTableRequest.of(0, true);
        orderTableRequest2 = OrderTableRequest.of(0, false);
    }

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create01() {
        // when
        ExtractableResponse<Response> response = 테이블_등록_요청(orderTableRequest1);

        // then
        테이블_등록됨(response);
    }

    @DisplayName("테이블 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        ExtractableResponse<Response> createdResponse1 = 테이블_등록되어_있음(orderTableRequest1);
        ExtractableResponse<Response> createdResponse2 = 테이블_등록되어_있음(orderTableRequest2);

        // when
        ExtractableResponse<Response> response = 테이블_목록_조회_요청();

        // then
        테이블_목록_응답됨(response);
        테이블_목록_포함됨(response, Lists.newArrayList(createdResponse1, createdResponse2));
    }

    @DisplayName("테이블의 상태를 빈 테이블 상태로 변경할 수 있다.")
    @Test
    void change01() {
        // given
        OrderTableResponse createdOrderTable = 테이블_등록되어_있음(orderTableRequest2).as(OrderTableResponse.class);
        OrderTableRequest request = OrderTableRequest.of(createdOrderTable.getNumberOfGuests(), true);

        // when
        ExtractableResponse<Response> response = 빈_테이블_변경_요청(createdOrderTable.getId(), request);

        // then
        빈_테이블_변경됨(response);
    }

    @DisplayName("테이블의 손님 수를 변경할 수 있다.")
    @Test
    void change02() {
        // given
        OrderTableResponse createdOrderTable = 테이블_등록되어_있음(orderTableRequest2).as(OrderTableResponse.class);
        OrderTableRequest request = OrderTableRequest.of(10, createdOrderTable.isEmpty());

        // when
        ExtractableResponse<Response> response = 테이블_손님_수_변경_요청(createdOrderTable.getId(), request);

        // then
        테이블_손님_수_변경됨(response);
    }
}