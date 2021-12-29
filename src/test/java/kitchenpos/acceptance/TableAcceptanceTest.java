package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.TestApiClient;
import kitchenpos.domain.*;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.dto.MenuProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {
    private OrderTable 일번테이블;
    private Menu 소고기세트메뉴;
    private Product 소고기한우;
    private MenuGroupResponse 추천메뉴;
    private Order 일번테이블_주문;

    @BeforeEach
    public void setUp() {
        super.setUp();

        추천메뉴 = MenuAcceptanceTest.메뉴그룹_등록되어있음(MenuGroupRequest.of("추천메뉴"));
        소고기한우 = MenuAcceptanceTest.상품_등록되어있음(Product.of("소고기한우", 30000));
        소고기세트메뉴 = MenuAcceptanceTest.메뉴_등록되어있음("소고기+소고기", 50000, 추천메뉴.toEntity(), Arrays.asList(MenuProductRequest.of(소고기한우, 2L)));
        일번테이블 = 테이블_등록되어_있음(OrderTable.of(4, false));
        일번테이블_주문 = OrderAcceptanceTest.주문_생성됨(일번테이블, Arrays.asList(OrderLineItem.of(소고기세트메뉴, 2)));
    }

    @DisplayName("테이블 관리")
    @Test
    void handleTable() {
        // 테이블 생성
        OrderTable 이번테이블 = 테이블_등록되어_있음(OrderTable.of(10, false));
        Order 이번테이블_주문 = OrderAcceptanceTest.주문_생성됨(이번테이블, Arrays.asList(OrderLineItem.of(소고기세트메뉴, 3)));

        // 테이블 조회
        ExtractableResponse<Response> findResponse = 모든_테이블_조회_요청();
        모든_테이블_조회_확인(findResponse, 일번테이블, 이번테이블);

        // 테이블 공석 설정
        OrderTable emptyOrderTable = OrderTable.of(true);
        ExtractableResponse<Response> changeEmptyResponse1 = 테이블_공석_변경_요청(일번테이블_주문.getId(), emptyOrderTable);
        OrderTable savedOrder1 = 테이블_공석_변경_확인(changeEmptyResponse1, emptyOrderTable);
        ExtractableResponse<Response> changeEmptyResponse2 = 테이블_공석_변경_요청(이번테이블_주문.getId(), emptyOrderTable);
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
        ExtractableResponse<Response> updateNumberOfGuestResponse = 테이블_손님_수_변경_요청(이번테이블.getId(), OrderTable.of(newNumberOfGuest));
        테이블_손님_수_변경_확인(updateNumberOfGuestResponse, newNumberOfGuest);
    }

    private OrderTable 테이블_공석_변경_확인(ExtractableResponse<Response> changeEmptyResponse, OrderTable expected) {
        OrderTable orderTable = changeEmptyResponse.as(OrderTable.class);
        assertAll(
                () -> assertThat(changeEmptyResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderTable.isEmpty()).isEqualTo(expected.isEmpty())
        );
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

        assertAll(
                () -> assertThat(updateNumberOfGuestResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> {
                    OrderTable orderTable = updateNumberOfGuestResponse.as(OrderTable.class);
                    assertThat(orderTable.getNumberOfGuests()).isEqualTo(expected);
                }
        );

    }

    private ExtractableResponse<Response> 테이블_손님_수_변경_요청(Long id, OrderTable orderTable) {
        return TestApiClient.update(orderTable, "/api/tables/" + id + "/number-of-guests");
    }

    private void 단체_해지_확인(ExtractableResponse<Response> unGroupResponse) {
        assertThat(unGroupResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 단체_해지_요청(Long id) {
        return TestApiClient.delete("/api/table-groups/" + id);
    }

    private TableGroup 단체_생성_확인(ExtractableResponse<Response> createGroupResponse) {
        TableGroup tableGroup = createGroupResponse.as(TableGroup.class);
        String location = createGroupResponse.header("Location");
        assertAll(
                () -> assertThat(createGroupResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(location).isEqualTo("/api/table-groups/" + tableGroup.getId()),
                () -> {
                    tableGroup.getOrderTables().forEach(orderTable -> {
                        assertThat(orderTable.getTableGroup()).isNotNull();
                    });
                }
        );
        return tableGroup;
    }

    private ExtractableResponse<Response> 단체_생성_요청(TableGroup tableGroup) {
        return TestApiClient.create(tableGroup, "/api/table-groups");
    }

    private void 모든_테이블_조회_확인(ExtractableResponse<Response> findResponse, OrderTable... tables) {
        assertAll(
                () -> assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> {
                    List<OrderTable> list = findResponse.jsonPath().getList(".", OrderTable.class);
                    assertThat(list).containsAll(Arrays.asList(tables));
                }
        );
    }

    private ExtractableResponse<Response> 모든_테이블_조회_요청() {
        return TestApiClient.get("/api/tables");
    }

    public static OrderTable 테이블_등록되어_있음(OrderTable orderTable) {
        return TestApiClient.create(orderTable, "/api/tables").as(OrderTable.class);
    }
}
