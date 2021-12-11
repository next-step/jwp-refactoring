package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.order.OrderService;
import kitchenpos.application.table.TableGroupService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.TableGroupDto;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private OrderService orderService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 치킨_주문_단체테이블;
    private OrderTable 치킨2_주문_단체테이블;
    private TableGroup 단체주문테이블;

    @BeforeEach
    void setUp() {
        치킨_주문_단체테이블 = OrderTable.of(1L, 0, true);
        치킨2_주문_단체테이블 = OrderTable.of(2L, 0, true);

        단체주문테이블 = TableGroup.of(1L, List.of(치킨_주문_단체테이블, 치킨2_주문_단체테이블));
    }

    @DisplayName("단체지정이 저장된다.")
    @Test
    void create_tableGroup() {
        // given
        when(orderTableRepository.findAllByIdIn(List.of(this.치킨_주문_단체테이블.getId(), this.치킨2_주문_단체테이블.getId()))).thenReturn(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(this.단체주문테이블);
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(null);

        // when
        TableGroupDto createdTableGroup = tableGroupService.create(TableGroupDto.of(this.단체주문테이블));

        // then
        Assertions.assertThat(createdTableGroup).isEqualTo(TableGroupDto.of(this.단체주문테이블));
    }

    @DisplayName("주문테이블이 없이 단체지정시 예외가 발생된다.")
    @Test
    void exception_createTableGoup_notExistOrderTable() {
        // given
        when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));

        this.단체주문테이블.changeOrderTables(null);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.create(TableGroupDto.of(this.단체주문테이블)));
    }

    @DisplayName("주문테이블의 개수가 2개 미만으로 단체지정시 예외가 발생된다.")
    @Test
    void exception_createTableGoup_underTwoCountOrderTable() {
        // given
        when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));

        this.단체주문테이블.changeOrderTables(List.of(this.치킨_주문_단체테이블));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.create(TableGroupDto.of(this.단체주문테이블)));
    }

    @DisplayName("미존재 주문테이블가 포함된 단체지정으로 저장시 예외가 발생된다.")
    @Test
    void exception_createTableGoup_containNotExistOrderTable() {
        // given
        when(orderTableRepository.findAllByIdIn(List.of(this.치킨_주문_단체테이블.getId(), this.치킨2_주문_단체테이블.getId()))).thenReturn(List.of(this.치킨_주문_단체테이블));

        this.단체주문테이블.changeOrderTables(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.create(TableGroupDto.of(this.단체주문테이블)));
    }

    @DisplayName("단체지정이 될 주문테이블 다른 단체지정에 등록된 경우 예외가 발생된다.")
    @Test
    void exception_createTableGoup_existOrderTableInOtherTableGroup() {
        // given
        this.치킨_주문_단체테이블.changeTableGroup(this.단체주문테이블);
        this.단체주문테이블.changeOrderTables(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.create(TableGroupDto.of(this.단체주문테이블)));
    }

    @DisplayName("단체지정이 해제된다.")
    @Test
    void update_tableUnGroup() {
        // given
        when(orderTableRepository.findAllByTableGroupId(this.단체주문테이블.getId())).thenReturn(this.단체주문테이블.getOrderTables());

        // when
        tableGroupService.ungroup(this.단체주문테이블.getId());

        // then
        verify(orderTableRepository, atLeast(1)).save(any(OrderTable.class));
    }

    @DisplayName("주문테이블의 주문상태가 계산 단계가 아닐때 단체지정이 해제시 예외가 발생된다.")
    @Test
    void exception_updateTableUnGroup_notCompletionOrderStatus() {
        // given
        when(orderService.isExistNotCompletionOrder(anyList())).thenReturn(true);
        when(orderTableRepository.findAllByTableGroupId(this.단체주문테이블.getId())).thenReturn(this.단체주문테이블.getOrderTables());

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.ungroup(this.단체주문테이블.getId()));
    }
}
