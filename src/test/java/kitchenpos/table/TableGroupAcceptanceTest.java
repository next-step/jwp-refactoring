package kitchenpos.table;

import static kitchenpos.table.TableAcceptanceTest.사용가능;
import static kitchenpos.table.TableAcceptanceTest.손님_입장;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class TableGroupAcceptanceTest extends AcceptanceTest {

    TableGroup 단체손님;
    OrderTable 손님1;
    OrderTable 손님2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        손님1 = 손님_입장(4, 사용가능).as(OrderTable.class);
        손님2 = 손님_입장(4, 사용가능).as(OrderTable.class);
    }

    @Test
    @DisplayName("단체 손님을 생성한다")
    void 단체_손님을_생성한다() {
        // when
        ExtractableResponse<Response> response = 단체_손님을_생성(Arrays.asList(손님1, 손님2));

        // then
        단체_손님이_생성됨(response);
    }

    @Test
    @DisplayName("단체 손님을 해제한다")
    void 단체_손님을_해제한다() {
        // given
        Long 단체손님 = 단체_손님을_생성(Arrays.asList(손님1, 손님2)).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 단체_손님을_해제(단체손님);

        // then
        단체_손님이_해제됨(response);
    }


    public static ExtractableResponse<Response> 단체_손님을_생성(List<OrderTable> orderTableList) {
        List<Long> orderTableIds = orderTableList.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        TableGroupRequest tableGroup = new TableGroupRequest(orderTableIds);

        return AcceptanceTest.doPost("/api/table-groups", tableGroup);
    }

    public static ExtractableResponse<Response> 단체_손님을_해제(Long orderTableId) {
        return AcceptanceTest.doDelete("/api/table-groups/" + orderTableId);
    }

    public static void 단체_손님이_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 단체_손님이_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
