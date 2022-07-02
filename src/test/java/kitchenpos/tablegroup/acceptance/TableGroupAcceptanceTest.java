package kitchenpos.tablegroup.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.*;
import static kitchenpos.order.acceptance.OrderAcceptanceTest.주문_등록되어_있음;
import static kitchenpos.order.acceptance.OrderAcceptanceTest.주문_상태_변경_요청;
import static kitchenpos.table.acceptance.TableAcceptanceTest.주문_테이블_가져옴;
import static kitchenpos.table.acceptance.TableAcceptanceTest.주문_테이블_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 그룹 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void createTableGroup() {
        // when
        ExtractableResponse<Response> 등록된_테이블_그룹 = 테이블_그룹_등록되어_있음(테이블_그룹_생성());

        // then
        테이블_그룹_생성_검증됨(등록된_테이블_그룹);
    }

    @DisplayName("[예외] 주문 테이블 하나만으로 테이블 그룹 생성한다.")
    @Test
    void createTableGroup_with_one_order_table() {
        // when
        ExtractableResponse<Response> 등록된_테이블_그룹 = 테이블_그룹_등록되어_있음(주문_테이블_하나로_테이블_그룹_생성());

        // then
        테이블_그룹_생성_실패됨(등록된_테이블_그룹);
    }

    @DisplayName("[예외] 저장 안된 주문 테이블을 포함하여 테이블 그룹 생성한다.")
    @Test
    void createTableGroup_with_not_saved_order_table() {
        // when
        ExtractableResponse<Response> 등록된_테이블_그룹 = 테이블_그룹_등록되어_있음(저장안한_주문_테이블_포함하여_테이블_그룹_생성());

        // then
        테이블_그룹_생성_실패됨(등록된_테이블_그룹);
    }

    @DisplayName("[예외] 이용 중인 주문 테이블을 포함하여 테이블 그룹 생성한다.")
    @Test
    void createTableGroup_with_not_empty_order_table() {
        // when
        ExtractableResponse<Response> 등록된_테이블_그룹 = 테이블_그룹_등록되어_있음(사용중인_주문_테이블로_테이블_그룹_생성());

        // then
        테이블_그룹_생성_실패됨(등록된_테이블_그룹);
    }

    @DisplayName("[예외] 이미 다른 테이블 그룹과 연결된 주문 테이블로 테이블 그룹 생성한다.")
    @Test
    void createTableGroup_with_order_table_already_mapping_with_other_table_group() {
        // when
        ExtractableResponse<Response> 등록된_테이블_그룹 = 테이블_그룹_등록되어_있음(이미_다른_테이블_그룹에_포함된_주문_테이블로_테이블_그룹_생성());

        // then
        테이블_그룹_생성_실패됨(등록된_테이블_그룹);
    }

    @DisplayName("식사 완료된 테이블 그룹과 주문 정보 간에 관계를 해지한다.")
    @Test
    void ungroup() {
        // when
        TableGroup 등록된_테이블_그룹 = 식사_완료된_테이블_그룹_생성();

        // then
        테이블_그룹_해지_요청(등록된_테이블_그룹.getId());
    }

    @DisplayName("[예외] 식사 중인 테이블 그룹과 주문 정보 간에 관계를 해지한다.")
    @Test
    void ungroup_not_completion_order() {
        // when
        TableGroup 등록된_테이블_그룹 = 식사_중인_테이블_그룹_생성();

        // then
        테이블_그룹_해지_요청(등록된_테이블_그룹.getId());
    }

    public static TableGroupRequest 테이블_그룹_생성() {
        OrderTableRequest 등록된_주문_테이블1 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, true));
        OrderTableRequest 등록된_주문_테이블2 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(5, true));
        return new TableGroupRequest(Arrays.asList(등록된_주문_테이블1, 등록된_주문_테이블2));
    }

    public static TableGroupRequest 주문_테이블_하나로_테이블_그룹_생성() {
        OrderTableRequest 등록된_주문_테이블 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, true));
        return new TableGroupRequest(Collections.singletonList(등록된_주문_테이블));
    }

    public static TableGroupRequest 저장안한_주문_테이블_포함하여_테이블_그룹_생성() {
        OrderTableRequest 등록된_주문_테이블1 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, true));
        OrderTableRequest 등록된_주문_테이블2 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(5, true));
        OrderTableRequest 저장안한_주문_테이블 = new OrderTableRequest();
        return new TableGroupRequest(Arrays.asList(등록된_주문_테이블1, 등록된_주문_테이블2, 저장안한_주문_테이블));
    }

    public static TableGroupRequest 사용중인_주문_테이블로_테이블_그룹_생성() {
        OrderTableRequest 등록된_주문_테이블1 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, false));
        OrderTableRequest 등록된_주문_테이블2 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(5, false));
        return new TableGroupRequest(Arrays.asList(등록된_주문_테이블1, 등록된_주문_테이블2));
    }

    public static TableGroupRequest 이미_다른_테이블_그룹에_포함된_주문_테이블로_테이블_그룹_생성() {
        OrderTableRequest 등록된_주문_테이블1 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, true));
        OrderTableRequest 등록된_주문_테이블2 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(5, true));
        테이블_그룹_등록되어_있음(new TableGroupRequest(Arrays.asList(등록된_주문_테이블1, 등록된_주문_테이블2)));
        return new TableGroupRequest(Arrays.asList(등록된_주문_테이블1, 등록된_주문_테이블2));
    }

    public static TableGroup 식사_완료된_테이블_그룹_생성() {
        OrderTableRequest 등록된_주문_테이블1 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, true));
        OrderTableRequest 등록된_주문_테이블2 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, true));
        TableGroupRequest tableGroup = new TableGroupRequest(Arrays.asList(등록된_주문_테이블1, 등록된_주문_테이블2));
        TableGroupResponse tableGroupResponse = 테이블_그룹_가져옴(테이블_그룹_등록되어_있음(tableGroup));

        MenuResponse 등록된_메뉴 = 메뉴_가져옴(메뉴_등록되어_있음(테스트_메뉴_생성(MENU_NAME01, MENU_PRICE01)));
        OrderLineItem 생성된_주문_항목 = new OrderLineItem(new Menu(등록된_메뉴.getId()), 1);

        ExtractableResponse<Response> 등록된_주문1 = 주문_등록되어_있음(new OrderRequest(등록된_주문_테이블1.getId(), Arrays.asList(생성된_주문_항목)));
        ExtractableResponse<Response> 등록된_주문2 = 주문_등록되어_있음(new OrderRequest(등록된_주문_테이블2.getId(), Arrays.asList(생성된_주문_항목)));

        주문_상태_변경_요청(등록된_주문1, OrderStatus.COMPLETION);
        주문_상태_변경_요청(등록된_주문2, OrderStatus.COMPLETION);

        return new TableGroup(tableGroupResponse.getId());
    }

    public static TableGroup 식사_중인_테이블_그룹_생성() {
        OrderTableRequest 등록된_주문_테이블1 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, true));
        OrderTableRequest 등록된_주문_테이블2 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, true));

        TableGroupRequest request = new TableGroupRequest(Arrays.asList(등록된_주문_테이블1, 등록된_주문_테이블2));
        request = 테이블_그룹_등록되어_있음(request).as(TableGroupRequest.class);

        MenuResponse 등록된_메뉴 = 메뉴_가져옴(메뉴_등록되어_있음(테스트_메뉴_생성(MENU_NAME01, MENU_PRICE01)));
        OrderLineItem 생성된_주문_항목 = new OrderLineItem(new Menu(등록된_메뉴.getId()), 1);

        ExtractableResponse<Response> 등록된_주문1 = 주문_등록되어_있음(new OrderRequest(등록된_주문_테이블1.getId(), Arrays.asList(생성된_주문_항목)));
        ExtractableResponse<Response> 등록된_주문2 = 주문_등록되어_있음(new OrderRequest(등록된_주문_테이블2.getId(), Arrays.asList(생성된_주문_항목)));

        주문_상태_변경_요청(등록된_주문1, OrderStatus.MEAL);
        주문_상태_변경_요청(등록된_주문2, OrderStatus.MEAL);

        return new TableGroup(request.getId());
    }

    public static ExtractableResponse<Response> 테이블_그룹_등록되어_있음(TableGroupRequest request) {
        return 테이블_그룹_생성_요청(request);
    }

    public static ExtractableResponse<Response> 테이블_그룹_생성_요청(TableGroupRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static void 테이블_그룹_생성_검증됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 테이블_그룹_생성_실패됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 테이블_그룹_해지_요청(Long tableGroupId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/{tableGroupId}", tableGroupId)
                .then().log().all()
                .extract();
    }

    public static TableGroupResponse 테이블_그룹_가져옴(ExtractableResponse<Response> response) {
        return response.as(TableGroupResponse.class);
    }
}
