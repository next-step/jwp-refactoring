package kitchenpos.acceptence;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceSupport;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.order.dto.TableRequest;
import kitchenpos.order.dto.TableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static kitchenpos.acceptence.TableRestControllerTest.주문테이블을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;

class TableGroupRestControllerTest extends AcceptanceSupport {
    private TableResponse 주문테이블_일번;
    private TableResponse 주문테이블_이번;
    private TableGroupRequest 크리스마스파티;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블_일번 = 주문테이블을_생성한다(new TableRequest(5, true))
                .as(TableResponse.class);
        주문테이블_이번 = 주문테이블을_생성한다(new TableRequest(9, true))
                .as(TableResponse.class);

        크리스마스파티 = new TableGroupRequest(Arrays.asList(주문테이블_일번.getId(), 주문테이블_이번.getId()));
    }

    @Test
    @DisplayName("단체지정을 등록 할 수 있다.")
    void createTableGroup() {
        // when
        ExtractableResponse<Response> response = 단체지정_생성을_요청한다(크리스마스파티);

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.CREATED);
    }

    @Test
    @DisplayName("단체지정 등록 취소 할 수 있다.")
    void upGroupTableGroup() {
        // given
        TableGroupResponse 크리스마스파티_응답 = 단체지정_생성을_요청한다(크리스마스파티).as(TableGroupResponse.class);

        // when
        ExtractableResponse<Response> response = 단체지정_취소를_요청한다(크리스마스파티_응답.getId());

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.NO_CONTENT);
    }

    private ExtractableResponse<Response> 단체지정_생성을_요청한다(TableGroupRequest tableGroup) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    private void 상태값을_비교한다(int statusCode, HttpStatus created) {
        assertThat(statusCode).isEqualTo(created.value());
    }

    private ExtractableResponse<Response> 단체지정_취소를_요청한다(Long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/api/table-groups/{id}", id)
                .then().log().all()
                .extract();
    }
}
