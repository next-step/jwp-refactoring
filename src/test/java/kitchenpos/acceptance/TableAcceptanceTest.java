package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {
    private OrderTable 테이블1;
    private Menu 메뉴;
    private Product 소고기한우;
    private MenuGroup 추천메뉴;
    private Order 주문1;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // 주문 생성
        추천메뉴 = MenuAcceptanceTest.메뉴그룹_등록되어있음(MenuGroup.of("추천메뉴"));
        소고기한우 = MenuAcceptanceTest.상품_등록되어있음(Product.of("소고기한우", 30000));
        메뉴 = MenuAcceptanceTest.메뉴_등록되어있음("소고기+소고기",50000,추천메뉴.getId(),Arrays.asList(MenuProduct.of(소고기한우.getId(), 2L)));
        테이블1 = 테이블_등록되어_있음(OrderTable.of(4, false));
        주문1 = OrderAcceptanceTest.주문_생성됨(테이블1.getId(), Arrays.asList(OrderLineItem.of(메뉴.getId(), 2)));
    }

    @DisplayName("테이블 관리")
    @Test
    void handleTable() {
        // 테이블 생성
        OrderTable 테이블2 = 테이블_등록되어_있음(OrderTable.of(10, false));
        Order 주문2 = OrderAcceptanceTest.주문_생성됨(테이블2.getId(), Arrays.asList(OrderLineItem.of(메뉴.getId(), 3)));

        // 테이블 조회
        ExtractableResponse<Response> findResponse =  모든_테이블_조회_요청();
        모든_테이블_조회_확인(findResponse, 테이블1, 테이블2);

        // 테이블 공석 설정
        OrderTable emptyOrderTable = OrderTable.of(true);
        ExtractableResponse<Response> changeEmptyResponse1 = 테이블_공석_변경_요청(주문1.getId(), emptyOrderTable);
        OrderTable savedOrder1 = 테이블_공석_변경_확인(changeEmptyResponse1, emptyOrderTable);
        ExtractableResponse<Response> changeEmptyResponse2 = 테이블_공석_변경_요청(주문2.getId(), emptyOrderTable);
        OrderTable savedOrder2 = 테이블_공석_변경_확인(changeEmptyResponse2, emptyOrderTable);

        // 단체 생성
        TableGroup tableGroup = TableGroup.of(Arrays.asList(savedOrder1, savedOrder2));
        ExtractableResponse<Response> createGroupResponse = 단체_생성_요청(tableGroup);
        TableGroup savedTableGroup = 단체_생성_확인(createGroupResponse);

        // 단체 해지
        ExtractableResponse<Response> unGroupResponse = 단체_해지_요청(savedTableGroup.getId());
        단체_해지_확인(unGroupResponse);

        // 테이블 손님 수 변경
        int newNumberOfGuest = 20;
        ExtractableResponse<Response> updateNumberOfGuestResponse = 테이블_손님_수_변경_요청(테이블2.getId(), OrderTable.of(newNumberOfGuest));
        테이블_손님_수_변경_확인(updateNumberOfGuestResponse, newNumberOfGuest);

        // 주문 상태 COMPLETE 변경
        Order updateOrder = Order.of(OrderStatus.COMPLETION.name());
        ExtractableResponse<Response> updateOrderStatusResponse =
                OrderAcceptanceTest.주문_상태_변경_요청(주문2.getId(), Order.of(OrderStatus.COMPLETION.name()));
        OrderAcceptanceTest.주문_상태_변경_확인(updateOrderStatusResponse, updateOrder);
    }

    private OrderTable 테이블_공석_변경_확인(ExtractableResponse<Response> changeEmptyResponse, OrderTable expected) {
        assertThat(changeEmptyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        OrderTable orderTable = changeEmptyResponse.as(OrderTable.class);
        assertThat(orderTable.isEmpty()).isEqualTo(expected.isEmpty());
        return orderTable;
    }

    private ExtractableResponse<Response> 테이블_공석_변경_요청(Long id, OrderTable emptyOrderTable) {
        return RestAssured
                .given().log().all()
                .body(emptyOrderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/" + id + "/empty")
                .then().log().all()
                .extract();
    }

    private void 테이블_손님_수_변경_확인(ExtractableResponse<Response> updateNumberOfGuestResponse, int expected) {
        assertThat(updateNumberOfGuestResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        OrderTable orderTable = updateNumberOfGuestResponse.as(OrderTable.class);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expected);
    }

    private ExtractableResponse<Response> 테이블_손님_수_변경_요청(Long id, OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/"+ id + "/number-of-guests")
                .then().log().all()
                .extract();
    }

    private void 단체_해지_확인(ExtractableResponse<Response> unGroupResponse) {
        assertThat(unGroupResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 단체_해지_요청(Long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/api/table-groups/" + id)
                .then().log().all()
                .extract();
    }

    private TableGroup 단체_생성_확인(ExtractableResponse<Response> createGroupResponse) {
        assertThat(createGroupResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String location = createGroupResponse.header("Location");
        TableGroup tableGroup = createGroupResponse.as(TableGroup.class);
        assertThat(location).isEqualTo("/api/table-groups/" + tableGroup.getId());
        tableGroup.getOrderTables().forEach( orderTable -> {
            assertThat(orderTable.getTableGroupId()).isNotNull();
        });
        return tableGroup;
    }

    private ExtractableResponse<Response> 단체_생성_요청(TableGroup tableGroup) {
        return RestAssured
                .given().log().all()
                .body(tableGroup)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    private void 모든_테이블_조회_확인(ExtractableResponse<Response> findResponse, OrderTable... tables) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<OrderTable> list = findResponse.jsonPath().getList(".", OrderTable.class);
        assertThat(list).containsAll(Arrays.asList(tables));
    }

    private ExtractableResponse<Response> 모든_테이블_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static OrderTable 테이블_등록되어_있음(OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract()
                .as(OrderTable.class);
    }
}
