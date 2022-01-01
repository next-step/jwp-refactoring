package kitchenpos.tableGroup;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.tableGroup.application.TableGroupService;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체 관련 기능")
public class TableGroupServiceTest {

    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    TableGroupService tableGroupService;

    private TableGroup tableGroup;
    private TableGroupRequest tableGroupRequest;
    OrderTable orderTable1;
    OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = 주문_테이블_생성(1L, 2, true);
        orderTable2 = 주문_테이블_생성(2L, 4, true);
        tableGroupRequest = 단체_지정_요청(Arrays.asList(new OrderTableRequest(orderTable1.getId()), new OrderTableRequest(orderTable2.getId())));
    }

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        tableGroup = mock(TableGroup.class);

        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        TableGroupResponse createTableGroup = tableGroupService.create(tableGroupRequest);

        assertThat(createTableGroup).isNotNull();

    }

    @Test
    @DisplayName("테이블 그룹을 등록할 때 테이블 갯수가 2보다 작으면 실패한다.")
    void createWithUnderTwoOrderTable() {
        TableGroupRequest tableGroupRequest = 단체_지정_요청(Arrays.asList(new OrderTableRequest(orderTable1.getId())));

        단체_지정_실패(tableGroupRequest, "주문 테이블을 2개 이상");
    }

    private OrderTable 주문_테이블_생성(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    private TableGroupRequest 단체_지정_요청(List<OrderTableRequest> orderTables) {
        return new TableGroupRequest(orderTables);
    }

    private void 단체_지정_실패(TableGroupRequest tableGroupRequest, String message) {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).withMessageContaining(message);
    }
}
