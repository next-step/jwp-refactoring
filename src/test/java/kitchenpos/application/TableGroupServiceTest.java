package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
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
import kitchenpos.dto.OrderTableDto;
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
    private OrderTable 치킨3_주문_단체테이블;

    private TableGroup 단체주문테이블;
    private TableGroup 단체2주문테이블;

    @BeforeEach
    void setUp() {
        치킨_주문_단체테이블 = OrderTable.of(1L, 0, true);
        치킨2_주문_단체테이블 = OrderTable.of(2L, 0, true);
        치킨3_주문_단체테이블 =  OrderTable.of(3L, 0, true);

        단체주문테이블 = TableGroup.of(1L);
        단체2주문테이블 = TableGroup.of(2L);
    }

    @DisplayName("단체지정이 저장된다.")
    @Test
    void create_tableGroup() {
        // given
        TableGroupDto 단체지정_요청정보 = TableGroupDto.of(List.of(OrderTableDto.of(this.치킨_주문_단체테이블), OrderTableDto.of(this.치킨2_주문_단체테이블)));
        List<OrderTable> 조회된_주문테이블_리스트 = List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블);
        TableGroup 단체주문테이블 = TableGroup.of(1L, List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));

        when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(조회된_주문테이블_리스트);
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(단체주문테이블);

        // when
        TableGroupDto createdTableGroup = tableGroupService.create(단체지정_요청정보);

        // then
        Assertions.assertThat(createdTableGroup).isNotNull();
    }

    @DisplayName("주문테이블의 개수가 2개 미만으로 단체지정시 예외가 발생된다.")
    @Test
    void exception_createTableGoup_underTwoCountOrderTable() {
        // given
        TableGroup 단체주문테이블 = TableGroup.of(1L, List.of(this.치킨_주문_단체테이블));
        List<OrderTable> 조회된_주문테이블_리스트 = List.of(this.치킨_주문_단체테이블);

        when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(조회된_주문테이블_리스트);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.create(TableGroupDto.of(단체주문테이블)));
    }

    @DisplayName("미존재 주문테이블가 포함된 단체지정으로 저장시 예외가 발생된다.")
    @Test
    void exception_createTableGoup_containNotExistOrderTable() {
        // given
        TableGroup 단체주문테이블 = TableGroup.of(1L, List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));
        List<OrderTable> 조회된_주문테이블_리스트 = List.of(this.치킨_주문_단체테이블);

        when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(조회된_주문테이블_리스트);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.create(TableGroupDto.of(단체주문테이블)));
    }

    @DisplayName("단체지정 될 주문테이블이 이미 단체지정에 등록된 경우 예외가 발생된다.")
    @Test
    void exception_createTableGoup_existOrderTableInOtherTableGroup() {
        // given
        TableGroup 단체주문테이블 = TableGroup.of(1L, List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));
        TableGroup 단체2주문테이블 = TableGroup.of(1L, List.of(this.치킨2_주문_단체테이블, this.치킨3_주문_단체테이블));

        List<OrderTable> 조회된_주문테이블_리스트 = List.of(OrderTable.of(1L, 0, true), OrderTable.of(2L, 단체2주문테이블, 0, true));

        when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(조회된_주문테이블_리스트);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.create(TableGroupDto.of(단체주문테이블)));
    }

    @DisplayName("단체지정이 해제된다.")
    @Test
    void update_tableUnGroup() {
        // given
        TableGroup 단체주문테이블 = TableGroup.of(1L, Lists.newArrayList(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));
        List<OrderTable> 조회된_주문테이블_리스트 = List.of(OrderTable.of(1L, 단체주문테이블, 0, true), OrderTable.of(2L, 단체주문테이블, 0, true));

        when(orderTableRepository.findAllByTableGroupId(단체주문테이블.getId())).thenReturn(조회된_주문테이블_리스트);

        // when
        tableGroupService.ungroup(단체주문테이블.getId());

        // then
        assertAll(
            () -> Assertions.assertThat(this.치킨_주문_단체테이블.getTableGroup()).isNull(),
            () -> Assertions.assertThat(this.치킨2_주문_단체테이블.getTableGroup()).isNull()
        );
    }

    @DisplayName("주문테이블의 주문상태가 계산 단계가 아닐때 단체지정이 해제시 예외가 발생된다.")
    @Test
    void exception_updateTableUnGroup_notCompletionOrderStatus() {
        // given
        TableGroup 단체주문테이블 = TableGroup.of(1L, Lists.newArrayList(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));
        List<OrderTable> 조회된_주문테이블_리스트 = List.of(OrderTable.of(1L, 단체주문테이블, 0, true), OrderTable.of(2L, 단체주문테이블, 0, true));

        when(orderService.isExistNotCompletionOrder(anyList())).thenReturn(true);
        when(orderTableRepository.findAllByTableGroupId(단체주문테이블.getId())).thenReturn(조회된_주문테이블_리스트);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.ungroup(this.단체주문테이블.getId()));
    }
}
