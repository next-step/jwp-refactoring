package kitchenpos;

import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

import static kitchenpos.MenuAcceptanceUtil.신메뉴_강정치킨_가져오기;
import static kitchenpos.OrderAcceptanceTestUtil.주문_등록됨;
import static kitchenpos.TableAcceptanceUtil.주문이_들어간_테이블_가져오기;
import static kitchenpos.TableAcceptanceUtil.테이블_등록됨;
import static kitchenpos.TableGroupAcceptanceTestUtil.단체_지정_생성_실패됨;
import static kitchenpos.TableGroupAcceptanceTestUtil.단체_지정_생성_요청;
import static kitchenpos.TableGroupAcceptanceTestUtil.단체_지정_생성됨;
import static kitchenpos.TableGroupAcceptanceTestUtil.단체_지정_해지_실패됨;
import static kitchenpos.TableGroupAcceptanceTestUtil.단체_지정_해지_요청;
import static kitchenpos.TableGroupAcceptanceTestUtil.단체_지정_해지됨;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("단체 지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 주문이_들어간_테이블;
    private OrderTableResponse 테이블1;
    private OrderTableResponse 테이블2;
    private OrderTableResponse 테이블3;
    private OrderTableResponse 테이블4;

    private TableGroupResponse 단체_지정1;
    private TableGroupResponse 단체_지정2;

    @DisplayName("단체 지정 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> tableGroup() {
        return Stream.of(
                dynamicTest("기초 데이터를 추가한다.", () -> {
                    주문이_들어간_테이블 = 주문이_들어간_테이블_가져오기();
                    테이블1 = 테이블_등록됨(true, 0);
                    테이블2 = 테이블_등록됨(true, 0);
                    테이블3 = 테이블_등록됨(true, 0);
                    테이블4 = 테이블_등록됨(true, 0);
                }),
                dynamicTest("단체 지정을 등록한다.", () -> {
                    ResponseEntity<TableGroupResponse> response = 단체_지정_생성_요청(테이블1, 테이블2);

                    단체_지정_생성됨(response);
                    단체_지정1 = response.getBody();
                }),
                dynamicTest("주문이 들어간 테이블이 포함된 단체 지정을 등록한다.", () -> {
                    ResponseEntity<TableGroupResponse> response = 단체_지정_생성_요청(주문이_들어간_테이블, 테이블3);

                    단체_지정_생성_실패됨(response);
                }),
                dynamicTest("1개의 테이블로 단체 지정을 등록한다.", () -> {
                    ResponseEntity<TableGroupResponse> response = 단체_지정_생성_요청(테이블4);

                    단체_지정_생성_실패됨(response);
                }),
                dynamicTest("존재하지 않는 테이블이 포함된 단체 지정을 등록한다.", () -> {
                    ResponseEntity<TableGroupResponse> response = 단체_지정_생성_요청(테이블4.getId(), Long.MAX_VALUE);

                    단체_지정_생성_실패됨(response);
                }),
                dynamicTest("다른 단체 지정에 포함된 테이블이 포함된 단체 지정을 등록한다.", () -> {
                    ResponseEntity<TableGroupResponse> response = 단체_지정_생성_요청(테이블4, 테이블1);

                    단체_지정_생성_실패됨(response);
                }),
                dynamicTest("단체 지정을 등록한다.", () -> {
                    ResponseEntity<TableGroupResponse> response = 단체_지정_생성_요청(테이블3, 테이블4);

                    단체_지정_생성됨(response);
                    단체_지정2 = response.getBody();
                }),
                dynamicTest("단체 지정을 해지한다.", () -> {
                    ResponseEntity<Void> response = 단체_지정_해지_요청(단체_지정1);

                    단체_지정_해지됨(response);
                }),
                dynamicTest("주문이 들어간 테이블이 포함된 단체 지정은 단체 지정을 해지할 수 없다.", () -> {
                    주문_등록됨(테이블3, 신메뉴_강정치킨_가져오기());

                    ResponseEntity<Void> response = 단체_지정_해지_요청(단체_지정2);

                    단체_지정_해지_실패됨(response);
                })
        );
    }
}
