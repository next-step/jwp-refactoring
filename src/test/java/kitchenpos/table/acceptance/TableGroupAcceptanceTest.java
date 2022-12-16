package kitchenpos.table.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTestUtils.메뉴_면류_짜장면;
import static kitchenpos.order.acceptance.OrderAcceptanceTestUtils.주문_등록되어_있음;
import static kitchenpos.order.domain.OrderLineItemTestFixture.orderLineItemRequest;
import static kitchenpos.table.acceptance.TableAcceptanceTestUtils.*;
import static kitchenpos.table.acceptance.TableGroupAcceptanceTestUtils.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

@DisplayName("단체 테이블 인수 테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {
    private OrderTableResponse 주문이_들어간_테이블;
    private OrderTableResponse 테이블;
    private OrderTableResponse 테이블2;
    private OrderTableResponse 테이블3;
    private OrderTableResponse 테이블4;
    private TableGroupResponse 단체테이블;

    @DisplayName("단체 테이블 인수 테스트")
    @TestFactory
    Stream<DynamicNode> tableGroupAcceptance() {
        return Stream.of(
                dynamicTest("테이블들을 등록한다.", () -> {
                    // when
                    주문이_들어간_테이블 = 주문이_들어간_테이블();
                    테이블 = 주문_테이블_등록되어_있음(0, true);
                    테이블2 = 주문_테이블_등록되어_있음(0, true);
                    테이블3 = 주문_테이블_등록되어_있음(0, true);
                    테이블4 = 주문_테이블_등록되어_있음(0, true);
                }),
                dynamicTest("단체 테이블 등록시 테이블은 비어있을 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 단체_테이블_생성_요청(Collections.emptyList());

                    // then
                    단체_테이블_생성_실패(response);
                }),
                dynamicTest("단체 테이블 등록시 테이블은 2개 이상이어야 한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 단체_테이블_생성_요청(Collections.singletonList(테이블.getId()));

                    // then
                    단체_테이블_생성_실패(response);
                }),
                dynamicTest("단체 테이블 등록시 모두 등록된 테이블이어야 한다.", () -> {
                    // given
                    OrderTable 존재하지_않는_테이블 = OrderTable.of(Long.MAX_VALUE, null, 0, true);

                    // when
                    ExtractableResponse<Response> response = 단체_테이블_생성_요청(Collections.singletonList(존재하지_않는_테이블.id()));

                    // then
                    단체_테이블_생성_실패(response);
                }),
                dynamicTest("단체 테이블 등록시 모두 비어있는 테이블이어야 한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 단체_테이블_생성_요청(Arrays.asList(주문이_들어간_테이블.getId(), 테이블.getId()));

                    // then
                    단체_테이블_생성_실패(response);
                }),
                dynamicTest("단체 테이블로 등록한다.", () -> {
                    ExtractableResponse<Response> response = 단체_테이블_생성_요청(Arrays.asList(테이블.getId(), 테이블2.getId()));

                    // then
                    단체_테이블_생성됨(response);
                    단체테이블 = response.as(TableGroupResponse.class);
                }),
                dynamicTest("단체 테이블 등록시 이미 단체 테이블인 테이블은 등록 할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 단체_테이블_생성_요청(Arrays.asList(테이블.getId(), 테이블3.getId()));

                    // then
                    단체_테이블_생성_실패(response);
                }),
                dynamicTest("단체 테이블 해제시 조리 중이거나 식사중인 테이블은 해제할 수 없다.", () -> {
                    주문_등록되어_있음(테이블.getId(), Collections.singletonList(orderLineItemRequest(메뉴_면류_짜장면().getId(), 1L)));

                    // when
                    ExtractableResponse<Response> response = 단체_테이블_해제_요청(단체테이블.getId());

                    // then
                    단체_테이블_해제_실패(response);
                }),
                dynamicTest("단체 테이블을 해제한다.", () -> {
                    TableGroupResponse 단체테이블2 = 단체_테이블_등록되어_있음(테이블3.getId(), 테이블4.getId());

                    // when
                    ExtractableResponse<Response> response = 단체_테이블_해제_요청(단체테이블2.getId());

                    // then
                    단체_테이블_해제됨(response);
                })
        );
    }
}
