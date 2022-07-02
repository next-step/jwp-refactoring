package kitchenpos.table_group.application;

import static kitchenpos.common.fixture.OrderTableFixture.주문테이블_데이터_생성;
import static kitchenpos.common.fixture.TableGroupFixture.단체_데이터_생성;
import static kitchenpos.common.fixture.TableGroupFixture.단체_지정_데이터_생성;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.order.application.TableValidatorImpl;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableGroupRequestDto;
import kitchenpos.table.dto.TableGroupResponseDto;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table_group.domain.TableGroup;
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
    private TableValidatorImpl tableValidator;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(tableValidator, orderTableRepository, tableGroupRepository);
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
    }

    @DisplayName("조회되지 않는 주문 테이블이 있으면 생성할 수 없다.")
    @Test
    void create_fail_notExistsTable() {
        //given
        TableGroupRequestDto request = 단체_지정_데이터_생성(1L, 2L);
        OrderTable table1 = 주문테이블_데이터_생성(1L, null, 4, true);
        given(orderTableRepository.findByIdIn(any())).willReturn(Arrays.asList(table1));

        //when //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        //given
        Long tableGroupId = 1L;

        TableGroup tableGroup = 단체_데이터_생성(tableGroupId);
        OrderTable table1 = 주문테이블_데이터_생성(1L, null, 4, true);
        OrderTable table2 = 주문테이블_데이터_생성(2L, null, 4, true);
        tableGroup.group(Arrays.asList(table1, table2));
        given(tableGroupRepository.findById(tableGroupId)).willReturn(Optional.of(tableGroup));

        //when //then
        tableGroupService.ungroup(tableGroupId);
    }

    private void 단체_데이터_확인(TableGroupResponseDto response, Long tableGroupId) {
        assertEquals(tableGroupId, response.getId());
    }
}