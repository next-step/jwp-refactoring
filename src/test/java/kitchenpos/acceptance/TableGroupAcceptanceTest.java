package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단체 지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    @Test
    @DisplayName("단체 지정 관리 기능")
    void tableGroup() {
        // when
        ExtractableResponse<Response> response = 단체_지정(1L, 2L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(TableGroup.class).getOrderTables()).hasSize(2);

        // when
        ExtractableResponse<Response> deleteResponse = 단체_지정_해제(response.as(TableGroup.class));

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 단체_지정_해제(TableGroup tableGroup) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/{id}", tableGroup.getId())
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 단체_지정(Long... orderTableIds) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(
                Arrays.stream(orderTableIds).map(id -> {
                    OrderTable orderTable = new OrderTable();
                    orderTable.setId(id);
                    return orderTable;
                }).collect(Collectors.toList()));
        return RestAssured
                .given().log().all()
                .body(tableGroup)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all().extract();
    }
}
