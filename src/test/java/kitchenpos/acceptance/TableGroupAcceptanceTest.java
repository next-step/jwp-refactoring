package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTestUtils.메뉴_면류_짜장면;
import static kitchenpos.acceptance.OrderAcceptanceTestUtils.주문_등록되어_있음;
import static kitchenpos.acceptance.TableAcceptanceTestUtils.*;
import static kitchenpos.acceptance.TableGroupAcceptanceTestUtils.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

@DisplayName("단체 테이블 인수 테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {
    private OrderTable 주문이_들어간_테이블;
    private OrderTable 테이블;
    private OrderTable 테이블2;
    private OrderTable 테이블3;
    private OrderTable 테이블4;
    private TableGroup 단체테이블;

    @DisplayName("단체 테이블 인수 테스트")
    @TestFactory
    Stream<DynamicNode> tableGroupAcceptance() {
        return Stream.of(
                dynamicTest("테이블들을 등록한다.", () -> {
                    // when
                    주문이_들어간_테이블 = 주문이_들어간_테이블();
                    테이블 = 주문_테이블_등록되어_있음(null, 0, true);
                    테이블2 = 주문_테이블_등록되어_있음(null, 0, true);
                    테이블3 = 주문_테이블_등록되어_있음(null, 0, true);
                    테이블4 = 주문_테이블_등록되어_있음(null, 0, true);
                }),
                dynamicTest("단체 테이블 등록시 테이블은 비어있을 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 단체_테이블_생성_요청();

                    // then
                    단체_테이블_생성_실패(response);
                }),
                dynamicTest("단체 테이블 등록시 테이블은 2개 이상이어야 한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 단체_테이블_생성_요청(테이블);

                    // then
                    단체_테이블_생성_실패(response);
                }),
                dynamicTest("단체 테이블 등록시 모두 등록된 테이블이어야 한다.", () -> {
                    // given
                    OrderTable 존재하지_않는_테이블 = new OrderTable();

                    // when
                    ExtractableResponse<Response> response = 단체_테이블_생성_요청(존재하지_않는_테이블);

                    // then
                    단체_테이블_생성_실패(response);
                }),
                dynamicTest("단체 테이블 등록시 모두 비어있는 테이블이어야 한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 단체_테이블_생성_요청(주문이_들어간_테이블, 테이블);

                    // then
                    단체_테이블_생성_실패(response);
                }),
                dynamicTest("단체 테이블로 등록한다.", () -> {
                    ExtractableResponse<Response> response = 단체_테이블_생성_요청(테이블, 테이블2);

                    // then
                    단체_테이블_생성됨(response);
                    단체테이블 = response.as(TableGroup.class);
                }),
                dynamicTest("단체 테이블 등록시 이미 단체 테이블인 테이블은 등록 할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 단체_테이블_생성_요청(테이블, 테이블3);

                    // then
                    단체_테이블_생성_실패(response);
                }),
                dynamicTest("단체 테이블 해제시 조리 중이거나 식사중인 테이블은 해제할 수 없다.", () -> {
                    주문_등록되어_있음(테이블, 메뉴_면류_짜장면());

                    // when
                    ExtractableResponse<Response> response = 단체_테이블_해제_요청(단체테이블.getId());

                    // then
                    단체_테이블_해제_실패(response);
                }),
                dynamicTest("단체 테이블을 해제한다.", () -> {
                    TableGroup 단체테이블2 = 단체_테이블_등록되어_있음(테이블3, 테이블4);

                    // when
                    ExtractableResponse<Response> response = 단체_테이블_해제_요청(단체테이블2.getId());

                    // then
                    단체_테이블_해제됨(response);
                })
        );
    }
}
