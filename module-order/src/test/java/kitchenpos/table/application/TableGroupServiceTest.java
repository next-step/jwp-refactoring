package kitchenpos.table.application;

import static kitchenpos.table.fixture.TableFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private TableValidator tableValidator;
    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable orderTable_1;
    private OrderTable orderTable_2;
    private TableGroup tableGroup;
    private TableGroupRequest tableGroupRequest;

    @BeforeEach
    void setUp() {
        orderTable_1 = 테이블_생성(1L, 2, true);
        orderTable_2 = 테이블_생성(2L, 4, true);
        tableGroup = 테이블그룹_생성(1L, Arrays.asList(orderTable_1, orderTable_2));

        tableGroupRequest = 테이블그룹요청_생성();
    }

    @DisplayName("테이블 그룹을 등록한다.")
    @Test
    void create() {
        //given
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable_1, orderTable_2));
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        //when
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        //then
        assertThat(tableGroupResponse).isNotNull();
        assertThat(tableGroupResponse.getOrderTables().size()).isEqualTo(2);
    }

    @DisplayName("그룹 내에 존재하지 않는 테이블이 있으면, 테이블 그룹으로 등록할 수 없다.")
    @Test
    void create_invalidNotExistsOrderTable() {
        //given
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable_1));

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹 내에 비어있지 않은 테이블이 있으면, 테이블 그룹으로 등록할 수 없다.")
    @Test
    void create_invalidEmptyOrderTable() {
        //given
        orderTable_1.changeEmpty(false);
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable_1, orderTable_2));

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹 내에 그룹에 속한 테이블이 있으면, 테이블 그룹으로 등록할 수 없다.")
    @Test
    void create_invalidOtherGroup() {
        //given
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable_1, orderTable_2));
        given(tableGroupRepository.existsByOrderTableIdIn(any())).willReturn(true);

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        //given
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));

        //when & then
        tableGroupService.ungroup(tableGroup.getId());

        //then
        assertThat(tableGroup.getOrderTables().size()).isZero();
    }

    @DisplayName("그룹 내 테이블의 주문 상태가 조리나 식사중이면, 그룹을 해제할 수 없다.")
    @Test
    void ungroup_invalidOrderStatus() {
        //given
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));
        willThrow(new IllegalArgumentException("현재 조리중이거나 식사중인 주문이 존재합니다."))
                .given(tableValidator)
                .validateOrderStatus(1L);

        //when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
