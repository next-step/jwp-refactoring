package kitchenpos.table.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_등록_요청;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록_요청;
import static kitchenpos.order.acceptance.OrderAcceptanceTest.주문_등록_요청;
import static kitchenpos.order.acceptance.OrderAcceptanceTest.주문_상태_변경_요청;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록_요청;
import static kitchenpos.table.acceptance.TableAcceptanceTest.테이블_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@DisplayName("테이블 그룹 관련 기능 인수테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    private OrderTable 빈_테이블;
    private OrderTable 다른_빈_테이블;
    private OrderTable 손님_4명_테이블;
    private OrderTable 손님_3명_테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();

        빈_테이블 = 테이블_등록_요청(0, true).getBody();
        다른_빈_테이블 = 테이블_등록_요청(0, true).getBody();
        손님_4명_테이블 = 테이블_등록_요청(4, false).getBody();
        손님_3명_테이블 = 테이블_등록_요청(3, false).getBody();
    }
    /**
     * Feature 테이블 그룹 관련 기능
     *
     * Backgroud
     * Given 테이블 등록되어 있음
     *
     * Screnario 테이블 그룹 관련 기능
     * When 손님 테이블 그룹 등록 요청
     * Then 테이블 그룹 등록 실패됨
     * When 빈 테이블 그룹 등록 요청
     * Then 테이블 그룹 등록됨
     * 
     * Given 주문(요리, 식사) 있음
     * When 테이블 그룹 해제 요청
     * Then 테이블 그룹 해제 실패됨
     * 
     * Given 주문(계산 완료) 있음
     * When 테이블 그룹 해제 요청
     * Then 테이블 그룹 해제됨
     */
    @Test
    @DisplayName("테이블 그룹 관련 기능")
    void integrationTest() {
        //when
        ResponseEntity<TableGroup> 손님_테이블_그룹_등록_요청_결과 = 테이블_그룹_등록_요청(손님_4명_테이블, 손님_3명_테이블);
        //then
        테이블_그룹_등록_실패됨(손님_테이블_그룹_등록_요청_결과);

        //when
        ResponseEntity<TableGroup> 빈_테이블_그룹_등록_요청_결과 = 테이블_그룹_등록_요청(빈_테이블, 다른_빈_테이블);
        TableGroup 테이블_그룹 = 빈_테이블_그룹_등록_요청_결과.getBody();
        //then
        테이블_그룹_등록됨(빈_테이블_그룹_등록_요청_결과);

        //given
        MenuGroup 추천메뉴 = 메뉴_그룹_등록_요청("추천메뉴").getBody();
        Product 허니콤보 = 상품_등록_요청("허니콤보", 20_000L).getBody();
        Product 레드콤보 = 상품_등록_요청("레드콤보", 19_000L).getBody();
        Menu 허니레드콤보 = 메뉴_등록_요청(추천메뉴, "허니레드콤보", 39_000L, 허니콤보, 레드콤보).getBody();
        Order 주문 = 주문_등록_요청(빈_테이블, 허니레드콤보).getBody();

        //when
        ResponseEntity<Void> 주문_있는_테이블_그룹_해제_요청_결과 = 테이블_그룹_해제_요청(테이블_그룹);
        //then
        테이블_그룹_해제_실패됨(주문_있는_테이블_그룹_해제_요청_결과);

        //given
        주문_상태_변경_요청(주문, OrderStatus.COMPLETION.name());
        //when
        ResponseEntity<Void> 테이블_그룹_해제_요청_결과 = 테이블_그룹_해제_요청(테이블_그룹);
        //then
        테이블_그룹_해제됨(테이블_그룹_해제_요청_결과);
    }

    private void 테이블_그룹_해제됨(ResponseEntity<Void> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private void 테이블_그룹_해제_실패됨(ResponseEntity<Void> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Void> 테이블_그룹_해제_요청(TableGroup tableGroup) {
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

    private void 테이블_그룹_등록됨(ResponseEntity<TableGroup> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().get("Location")).isNotNull();
    }

    private ResponseEntity<TableGroup> 테이블_그룹_등록_요청(OrderTable... orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(주문_테이블_변환(orderTables));

        return testRestTemplate.postForEntity("/api/table-groups/", tableGroup, TableGroup.class);
    }

    private List<OrderTable> 주문_테이블_변환(OrderTable[] orderTables) {
        return Arrays.stream(orderTables)
                .collect(Collectors.toList());
    }
}
