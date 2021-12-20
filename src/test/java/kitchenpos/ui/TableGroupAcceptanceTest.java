package kitchenpos.ui;

import static kitchenpos.ui.OrderAcceptanceTest.주문테이블_생성;
import static kitchenpos.utils.AcceptanceTestUtil.delete;
import static kitchenpos.utils.AcceptanceTestUtil.post;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class TableGroupAcceptanceTest extends AcceptanceTest {
    private OrderTable 테이블1;
    private OrderTable 테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        테이블1 = 주문테이블_생성(OrderTable.ofWithEmpty(2)).as(OrderTable.class);
        테이블2 = 주문테이블_생성(OrderTable.ofWithEmpty(3)).as(OrderTable.class);
    }

    @DisplayName("테이블 그룹을 생성한다")
    @Test
    void createTableGroup() {
        // given
        TableGroup 테이블_그룹 = TableGroup.of(Lists.newArrayList(테이블1, 테이블2));

        // when
        ExtractableResponse<Response> 테이블_그룹_생성_응답 = 테이블_그룹_생성(테이블_그룹);

        // then
        테이블_그룹_생성됨(테이블_그룹_생성_응답);
    }

    @DisplayName("테이블 그룹을 해제한다")
    @Test
    void unGroup() {
        // given
        TableGroup 생성된_테이블_그룹 = 테이블_그룹_생성(TableGroup.of(Lists.newArrayList(테이블1, 테이블2))).as(
            TableGroup.class);

        // when
        ExtractableResponse<Response> 테이블그룹_해제_응답 = 테이블그룹_해제(생성된_테이블_그룹);

        // then
        테이블그룹_해제됨(테이블그룹_해제_응답);

    }

    private ExtractableResponse<Response> 테이블_그룹_생성(TableGroup tableGroup) {
        return post("/api/table-groups", tableGroup);
    }

    private void 테이블_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();

        TableGroup tableGroup = response.as(TableGroup.class);
        assertThat(tableGroup.getOrderTables()).extracting(OrderTable::getId)
            .contains(테이블1.getId(), 테이블2.getId());
    }

    private ExtractableResponse<Response> 테이블그룹_해제(TableGroup tableGroup) {
        return delete("/api/table-groups/" + tableGroup.getId());
    }

    private void 테이블그룹_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}