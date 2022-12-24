package kitchenpos;

import static kitchenpos.OrderAcceptanceTest.메뉴_등록_요청;
import static kitchenpos.OrderAcceptanceTest.주문_생성_요청;
import static kitchenpos.TableAcceptanceTest.테이블_생성_요청;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("단체 지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private OrderTable 테이블3;
    private OrderTable 테이블4;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        테이블1 = 테이블_생성_요청(true, 0).as(OrderTable.class);
        테이블2 = 테이블_생성_요청(true, 0).as(OrderTable.class);
        테이블3 = 테이블_생성_요청(true, 0).as(OrderTable.class);
        테이블4 = 테이블_생성_요청(true, 0).as(OrderTable.class);
    }

    /**
     * Feature: 단체 지정 관련 기능
     *  Background:
     *      given: 테이블을 생성한다
     *      and: 빈테이블을 생성한다
     *      and: 메뉴를 생성한다
     * <p>
     *  Scenario: 단체 지정 추가 조회
     *      when: 단체 지정을 생성
     *      then: 단체 지정이 생성됨
     *      when: 주문이 들어간 테이블이 포함된 단체 지정을 등록한다
     *      then: 단체 지정이 생성이 실패됨
     *      when: 한개의 테이블로 단체지정 생성
     *      then: 단체 지정이 생성이 실패됨
     *      when: 존재하지 않는 테이블로 단체지정 생성
     *      then: 단체 지정이 생성이 실패됨
     *      when: 다른 단체에 등록된 테이블로 단체지정 생성
     *      then: 단체 지정이 생성이 실패됨
     *      when: 단체 지정을 삭제한다
     *      then: 단체 지정이 삭제됨
     *      when: 주문 상태인 테이블은 단체 지정을 삭제할 수 없다
     *      then: 단체 지정삭제가 실패됨
     */

    @DisplayName("단체 지정 추가 삭제")
    @Test
    void scenarioTableGroup() {
        // when
        ExtractableResponse<Response> 단체지정생성 = 단체_지정_생성_요청(테이블1, 테이블2);
        // then
        단체_지정_생성됨(단체지정생성);
        TableGroup 단체_지정1 = 단체지정생성.as(TableGroup.class);

        // when
        ExtractableResponse<Response> 주문된테이블_단체지정 = 단체_지정_생성_요청(new OrderTable(), 테이블3);
        // then
        단체_지정_생성_실패됨(주문된테이블_단체지정);

        // when
        ExtractableResponse<Response> 단체지정한개 = 단체_지정_생성_요청(테이블4);
        단체_지정_생성_실패됨(단체지정한개);


        // when
        OrderTable 없는테이블 = new OrderTable();
        없는테이블.setId(Long.MAX_VALUE);
        ExtractableResponse<Response> 없는테이블단체지정 = 단체_지정_생성_요청(테이블4, 없는테이블);
        // then
        단체_지정_생성_실패됨(없는테이블단체지정);

        // when
        ExtractableResponse<Response> 중복단체지정 = 단체_지정_생성_요청(테이블4, 테이블1);
        // then
        단체_지정_생성_실패됨(중복단체지정);

        // when
        ExtractableResponse<Response> 단체지정해지 = 단체_지정_해지_요청(단체_지정1);
        // then
        단체_지정_해지됨(단체지정해지);

        // given
        ExtractableResponse<Response> 신규단체지정 = 단체_지정_생성_요청(테이블3, 테이블4);
        TableGroup 단체_지정2 = 신규단체지정.as(TableGroup.class);
        // when
        주문_생성_요청(테이블3, 메뉴_등록_요청().as(Menu.class));
        ExtractableResponse<Response> response = 단체_지정_해지_요청(단체_지정2);

        단체_지정_해지_실패됨(response);
    }

    public static ExtractableResponse<Response> 단체_지정_생성_요청(OrderTable... orderTables) {
        Map<String, Object> request = new HashMap<>();
        request.put("orderTables", Arrays.asList(orderTables));
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 단체_지정_해지_요청(TableGroup tableGroup) {
        return RestAssured
                .given().log().all()
                .when().delete("/api/table-groups/{tableGroupId}", tableGroup.getId())
                .then().log().all()
                .extract();
    }

    public static void 단체_지정_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 단체_지정_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 단체_지정_해지됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 단체_지정_해지_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}

