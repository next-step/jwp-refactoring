package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("단체지정")
class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블1 = 주문가능한_테이블을_존재(new OrderTableRequest(3, true));
        주문테이블2 = 주문가능한_테이블을_존재(new OrderTableRequest(4, true));
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
    @DisplayName("단체지정석 관리")
    @Test
    void tableGroupManage() {
        //given
        TableGroupRequest 단체지정테이블 = new TableGroupRequest(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()));

        //when
        final ExtractableResponse<Response> response = 단체지정테이블_요청(단체지정테이블);

        //then
        단체지정테이블로_변경됨(response);



        TableGroupResponse 단체지정된_테이블 = response.as(TableGroupResponse.class);
        //then
        단체지정된_테이블이_조회됨(단체지정된_테이블, 주문테이블을_조회());

//        //when
//        final ExtractableResponse<Response> deleteResponse = 단체지정_테이블_제거(단체지정테이블);
//        //then
//        단체_테이블_제거가됨(deleteResponse);
//
//        //when
//        List<OrderTable> orderTables = 주문테이블을_조회();
//        //then
//        단체지정된_테이블이_조회되지_않음(단체지정테이블, orderTables);
    }



    private void 단체지정된_테이블이_조회됨(TableGroupResponse tableGroup, List<OrderTable> 주문테이블을_조회) {
        final List<OrderTable> collect = 주문테이블을_조회.stream()
                .filter((it) -> tableGroup.getId().equals(it.getTableGroupId()))
                .collect(Collectors.toList());

        assertThat(collect).hasSize(tableGroup.getOrderTables().size());
    }

    private void 단체지정된_테이블이_조회되지_않음(TableGroup tableGroup, List<OrderTable> 주문테이블을_조회) {
        final List<OrderTable> collect = 주문테이블을_조회.stream()
                .filter((it) -> tableGroup.getId().equals(it.getTableGroupId()))
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

    private ExtractableResponse<Response> 단체지정_테이블_제거(TableGroup tableGroup) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("tableGroupId", tableGroup.getId())
                .when().delete("/api/table-groups/{tableGroupId}")
                .then()
                .log().all()
                .extract();
    }


    private OrderTable 주문가능한_테이블을_존재(OrderTableRequest orderTable) {
        return TableAcceptanceTest.주문가능한_테이블을_요청한다(orderTable).as(OrderTable.class);
    }

    private List<OrderTable> 주문테이블을_조회() {
      return TableAcceptanceTest.주문_테이블을_조회한다().jsonPath().getList(".", OrderTable.class);
    }

    private void 단체_테이블_제거가됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
