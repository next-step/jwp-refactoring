package table_group.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static table_group.fixture.TableGroupFixture.단체_데이터_생성;
import static table_group.fixture.TableGroupFixture.단체_지정_데이터_생성;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import table.domain.OrderTable;
import table.repository.OrderTableRepository;
import table_group.dto.TableGroupRequestDto;
import table_group.dto.TableGroupResponseDto;
import table_group.repository.TableGroupRepository;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    private TableGroupService tableGroupService;

    @Mock
    private TableGroupValidator tableGroupValidator;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(tableGroupValidator, orderTableRepository, tableGroupRepository);
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        //given
        TableGroupRequestDto request = 단체_지정_데이터_생성(1L, 2L);

        OrderTable table1 = new OrderTable(1L, null, 4, true);
        OrderTable table2 = new OrderTable(2L, null, 3, true);
        given(orderTableRepository.findByIdIn(any())).willReturn(Arrays.asList(table1, table2));

        Long tableGroupId = 1L;
        given(tableGroupRepository.save(any())).willReturn(단체_데이터_생성(tableGroupId));

        //when
        TableGroupResponseDto response = tableGroupService.create(request);

        //then
        단체_데이터_확인(response, tableGroupId);
        테이블_그룹핑_확인(tableGroupId, table1, table2);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        //given
        Long tableGroupId = 1L;
        OrderTable table1 = new OrderTable(1L, 1L, 4, true);
        OrderTable table2 = new OrderTable(2L, 1L, 4, true);

        given(orderTableRepository.findByTableGroupId(tableGroupId)).willReturn(Arrays.asList(table1, table2));

        //when
        tableGroupService.ungroup(tableGroupId);

        //then
        assertThat(table1.getTableGroupId()).isNull();
        assertThat(table2.getTableGroupId()).isNull();
    }

    private void 단체_데이터_확인(TableGroupResponseDto response, Long tableGroupId) {
        assertEquals(tableGroupId, response.getId());
    }

    private void 테이블_그룹핑_확인(Long tableGroupId, OrderTable... orderTables) {
        for (final OrderTable orderTable : orderTables) {
            assertEquals(tableGroupId, orderTable.getTableGroupId());
        }
    }
}