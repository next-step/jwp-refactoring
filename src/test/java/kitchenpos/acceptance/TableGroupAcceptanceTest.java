package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static kitchenpos.acceptance.TableAcceptanceTest.주문_테이블_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단체 지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        orderTable1 = 빈_테이블_등록_되어있음(4);
        orderTable2 = 빈_테이블_등록_되어있음(4);
    }

    @Test
    @DisplayName("단체 지정 관리")
    void tableGroup() {
        // when
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        ExtractableResponse<Response> response = 단체_지정_등록_요청(tableGroup);
        // then
        단체_지정_등록됨(response);
        TableGroup createdTableGroup = response.as(TableGroup.class);

        // when
        response = 단체_지정_해제_요청(createdTableGroup.getId());
        // then
        단체_지정_해제됨(response);
    }

    public static OrderTable 빈_테이블_등록_되어있음(int numberOfGuests) {
        return 주문_테이블_등록_요청(true, numberOfGuests).as(OrderTable.class);
    }

    public static ExtractableResponse<Response> 단체_지정_등록_요청(TableGroup tableGroup) {

        return RestAssured
                .given().log().all()
                .body(tableGroup)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 단체_지정_해제_요청(Long id) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/{id}", id)
                .then().log().all().extract();
    }

    public static void 단체_지정_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 단체_지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
