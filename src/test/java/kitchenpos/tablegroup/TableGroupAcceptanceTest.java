package kitchenpos.tablegroup;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("단체 지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    private TableGroupCreateRequest tableGroupCreateRequest;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        final OrderTable firstOrderTable = orderTableRepository.save(OrderTable.builder().empty(true).build());
        final OrderTable secondOrderTable = orderTableRepository.save(OrderTable.builder().empty(true).build());
        tableGroupCreateRequest = new TableGroupCreateRequest(Arrays.asList(new TableGroupCreateRequest.OrderTable(firstOrderTable.getId()),
                new TableGroupCreateRequest.OrderTable(secondOrderTable.getId())));
    }

    @Test
    @DisplayName("단체 지정을 할 수 있다.")
    void createTableGroup() {
        // when
        final ExtractableResponse<Response> 단체_지정_요청_응답 = 단쳬_지정_요청(tableGroupCreateRequest);

        // then
        단체_지정_됨(단체_지정_요청_응답);
    }

    private void 단체_지정_됨(final ExtractableResponse<Response> response) {
        final TableGroup 생성된_단체 = response.as(TableGroup.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(생성된_단체.getId()).isNotNull(),
                () -> assertThat(생성된_단체.getOrderTables()).isNotNull(),
                () -> assertThat(생성된_단체.getOrderTables().size()).isEqualTo(2)
        );
    }

    public ExtractableResponse<Response> 단쳬_지정_요청(final TableGroupCreateRequest tableGroupCreateRequest) {
        return post("/api/table-groups", tableGroupCreateRequest);
    }

    @Test
    @DisplayName("단체 지정을 해제 할 수 있다.")
    void ungroup() {
        // given
        final TableGroupResponse 생성된_단체 = 생성된_단체(tableGroupCreateRequest);

        // when
        final ExtractableResponse<Response> 단체_지정_해체_요청_응답 = 단체_지정_해체_요청(생성된_단체);

        // then
        단체_지정_해체_됨(단체_지정_해체_요청_응답);
    }

    private void 단체_지정_해체_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public ExtractableResponse<Response> 단체_지정_해체_요청(final TableGroupResponse 생성된_단체) {
        return delete("/api/table-groups/{tableGroupId}", 생성된_단체.getId());
    }

    private TableGroupResponse 생성된_단체(final TableGroupCreateRequest request) {
        return 단쳬_지정_요청(request).as(TableGroupResponse.class);
    }
}
