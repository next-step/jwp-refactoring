package kitchenpos.tablegroup.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.*;
import static kitchenpos.order.acceptance.OrderAcceptanceTest.주문_등록되어_있음;
import static kitchenpos.table.acceptance.TableAcceptanceTest.주문_테이블_가져옴;
import static kitchenpos.table.acceptance.TableAcceptanceTest.주문_테이블_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 그룹 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> 등록된_테이블_그룹 = 테이블_그룹_등록되어_있음(테스트_테이블_그룹_생성());

        // then
        테이블_그룹_생성_검증됨(등록된_테이블_그룹);
    }

    @DisplayName("테이블 그룹과 주문 정보 간에 관계를 해지한다.")
    @Test
    void ungroup() {

    }

    private static TableGroup 테스트_테이블_그룹_생성() {
        Menu 등록된_메뉴 = 메뉴_가져옴(메뉴_등록되어_있음(테스트_메뉴_생성()));
        OrderLineItem 생성된_주문_항목 = new OrderLineItem(등록된_메뉴.getId(), 1);

        OrderTable 등록된_주문_테이블1 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, true));
        OrderTable 등록된_주문_테이블2 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(5, true));

        주문_등록되어_있음(new Order(등록된_주문_테이블1.getId(), Arrays.asList(생성된_주문_항목)));
        주문_등록되어_있음(new Order(등록된_주문_테이블2.getId(), Arrays.asList(생성된_주문_항목)));

        return new TableGroup(Arrays.asList(등록된_주문_테이블1, 등록된_주문_테이블2));
    }

    public static ExtractableResponse<Response> 테이블_그룹_등록되어_있음(TableGroup tableGroup) {
        return 테이블_그룹_생성_요청(tableGroup);
    }

    public static ExtractableResponse<Response> 테이블_그룹_생성_요청(TableGroup tableGroup) {
        return RestAssured
                .given().log().all()
                .body(tableGroup)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static void 테이블_그룹_생성_검증됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 테이블_그룹_해지_요청(Long tableGroupId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/{tableGroupId}", tableGroupId)
                .then().log().all()
                .extract();
    }
}
