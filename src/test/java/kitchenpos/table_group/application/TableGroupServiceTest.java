package kitchenpos.table_group.application;

import static kitchenpos.common.fixture.OrderTableFixture.주문테이블_데이터_생성;
import static kitchenpos.common.fixture.TableGroupFixture.단체_데이터_생성;
import static kitchenpos.common.fixture.TableGroupFixture.단체_지정_데이터_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableGroupRequestDto;
import kitchenpos.table.dto.TableGroupResponseDto;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table_group.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        OrderTable table1 = 주문테이블_데이터_생성(1L, null, 4, true);
        OrderTable table2 = 주문테이블_데이터_생성(2L, null, 3, true);
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
        OrderTable table1 = 주문테이블_데이터_생성(1L, 1L, 4, true);
        OrderTable table2 = 주문테이블_데이터_생성(2L, 1L, 4, true);

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