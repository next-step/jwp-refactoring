package kitchenpos.tablegroup.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.tablegroup.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.menu.acceptance.MenuGroupRestAssured.메뉴그룹_생성_요청;
import static kitchenpos.menu.acceptance.MenuRestAssured.메뉴_생성_요청;
import static kitchenpos.order.acceptance.OrderRestAssured.주문_생성_요청;
import static kitchenpos.tablegroup.acceptance.OrderTableRestAssured.테이블_생성_요청;
import static kitchenpos.tablegroup.acceptance.TableGroupRestAssured.테이블군_삭제_요청;
import static kitchenpos.tablegroup.acceptance.TableGroupRestAssured.테이블군_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 비어있지_않은_주문_테이블1;
    private OrderTableResponse 비어있지_않은_주문_테이블2;
    private OrderTableResponse 빈_주문_테이블1;
    private OrderTableResponse 빈_주문_테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        비어있지_않은_주문_테이블1 = 테이블_생성_요청(2, false).as(OrderTableResponse.class);
        비어있지_않은_주문_테이블2 = 테이블_생성_요청(2, false).as(OrderTableResponse.class);
        빈_주문_테이블1 = 테이블_생성_요청(0, true).as(OrderTableResponse.class);
        빈_주문_테이블2 = 테이블_생성_요청(0, true).as(OrderTableResponse.class);
    }

    /**
     * When 등록되어 있지 않은 주문 테이블을 포함한 테이블 그룹을 등록 요청하면
     * Then 테이블 그룹을 생성할 수 없다.
     */
    @DisplayName("등록되어 있지 않은 주문 테이블을 포함한 테이블 그룹을 등록한다.")
    @Test
    void createTableGroupWithNullOrderTable() {
        // when
        ExtractableResponse<Response> response = 테이블군_생성_요청(Arrays.asList(-1L, -2L));

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * When 비어있는 주문 테이블 목록을 포함한 테이블 그룹을 등록 요청하면
     * Then 테이블 그룹을 생성할 수 없다.
     */
    @DisplayName("비어있는 주문 테이블 목록을 포함한 테이블 그룹을 등록한다.")
    @Test
    void createTableGroupWithEmptyOrderTables() {
        // when
        ExtractableResponse<Response> response = 테이블군_생성_요청(Collections.emptyList());

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 주문 테이블을 생성하고
     * When 2보다 작은 크기의 주문 테이블 목록을 포함한 테이블 그룹을 등록 요청하면
     * Then 테이블 그룹을 생성할 수 없다.
     */
    @DisplayName("2보다 작은 크기의 주문 테이블 목록을 포함한 테이블 그룹을 등록한다.")
    @Test
    void createTableGroupWithOrderTablesLessThenTwo() {
        // when
        ExtractableResponse<Response> response =
                테이블군_생성_요청(Arrays.asList(빈_주문_테이블1.getId()));

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 주문 테이블을 생성하고
     * When 비어있지 않은 주문 테이블을 포함한 테이블 그룹을 등록 요청하면
     * Then 테이블 그룹을 생성할 수 없다.
     */
    @DisplayName("비어있지 않은 주문 테이블을 포함한 테이블 그룹을 등록한다.")
    @Test
    void createTableWithNotEmptyOrderTable() {
        // when
        ExtractableResponse<Response> response =
                테이블군_생성_요청(Arrays.asList(비어있지_않은_주문_테이블1.getId(), 빈_주문_테이블2.getId()));

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 주문 테이블을 생성하고
     * When 테이블 그룹을 등록 요청하면
     * Then 테이블 그룹을 생성할 수 있다.
     */
    @DisplayName("테이블 그룹을 등록한다.")
    @Test
    void createTable() {
        // when
        TableGroupResponse response =
                테이블군_생성_요청(Arrays.asList(빈_주문_테이블1.getId(), 빈_주문_테이블2.getId()))
                        .as(TableGroupResponse.class);

        // then
        assertThat(response.getOrderTables()).hasSize(2);
    }

    /**
     * When 등록되지 않은 테이블 그룹을 해제 요청하면
     * Then 테이블 그룹을 해제할 수 없다.
     */
    @DisplayName("등록되지 않은 테이블 그룹을 해제한다.")
    @Test
    void ungroupWithNullTableGroup() {
        // when
        ExtractableResponse<Response> response = 테이블군_삭제_요청(-1L);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 등록하고
     * And 메뉴를 등록하고
     * And 주문 테이블을 등록하고
     * And 테이블 그룹을 생성하고
     * And 조리중이거나 식사중인 주문을 생성하고
     * When 테이블 그룹을 해제 요청하면
     * Then 테이블 그룹을 해제할 수 없다.
     */
    @DisplayName("조리중이거나 식사중인 주문 테이블을 포함한 테이블 그룹을 해제한다.")
    @Test
    void ungroupWithCookingOrEatingOrder() {
        // given
        MenuGroupResponse 양식 = 메뉴그룹_생성_요청("양식").as(MenuGroupResponse.class);
        MenuResponse 양식_세트 = 메뉴_생성_요청("양식 세트", new BigDecimal(0), 양식.getId(),
                Collections.emptyList()).as(MenuResponse.class);
        TableGroupResponse 테이블그룹 = 테이블군_생성_요청(Arrays.asList(빈_주문_테이블1.getId(), 빈_주문_테이블2.getId()))
                .as(TableGroupResponse.class);
        주문_생성_요청(빈_주문_테이블1.getId(), Arrays.asList(new OrderLineItemRequest(양식_세트.getId(), 1L)))
                .as(OrderResponse.class);
        주문_생성_요청(빈_주문_테이블2.getId(), Arrays.asList(new OrderLineItemRequest(양식_세트.getId(), 1L)))
                .as(OrderResponse.class);

        // when
        ExtractableResponse<Response> response = 테이블군_삭제_요청(테이블그룹.getId());

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 주문 테이블을 생성하고
     * And 테이블 그룹을 생성하고
     * When 테이블 그룹을 해제 요청하면
     * Then 테이블 그룹을 해제할 수 있다.
     */
    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        // given
        TableGroupResponse 테이블그룹 =
                테이블군_생성_요청(Arrays.asList(빈_주문_테이블1.getId(), 빈_주문_테이블2.getId()))
                        .as(TableGroupResponse.class);

        // when
        ExtractableResponse<Response> response = 테이블군_삭제_요청(테이블그룹.getId());

        // then
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }
}
