package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroupRepository;
import kitchenpos.ordertable.dto.TableGroupRequest;
import kitchenpos.ordertable.dto.TableGroupResponse;
import kitchenpos.ordertable.testfixtures.TableGroupTestFixtures;
import kitchenpos.ordertable.vo.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @DisplayName("테이블 그룹을 등록할 수 있다.")
    @Test
    void create() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, new NumberOfGuests(6), true),
            new OrderTable(2L, new NumberOfGuests(3), true));
        TableGroupRequest tableGroupRequest = TableGroupTestFixtures.convertToTableGroupRequest(
            orderTables);
        TableGroup tableGroup = new TableGroup(1L);
        TableGroupTestFixtures.테이블그룹_저장_결과_모킹(tableGroupRepository, tableGroup);

        //when
        TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);

        //then
        assertThat(savedTableGroup.getCreatedDate()).isEqualTo(tableGroup.getCreatedDate());
    }
}
