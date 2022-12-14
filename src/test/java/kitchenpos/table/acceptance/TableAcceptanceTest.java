package kitchenpos.table.acceptance;

import static kitchenpos.table.acceptance.TableAcceptanceTestUtils.*;
import static kitchenpos.table.acceptance.TableGroupAcceptanceTestUtils.단체_테이블_등록되어_있음;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

@DisplayName("주문 테이블 인수 테스트")
class TableAcceptanceTest extends AcceptanceTest {
    private OrderTable 존재하지_않는_테이블;
    private OrderTableResponse 비어있는_테이블;
    private OrderTableResponse 주문테이블;
    private OrderTableResponse 주문테이블2;

    @DisplayName("주문 테이블 인수 테스트")
    @TestFactory
    Stream<DynamicNode> tableAcceptance() {
        return Stream.of(
                dynamicTest("비어있는 테이블을 등록한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 주문_테이블_생성_요청(0, true);

                    // then
                    주문_테이블_생성됨(response);
                    비어있는_테이블 = response.as(OrderTableResponse.class);
                }),
                dynamicTest("주문 테이블을 등록한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 주문_테이블_생성_요청(2, false);

                    // then
                    주문_테이블_생성됨(response);
                    주문테이블 = response.as(OrderTableResponse.class);
                }),
                dynamicTest("주문 테이블을 등록한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 주문_테이블_생성_요청(4, false);

                    // then
                    주문_테이블_생성됨(response);
                    주문테이블2 = response.as(OrderTableResponse.class);
                }),
                dynamicTest("주문 테이블 목록을 조회하면 주문 테이블 목록을 반환한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 주문_테이블_목록_조회_요청();

                    // then
                    주문_테이블_목록_조회됨(response);
                    주문_테이블_목록_포함됨(response, 비어있는_테이블, 주문테이블, 주문테이블2);
                }),
                dynamicTest("주문 테이블 손님 수 수정시 손님은 0명 이상이어야 한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 주문_테이블_손님_수_수정_요청(주문테이블.getId(), -1);

                    // then
                    테이블_손님_수_수정_실패(response);
                }),
                dynamicTest("주문 테이블 손님 수 수정시 주문 테이블로 등록되어 있어야 한다.", () -> {
                    // given
                    존재하지_않는_테이블 = OrderTable.of(Long.MAX_VALUE, null, 0, false);

                    // when
                    ExtractableResponse<Response> response = 주문_테이블_손님_수_수정_요청(존재하지_않는_테이블.id(), 3);

                    // then
                    테이블_손님_수_수정_실패(response);
                }),
                dynamicTest("빈 테이블은 손님 수를 수정할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 주문_테이블_손님_수_수정_요청(비어있는_테이블.getId(), 3);

                    // then
                    테이블_손님_수_수정_실패(response);
                }),
                dynamicTest("주문 테이블의 손님 수를 수정한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 주문_테이블_손님_수_수정_요청(주문테이블.getId(), 3);

                    // then
                    테이블_손님_수_수정됨(response);
                    테이블_손님_수_검증(response, 3);
                }),
                dynamicTest("비어있는 테이블로 수정시 주문 테이블로 등록되어 있어야 한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 주문_테이블_빈_테이블_수정_요청(존재하지_않는_테이블.id());

                    // then
                    비어있는_테이블로_수정_실패(response);
                }),
                dynamicTest("비어있는 테이블로 수정시 주문 테이블은 단체 테이블이 아니어야 한다.", () -> {
                    // given
                    OrderTableResponse 테이블 = 주문_테이블_등록되어_있음(0, true);
                    OrderTableResponse 테이블2 = 주문_테이블_등록되어_있음(0, true);
                    단체_테이블_등록되어_있음(테이블.getId(), 테이블2.getId());
//                    주문_등록되어_있음(테이블, 메뉴_면류_짜장면());

                    // when
                    ExtractableResponse<Response> response = 주문_테이블_빈_테이블_수정_요청(테이블.getId());

                    // then
                    비어있는_테이블로_수정_실패(response);
                }),
                dynamicTest("비어있는 테이블로 수정시 주문 상태가 조리 중 또는 식사 중이면 안된다.", () -> {
                    // given
                    OrderTableResponse 주문이_들어간_테이블 = 주문이_들어간_테이블();

                    // when
                    ExtractableResponse<Response> response = 주문_테이블_빈_테이블_수정_요청(주문이_들어간_테이블.getId());

                    // then
                    비어있는_테이블로_수정_실패(response);
                }),
                dynamicTest("주문 테이블을 비어있는 테이블로 수정한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 주문_테이블_빈_테이블_수정_요청(비어있는_테이블.getId());

                    // then
                    비어있는_테이블로_수정됨(response);
                    비어있는_주문_테이블_검증(response);
                })
        );
    }
}
