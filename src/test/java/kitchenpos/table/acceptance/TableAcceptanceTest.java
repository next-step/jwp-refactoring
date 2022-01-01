package kitchenpos.table.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.common.TestApiClient;
import kitchenpos.menu.acceptance.MenuAcceptanceTest;
import kitchenpos.menu.dto.*;
import kitchenpos.order.acceptance.OrderAcceptanceTest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {
    private OrderTableResponse 일번테이블;
    private MenuResponse 소고기세트메뉴;
    private ProductResponse 소고기한우;
    private MenuGroupResponse 추천메뉴;
    private OrderResponse 일번테이블_주문;

    @BeforeEach
    public void setUp() {
        super.setUp();

        추천메뉴 = MenuAcceptanceTest.메뉴그룹_등록되어있음(MenuGroupRequest.of("추천메뉴"));
        소고기한우 = MenuAcceptanceTest.상품_등록되어있음(ProductRequest.of("소고기한우", BigDecimal.valueOf(30000)));
        소고기세트메뉴 = MenuAcceptanceTest.메뉴_등록되어있음(
                "소고기+소고기",
                50000,
                추천메뉴.toEntity(),
                Arrays.asList(
                        MenuProductRequest.of(소고기한우.toEntity(), 2L)
                )
        );
        일번테이블 = 테이블_등록되어_있음(OrderTableCreateRequest.of(4, false));
        일번테이블_주문 = OrderAcceptanceTest.주문_생성됨(일번테이블.getId(), Arrays.asList(OrderLineItemRequest.of(소고기세트메뉴.getId(), 2)));
    }

    @DisplayName("테이블 관리")
    @Test
    void handleTable() {
        // 테이블 생성
        OrderTableResponse 이번테이블 = 테이블_등록되어_있음(OrderTableCreateRequest.of(10, false));
        OrderResponse 이번테이블_주문 = OrderAcceptanceTest.주문_생성됨(이번테이블.getId(), Arrays.asList(OrderLineItemRequest.of(소고기세트메뉴.getId(), 3)));

        // 테이블 조회
        ExtractableResponse<Response> findResponse = 모든_테이블_조회_요청();
        모든_테이블_조회_확인(findResponse, 일번테이블.getId(), 이번테이블.getId());

        // 테이블 공석 설정
        OrderTableChangeEmptyRequest emptyOrderTableRequest = OrderTableChangeEmptyRequest.of(true);
        ExtractableResponse<Response> changeEmptyResponse1 = 테이블_공석_변경_요청(일번테이블_주문.getId(), emptyOrderTableRequest);
        OrderTableResponse savedOrder1 = 테이블_공석_변경_확인(changeEmptyResponse1, emptyOrderTableRequest);
        ExtractableResponse<Response> changeEmptyResponse2 = 테이블_공석_변경_요청(이번테이블_주문.getId(), emptyOrderTableRequest);
        OrderTableResponse savedOrder2 = 테이블_공석_변경_확인(changeEmptyResponse2, emptyOrderTableRequest);

        // 단체 생성
        TableGroupRequest tableGroup = TableGroupRequest.of(Arrays.asList(savedOrder1.getId(), savedOrder2.getId()));
        ExtractableResponse<Response> createGroupResponse = 단체_생성_요청(tableGroup);
        TableGroupResponse savedTableGroup = 단체_생성_확인(createGroupResponse);

        // 단체 해지
        ExtractableResponse<Response> unGroupResponse = 단체_해지_요청(savedTableGroup.getId());
        단체_해지_확인(unGroupResponse);

        // 테이블 손님 수 변경
        int newNumberOfGuest = 20;
        ExtractableResponse<Response> updateNumberOfGuestResponse = 테이블_손님_수_변경_요청(이번테이블.getId(), OrderTableChangeNumberOfGuestsRequest.of(newNumberOfGuest));
        테이블_손님_수_변경_확인(updateNumberOfGuestResponse, newNumberOfGuest);
    }

    private OrderTableResponse 테이블_공석_변경_확인(ExtractableResponse<Response> changeEmptyResponse, OrderTableChangeEmptyRequest expected) {
        OrderTableResponse orderTableResponse = changeEmptyResponse.as(OrderTableResponse.class);
        assertAll(
                () -> assertThat(changeEmptyResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderTableResponse.isEmpty()).isEqualTo(expected.isEmpty())
        );
        return orderTableResponse;
    }

    private ExtractableResponse<Response> 테이블_공석_변경_요청(Long id, OrderTableChangeEmptyRequest emptyOrderTable) {
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
                    OrderTableResponse orderTable = updateNumberOfGuestResponse.as(OrderTableResponse.class);
                    assertThat(orderTable.getNumberOfGuests()).isEqualTo(expected);
                }
        );

    }

    private ExtractableResponse<Response> 테이블_손님_수_변경_요청(Long id, OrderTableChangeNumberOfGuestsRequest request) {
        return TestApiClient.update(request, "/api/tables/" + id + "/number-of-guests");
    }

    private void 단체_해지_확인(ExtractableResponse<Response> unGroupResponse) {
        assertThat(unGroupResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 단체_해지_요청(Long id) {
        return TestApiClient.delete("/api/table-groups/" + id);
    }

    private TableGroupResponse 단체_생성_확인(ExtractableResponse<Response> createGroupResponse) {
        TableGroupResponse tableGroupResponse = createGroupResponse.as(TableGroupResponse.class);
        String location = createGroupResponse.header("Location");
        assertAll(
                () -> assertThat(createGroupResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(location).isEqualTo("/api/table-groups/" + tableGroupResponse.getId()),
                () -> {
                    tableGroupResponse.getOrderTables().forEach(orderTable -> {
                        assertThat(orderTable.getTableGroupId()).isNotNull();
                    });
                }
        );
        return tableGroupResponse;
    }

    private ExtractableResponse<Response> 단체_생성_요청(TableGroupRequest request) {
        return TestApiClient.create(request, "/api/table-groups");
    }

    private void 모든_테이블_조회_확인(ExtractableResponse<Response> findResponse, Long... tables) {
        assertAll(
                () -> assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> {
                    List<Long> list = findResponse.jsonPath().getList(".", OrderTableResponse.class)
                            .stream()
                            .map(orderTableResponse -> orderTableResponse.getId())
                            .collect(Collectors.toList());
                    assertThat(list).containsAll(Arrays.asList(tables));
                }
        );
    }

    private ExtractableResponse<Response> 모든_테이블_조회_요청() {
        return TestApiClient.get("/api/tables");
    }

    public static OrderTableResponse 테이블_등록되어_있음(OrderTableCreateRequest orderTable) {
        return TestApiClient.create(orderTable, "/api/tables").as(OrderTableResponse.class);
    }
}
