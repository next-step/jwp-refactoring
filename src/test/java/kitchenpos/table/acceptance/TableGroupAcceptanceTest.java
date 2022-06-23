package kitchenpos.table.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_등록_되어있음;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음;
import static kitchenpos.order.acceptance.OrderAcceptanceTest.주문_등록_되어있음;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록_되어있음;
import static kitchenpos.table.acceptance.TableAcceptanceTest.테이블_등록_되어있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DisplayName("단체 지정 관련 기능 인수테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    private OrderTable 빈_테이블;
    private OrderTable 다른_빈_테이블;
    private TableGroup 테이블_그룹;
    private OrderTable 손님_4명_테이블;
    private OrderTable 손님_3명_테이블;

    @TestFactory
    @DisplayName("단체 지정 관련 기능 정상 시나리오")
    Stream<DynamicTest> successTest() {
        return Stream.of(
                dynamicTest("단체 지정 등록 요청하면 단체 지정된다.", () -> {
                    빈_테이블 = 테이블_등록_되어있음(0, true);
                    다른_빈_테이블 = 테이블_등록_되어있음(0, true);

                    ResponseEntity<TableGroup> 빈_테이블_그룹_등록_요청_결과 = 단체_지정_등록_요청(빈_테이블, 다른_빈_테이블);

                    단체_지정_등록됨(빈_테이블_그룹_등록_요청_결과);
                }),
                dynamicTest("단체 지정 해제 요청하면 단체 지정해제된다.", () -> {
                    빈_테이블 = 테이블_등록_되어있음(0, true);
                    다른_빈_테이블 = 테이블_등록_되어있음(0, true);
                    테이블_그룹 = 단체_지정_되어있음(빈_테이블, 다른_빈_테이블);

                    ResponseEntity<Void> 테이블_그룹_해제_요청_결과 = 단체_지정_해제_요청(테이블_그룹);
                    //then
                    단체_지정_해제됨(테이블_그룹_해제_요청_결과);
                })
        );
    }

    @TestFactory
    @DisplayName("단체 지정 관련 기능 예외 시나리오")
    Stream<DynamicTest> failTest() {
        return Stream.of(
                dynamicTest("손님 테이블들을 단체 지정 등록 요청하면 단체 지정에 실패한다.", () -> {
                    손님_4명_테이블 = 테이블_등록_되어있음(4, false);
                    손님_3명_테이블 = 테이블_등록_되어있음(3, false);

                    ResponseEntity<TableGroup> 손님_테이블_단체_지정_등록_요청_결과 = 단체_지정_등록_요청(손님_4명_테이블, 손님_3명_테이블);

                    테이블_그룹_등록_실패됨(손님_테이블_단체_지정_등록_요청_결과);
                }),
                dynamicTest("주문이 조리, 식사 중일경우, 단체 지정 해제 요청하면 단체 지정 해제에 실패한다.", () -> {
                    MenuGroup 추천메뉴 = 메뉴_그룹_등록되어_있음("추천메뉴");
                    Product 허니콤보 = 상품_등록_되어있음("허니콤보", 20_000L);
                    Product 레드콤보 = 상품_등록_되어있음("레드콤보", 19_000L);
                    Menu 허니레드콤보 = 메뉴_등록_되어있음(추천메뉴, "허니레드콤보", 39_000L, 허니콤보, 레드콤보);
                    빈_테이블 = 테이블_등록_되어있음(0, true);
                    다른_빈_테이블 = 테이블_등록_되어있음(0, true);
                    테이블_그룹 = 단체_지정_되어있음(빈_테이블, 다른_빈_테이블);
                    Order 주문 = 주문_등록_되어있음(빈_테이블, 허니레드콤보);

                    ResponseEntity<Void> 주문_있는_테이블_단체_해제_요청_결과 = 단체_지정_해제_요청(테이블_그룹);

                    테이블_그룹_해제_실패됨(주문_있는_테이블_단체_해제_요청_결과);
                })
        );
    }

    private void 단체_지정_해제됨(ResponseEntity<Void> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private void 테이블_그룹_해제_실패됨(ResponseEntity<Void> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Void> 단체_지정_해제_요청(TableGroup tableGroup) {
        Map<String, Object> params = new HashMap<>();
        params.put("tableGroupId", tableGroup.getId());

        return testRestTemplate.exchange("/api/table-groups/{tableGroupId}",
                HttpMethod.DELETE,
                null,
                Void.class,
                params);
    }

    private void 테이블_그룹_등록_실패됨(ResponseEntity<TableGroup> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private TableGroup 단체_지정_되어있음(OrderTable... orderTables) {
        return 단체_지정_등록_요청(orderTables).getBody();
    }

    private void 단체_지정_등록됨(ResponseEntity<TableGroup> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().get("Location")).isNotNull();
    }

    private ResponseEntity<TableGroup> 단체_지정_등록_요청(OrderTable... orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(주문_테이블_변환(orderTables));

        return testRestTemplate.postForEntity("/api/table-groups/", tableGroup, TableGroup.class);
    }

    private List<OrderTable> 주문_테이블_변환(OrderTable[] orderTables) {
        return Arrays.stream(orderTables)
                .collect(Collectors.toList());
    }
}
