package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.KitchenposException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup(1L);
    }

    @DisplayName("테이블 그룹 생성")
    @Test
    void create() {
        // given
        테이블_그룹_저장_결과_반환(tableGroup);

        List<OrderTableIdRequest> requestTables = Arrays.asList(
            new OrderTableIdRequest(1L),
            new OrderTableIdRequest(2L));
        TableGroupRequest request = new TableGroupRequest(requestTables);

        // when
        TableGroupResponse actual = tableGroupService.create(request);

        // then
        assertThat(actual.getId()).isNotNull();
        Mockito.verify(tableGroupRepository, Mockito.times(2)).save(Mockito.any());
    }

    @DisplayName("주문 테이블이 없거나 2개 미만일 시 생성 불가능")
    @Test
    void createTableGroupFailWhenOrderTablesSizeIsLessThanTwo() {
        // given
        List<OrderTableIdRequest> requestTables = Arrays.asList(new OrderTableIdRequest(2L));
        TableGroupRequest request = new TableGroupRequest(requestTables);

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> tableGroupService.create(request))
            .withMessage("테이블 그룹을 생성하기 위해 2개 이상의 테이블이 필요합니다.");
    }

    @DisplayName("테이블 그룹 삭제")
    @Test
    void ungroup() {
        // given
        ID로_테이블_그룹_조회(tableGroup);

        // when
        tableGroupService.ungroup(1L);

        // then
        Mockito.verify(tableGroupRepository).save(Mockito.any());
    }

    private void 테이블_그룹_저장_결과_반환(TableGroup tableGroup) {
        Mockito.when(tableGroupRepository.save(Mockito.any()))
            .thenReturn(tableGroup);
    }

    private void ID로_테이블_그룹_조회(TableGroup tableGroup) {
        Mockito.when(tableGroupRepository.findById(Mockito.anyLong()))
            .thenReturn(java.util.Optional.ofNullable(tableGroup));
    }
}