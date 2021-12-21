package kitchenpos.domain.tablegroup;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("단체 지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    private TableGroup tableGroup;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        final OrderTable firstOrderTable = orderTableDao.save(new OrderTable(true));
        final OrderTable secondOrderTable = orderTableDao.save(new OrderTable(true));
        tableGroup = new TableGroup(Arrays.asList(firstOrderTable, secondOrderTable));
    }

    @Test
    @DisplayName("단체 지정을 할 수 있다.")
    void createTableGroup() {
        // when
        final ExtractableResponse<Response> 단체_지정_요청_응답 = 단쳬_지정_요청(tableGroup);

        // then
        단체_지정_됨(단체_지정_요청_응답);
    }

    private void 단체_지정_됨(final ExtractableResponse<Response> response) {
        final TableGroup 생성된_단체 = response.as(TableGroup.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(생성된_단체.getId()).isNotNull(),
                () -> assertThat(생성된_단체.getOrderTables()).extracting("tableGroupId").contains(생성된_단체.getId()),
                () -> assertThat(생성된_단체.getOrderTables()).extracting("empty").contains(false)
        );
    }

    public ExtractableResponse<Response> 단쳬_지정_요청(final TableGroup tableGroup) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("단체 지정을 해제 할 수 있다.")
    void ungroup() {
        // given
        final TableGroup 생성된_단체 = 생성된_단체(tableGroup);

        // when
        final ExtractableResponse<Response> 단체_지정_해체_요청_응답 = 단체_지정_해체_요청(생성된_단체);

        // then
        단체_지정_해체_됨(단체_지정_해체_요청_응답);
    }

    private void 단체_지정_해체_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public ExtractableResponse<Response> 단체_지정_해체_요청(final TableGroup 생성된_단체) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/{tableGroupId}", 생성된_단체.getId())
                .then().log().all()
                .extract();
    }

    private TableGroup 생성된_단체(final TableGroup tableGroup) {
        return 단쳬_지정_요청(tableGroup).as(TableGroup.class);
    }
}
