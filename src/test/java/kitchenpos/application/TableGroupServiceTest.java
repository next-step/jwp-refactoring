package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TableGroupServiceTest {
    @Mock
    private OrderRepository orderRepository;

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
        치킨_주문_단체테이블 = OrderTable.of(null, 0);

        치킨2_주문_단체테이블 = OrderTable.of(null, 0);

        단체주문테이블 = TableGroup.of(LocalDateTime.now(), List.of(치킨_주문_단체테이블, 치킨2_주문_단체테이블));
    }

    @DisplayName("단체지정이 저장된다.")
    @Test
    void create_tableGroup() {
        // given
        when(orderTableRepository.findAllByIdIn(List.of(this.치킨_주문_단체테이블.getId(), this.치킨2_주문_단체테이블.getId()))).thenReturn(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));
        when(tableGroupRepository.save(this.단체주문테이블)).thenReturn(this.단체주문테이블);
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(null);

        // when
        TableGroup createdTableGroup = tableGroupService.create(this.단체주문테이블);

        // then
        Assertions.assertThat(createdTableGroup).isEqualTo(this.단체주문테이블);
    }

    @DisplayName("주문테이블이 없이 단체지정시 예외가 발생된다.")
    @Test
    void exception_createTableGoup_notExistOrderTable() {
        // given
        when(orderTableRepository.findAllByIdIn(List.of(this.치킨_주문_단체테이블.getId(), this.치킨2_주문_단체테이블.getId()))).thenReturn(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(this.단체주문테이블);
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(null);

        this.단체주문테이블.changeOrderTables(null);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.create(this.단체주문테이블));
    }

    @DisplayName("주문테이블의 개수가 2개 미만으로 단체지정시 예외가 발생된다.")
    @Test
    void exception_createTableGoup_underTwoCountOrderTable() {
        // given
        when(orderTableRepository.findAllByIdIn(List.of(this.치킨_주문_단체테이블.getId(), this.치킨2_주문_단체테이블.getId()))).thenReturn(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(this.단체주문테이블);
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(null);

        this.단체주문테이블.changeOrderTables(List.of(this.치킨_주문_단체테이블));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.create(this.단체주문테이블));
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
                    .isThrownBy(() -> tableGroupService.create(this.단체주문테이블));
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
                    .isThrownBy(() -> tableGroupService.create(this.단체주문테이블));
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
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(),anyList())).thenReturn(true);
        when(orderTableRepository.findAllByTableGroupId(this.단체주문테이블.getId())).thenReturn(this.단체주문테이블.getOrderTables());

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.ungroup(this.단체주문테이블.getId()));
    }
}
