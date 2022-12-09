package kitchenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.acceptance.MenuAcceptance;
import kitchenpos.menu.acceptance.MenuGroupAcceptance;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("주문 테이블 관련 기능 인수 테스트")
public class TableAcceptanceTest extends AcceptanceTest {

    /**
     * When 방문한 손님 수를 음수로 하여 주문 테이블 생성을 요청하면
     * Then 주문 테이블을 생성할 수 없다.
     */
    @DisplayName("방문한 손님 수를 음수로 하여 주문 테이블을 생성한다.")
    @Test
    void createTableWithNegativeNumberOfGuests() {
        // when
        ExtractableResponse<Response> response = TableAcceptance.create_table(-5, true);

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
        ExtractableResponse<Response> response = TableAcceptance.create_table(0, true);

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
        TableAcceptance.create_table(0, true);

        // when
        ExtractableResponse<Response> response = TableAcceptance.table_list_has_been_queried();

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
        ExtractableResponse<Response> response = TableAcceptance.change_empty(-1L, true);

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
        MenuGroupResponse 양식 = MenuGroupAcceptance.create_menu_group("양식").as(MenuGroupResponse.class);
        MenuResponse 양식_세트 = MenuAcceptance.create_menu("양식 세트", new BigDecimal(0), 양식.getId(),
                Collections.emptyList()).as(MenuResponse.class);
        OrderTableResponse 주문테이블 = TableAcceptance.create_table(2, false).as(OrderTableResponse.class);
        OrderAcceptance.create_order(주문테이블.getId(), Arrays.asList(new OrderLineItemRequest(양식_세트.getId(), 1L)))
                .as(OrderResponse.class);

        // when
        ExtractableResponse<Response> response = TableAcceptance.change_empty(주문테이블.getId(), true);

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
        OrderTableResponse 주문테이블 = TableAcceptance.create_table(2, false)
                .as(OrderTableResponse.class);

        // when
        OrderTableResponse changed = TableAcceptance.change_empty(주문테이블.getId(), true)
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
        ExtractableResponse<Response> response = TableAcceptance.change_number_of_guests(-1L, 3);

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
        OrderTableResponse 주문테이블 = TableAcceptance.create_table(0, true)
                .as(OrderTableResponse.class);

        // when
        ExtractableResponse<Response> response = TableAcceptance.change_number_of_guests(주문테이블.getId(), 2);

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
        OrderTableResponse 주문테이블 = TableAcceptance.create_table(0, false)
                .as(OrderTableResponse.class);

        // when
        OrderTableResponse changed = TableAcceptance.change_number_of_guests(주문테이블.getId(), 2)
                .as(OrderTableResponse.class);

        // then
        assertEquals(2, changed.getNumberOfGuests());
    }
}
