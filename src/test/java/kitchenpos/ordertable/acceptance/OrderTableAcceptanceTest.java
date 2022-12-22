package kitchenpos.ordertable.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.tablegroup.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.menu.acceptance.MenuRestAssured.메뉴_생성_요청;
import static kitchenpos.menugroup.acceptance.MenuGroupRestAssured.메뉴그룹_생성_요청;
import static kitchenpos.order.acceptance.OrderRestAssured.주문_생성_요청;
import static kitchenpos.ordertable.acceptance.OrderTableRestAssured.테이블_empty_수정_요청;
import static kitchenpos.ordertable.acceptance.OrderTableRestAssured.테이블_생성_요청;
import static kitchenpos.ordertable.acceptance.OrderTableRestAssured.테이블_손님수_수정_요청;
import static kitchenpos.ordertable.acceptance.OrderTableRestAssured.테이블_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderTableAcceptanceTest extends AcceptanceTest {
    /**
     * When 방문한 손님 수를 음수로 하여 주문 테이블 생성을 요청하면
     * Then 주문 테이블을 생성할 수 없다.
     */
    @DisplayName("방문한 손님 수를 음수로 하여 주문 테이블을 생성한다.")
    @Test
    void createTableWithNegativeNumberOfGuests() {
        // when
        ExtractableResponse<Response> response = 테이블_생성_요청(-5, true);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * When 주문 테이블 생성을 요청하면
     * Then 주문 테이블을 생성할 수 있다.
     */
    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createTable() {
        // when
        ExtractableResponse<Response> response = 테이블_생성_요청(0, true);

        // then
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    /**
     * When 주문 테이블 목록을 조회 요청하면
     * Then 주문 테이블 목록을 조회할 있다.
     */
    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        // given
        테이블_생성_요청(0, true);

        // when
        ExtractableResponse<Response> response = 테이블_조회_요청();

        // then
        assertThat(response.jsonPath().getList(".", OrderTableResponse.class)).hasSize(1);
    }

    /**
     * When 등록되지 않은 주문 테이블을 빈 주문 테이블로 변경 요청하면
     * Then 빈 주문 테이블로 변경할 수 없다.
     */
    @DisplayName("등록되지 않은 주문 테이블을 빈 주문 테이블로 변경한다.")
    @Test
    void changeEmptyWithNullOrderTable() {
        // when
        ExtractableResponse<Response> response = 테이블_empty_수정_요청(-1L, true);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 등록하고
     * And 메뉴를 등록하고
     * And 주문 테이블을 등록하고
     * And 조리중이거나 식사중인 주문을 등록하고
     * When 빈 주문 테이블로 변경 요청하면
     * Then 빈 주문 테이블로 변경할 수 없다.
     */
    @DisplayName("조리중이거나 식사중인 주문 테이블을 빈 주문 테이블로 변경한다.")
    @Test
    void changeEmptyWithCookingOrEatingOrder() {
        // given
        MenuGroupResponse 양식 = 메뉴그룹_생성_요청("양식").as(MenuGroupResponse.class);
        MenuResponse 양식_세트 = 메뉴_생성_요청("양식 세트", new BigDecimal(0), 양식.getId(),
                Collections.emptyList()).as(MenuResponse.class);
        OrderTableResponse 주문테이블 = 테이블_생성_요청(2, false).as(OrderTableResponse.class);
        주문_생성_요청(주문테이블.getId(), Arrays.asList(new OrderLineItemRequest(양식_세트.getId(), 1L)))
                .as(OrderResponse.class);

        // when
        ExtractableResponse<Response> response = 테이블_empty_수정_요청(주문테이블.getId(), true);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 주문 테이블을 등록하고
     * When 빈 주문 테이블로 변경 요청하면
     * Then 빈 주문 테이블로 변경할 수 있다.
     */
    @DisplayName("주문 테이블을 빈 주문 테이블로 변경한다.")
    @Test
    void changeEmpty() {
        // given
        OrderTableResponse 주문테이블 = 테이블_생성_요청(2, false)
                .as(OrderTableResponse.class);

        // when
        OrderTableResponse changed = 테이블_empty_수정_요청(주문테이블.getId(), true)
                .as(OrderTableResponse.class);

        // then
        assertTrue(changed.isEmpty());
    }

    /**
     * When 등록되지 않은 주문 테이블을 방문자 손님 수를 변경 요청하면
     * Then 방문자 손님 수를 변경할 수 없다.
     */
    @DisplayName("등록되지 않은 주문 테이블의 방문자 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuestsWithNullOrderTable() {
        // when
        ExtractableResponse<Response> response = 테이블_손님수_수정_요청(-1L, 3);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 빈 주문 테이블을 등록하고
     * When 방문자 손님 수를 변경 요청하면
     * Then 방문자 손님 수를 변경할 수 없다.
     */
    @DisplayName("빈 주문 테이블의 방문자 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuestsWithEmptyTable() {
        // given
        OrderTableResponse 주문테이블 = 테이블_생성_요청(0, true)
                .as(OrderTableResponse.class);

        // when
        ExtractableResponse<Response> response = 테이블_손님수_수정_요청(주문테이블.getId(), 2);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 비어있지 않은 주문 테이블을 등록하고
     * When 방문자 손님 수를 변경 요청하면
     * Then 방문자 손님 수를 변경할 수 있다.
     */
    @DisplayName("비어있지 않은 주문 테이블의 방문자 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTableResponse 주문테이블 = 테이블_생성_요청(0, false)
                .as(OrderTableResponse.class);

        // when
        OrderTableResponse changed = 테이블_손님수_수정_요청(주문테이블.getId(), 2)
                .as(OrderTableResponse.class);

        // then
        assertEquals(2, changed.getNumberOfGuests());
    }

}
