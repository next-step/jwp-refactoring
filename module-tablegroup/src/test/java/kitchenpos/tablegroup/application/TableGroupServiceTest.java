package kitchenpos.tablegroup.application;


import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체 관련 기능")
public class TableGroupServiceTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = 테이블_등록(2, true);
        orderTable2 = 테이블_등록(4, true);
    }

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        // when
        TableGroupResponse tableGroup = 단체_지정_등록();

        // then
        assertThat(tableGroup).isNotNull();
    }

    @Test
    @DisplayName("테이블 그룹을 등록시, 테이블 갯수가 2보다 작으면 실패한다.")
    void createWithUnderTwoOrderTable() {
        // when-then
        assertThatThrownBy(() -> 단체_지정(Arrays.asList())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해제한다.")
    void ungroup() {
        // given
        TableGroupResponse tableGroup = 단체_지정_등록();

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertThat(orderTable1.getTableGroup()).isNull();
    }

    public static OrderTable 테이블_등록(int numberOfGuests, boolean empty) {
        return OrderTable.of(numberOfGuests, empty);
    }

    public TableGroupResponse 단체_지정_등록() {
        TableGroupRequest tableGroupRequest = 단체_지정_요청(Arrays.asList(OrderTableIdRequest.of(1L), OrderTableIdRequest.of(2L)));
        given(tableGroupRepository.save(any())).willReturn(TableGroup.of(1L));
        return tableGroupService.create(tableGroupRequest);
    }

    public static OrderTables 단체_지정(List<OrderTable> orderTables) {
        return OrderTables.of(orderTables);
    }

    public static TableGroupRequest 단체_지정_요청(List<OrderTableIdRequest> orderTables) {
        return TableGroupRequest.of(orderTables);
    }
}
