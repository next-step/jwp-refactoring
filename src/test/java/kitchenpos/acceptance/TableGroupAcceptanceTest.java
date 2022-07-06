package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("단체지정")
class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 주문테이블1;
    private OrderTableResponse 주문테이블2;
    private ExtractableResponse<Response> 생성된_단체지정석;
    private TableGroupResponse 단체지정된_테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블1 = 주문가능한_테이블을_존재(new OrderTableRequest(3, false));
        주문테이블2 = 주문가능한_테이블을_존재(new OrderTableRequest(4, false));
    }

    /*
        background
            given  주문가능한_테이블을_존재 (2건)
        Scenario 단체지정석 관리
            given 주문테이블 2건 단체지정테이블 정보 생성
            when 단체지정테이블_요청
            then 단체지정테이블로 변경됨
            when 주문테이블 조회
            then 주문테이블이 단체 테이블로 지정됨
            when 단체테이블 제거
            then 단체테이블 제거가 됨
            when 주문테이블 조회
            then 제거가된 단체 테이블 조회안됨;
    */
    @TestFactory
    @DisplayName("단체지정석 관리")
    Stream<DynamicTest> tableGroupManage() {
        return Stream.of(
                dynamicTest("단체지정석으로 변경요청", () -> {
                    //given
                    TableGroupRequest 단체지정테이블 = new TableGroupRequest(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()));

                    //when
                    생성된_단체지정석 = 단체지정테이블_요청(단체지정테이블);

                    //then
                    단체지정테이블로_변경됨(생성된_단체지정석);
                }),
                dynamicTest("단체지정석을 조회", () -> {
                    //when
                    단체지정된_테이블 = 생성된_단체지정석.as(TableGroupResponse.class);
                    //then
                    단체지정된_테이블이_조회됨(단체지정된_테이블, 주문테이블을_조회());

                }),
                dynamicTest("단체지정석 제거", () -> {
                    //when
                    final ExtractableResponse<Response> deleteResponse = 단체지정_테이블_제거(단체지정된_테이블.getId());
                    //then
                    단체_테이블_제거가됨(deleteResponse);
                }),
                dynamicTest("제거한 단체지정석 조회", () -> {
                    //when
                    List<OrderTableResponse> orderTables = 주문테이블을_조회();
                    //then
                    단체지정된_테이블이_조회되지_않음(단체지정된_테이블, orderTables);
                })
        );
    }



    private void 단체지정된_테이블이_조회됨(TableGroupResponse tableGroup, List<OrderTableResponse> 주문테이블을_조회) {
        final List<OrderTableResponse> collect = 주문테이블을_조회.stream()
                .filter((orderTableResponse) -> tableGroup.getId().equals(orderTableResponse.getTableGroupId()))
                .collect(Collectors.toList());

        assertThat(collect).hasSize(tableGroup.getOrderTables().size());
    }

    private void 단체지정된_테이블이_조회되지_않음(TableGroupResponse tableGroup, List<OrderTableResponse> 주문테이블을_조회) {
        final List<OrderTableResponse> collect = 주문테이블을_조회.stream()
                .filter((orderTableResponse) -> tableGroup.getId().equals(orderTableResponse.getTableGroupId()))
                .collect(Collectors.toList());

        assertThat(collect).hasSize(0);
    }

    private void 단체지정테이블로_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotEmpty();
    }

    private ExtractableResponse<Response> 단체지정테이블_요청(TableGroupRequest tableGroupRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroupRequest)
                .when().post("/api/table-groups")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 단체지정_테이블_제거(Long tableGroupId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("tableGroupId", tableGroupId)
                .when().delete("/api/table-groups/{tableGroupId}")
                .then()
                .log().all()
                .extract();
    }


    private OrderTableResponse 주문가능한_테이블을_존재(OrderTableRequest orderTable) {
        return TableAcceptanceTest.주문가능한_테이블을_요청한다(orderTable).as(OrderTableResponse.class);
    }

    private List<OrderTableResponse> 주문테이블을_조회() {
      return TableAcceptanceTest.주문_테이블을_조회한다().jsonPath().getList(".", OrderTableResponse.class);
    }

    private void 단체_테이블_제거가됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
