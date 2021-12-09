package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.acceptance.TableAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("테이블 그룹 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        orderTable1 = 테이블_등록되어_있음(new OrderTable(2, true));
        orderTable2 = 테이블_등록되어_있음(new OrderTable(2, true));
    }

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        // given
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));

        // when
        ExtractableResponse<Response> response = 테이블_그룹_등록_요청(tableGroup);

        // then
        테이블_그룹_등록됨(response);
    }

    @Test
    @DisplayName("테이블 그룹에서 테이블을 제거한다.")
    void list() {
        // given
        TableGroup savedTableGroup = 테이블_그룹_등록되어_있음(
                new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2)));

        // when
        ExtractableResponse<Response> response = 테이블_그룹에서_테이블_제거_요청(savedTableGroup.getId());

        // then
        테이블_그룹에서_테이블_제거됨(response);
    }

    public static TableGroup 테이블_그룹_등록되어_있음(TableGroup tableGroup) {
        return post("/api/table-groups", tableGroup).as(TableGroup.class);
    }

    public static ExtractableResponse<Response> 테이블_그룹_등록_요청(TableGroup tableGroup) {
        return post("/api/table-groups", tableGroup);
    }

    public static ExtractableResponse<Response> 테이블_그룹에서_테이블_제거_요청(Long id) {
        return delete("/api/table-groups/{tableGroupId}", id);
    }

    private void 테이블_그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 테이블_그룹에서_테이블_제거됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
