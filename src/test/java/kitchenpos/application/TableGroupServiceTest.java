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

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 치킨_주문_개인테이블 = new OrderTable();    
    private OrderTable 치킨_주문_단체테이블 = new OrderTable();
    private OrderTable 치킨2_주문_단체테이블 = new OrderTable();
    private TableGroup 단체주문테이블 = new TableGroup();
    
    @BeforeEach
    void setUp() {
        치킨_주문_개인테이블.setId(3L);
        치킨_주문_개인테이블.setEmpty(false);

        치킨_주문_단체테이블.setId(1L);
        치킨_주문_단체테이블.setEmpty(true);
        치킨2_주문_단체테이블.setId(2L);
        치킨2_주문_단체테이블.setEmpty(true);

        단체주문테이블.setId(1L);
        단체주문테이블.setOrderTables(List.of(치킨_주문_단체테이블, 치킨2_주문_단체테이블));
        단체주문테이블.setCreatedDate(LocalDateTime.now());
    }

    @DisplayName("단체지정이 저장된다.")
    @Test
    void create_tableGroup() {
        // given
        when(orderTableDao.findAllByIdIn(List.of(this.치킨_주문_단체테이블.getId(), this.치킨2_주문_단체테이블.getId()))).thenReturn(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));
        when(tableGroupDao.save(any(TableGroup.class))).thenReturn(this.단체주문테이블);
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(null);

        TableGroup 신규_단체지정 = new TableGroup();
        신규_단체지정.setOrderTables(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));
        
        // when
        TableGroup createdTableGroup = tableGroupService.create(신규_단체지정);

        // then
        Assertions.assertThat(createdTableGroup).isEqualTo(this.단체주문테이블);
    }

    @DisplayName("주문테이블이 없이 단체지정시 예외가 발생된다.")
    @Test
    void exception_createTableGoup_notExistOrderTable() {
        // given
        when(orderTableDao.findAllByIdIn(List.of(this.치킨_주문_단체테이블.getId(), this.치킨2_주문_단체테이블.getId()))).thenReturn(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));
        when(tableGroupDao.save(any(TableGroup.class))).thenReturn(this.단체주문테이블);
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(null);

        TableGroup 신규_단체지정 = new TableGroup();
        신규_단체지정.setOrderTables(null);
        
        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.create(신규_단체지정));
    }

    @DisplayName("주문테이블의 개수가 2개 미만으로 단체지정시 예외가 발생된다.")
    @Test
    void exception_createTableGoup_underTwoCountOrderTable() {
        // given
        when(orderTableDao.findAllByIdIn(List.of(this.치킨_주문_단체테이블.getId(), this.치킨2_주문_단체테이블.getId()))).thenReturn(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));
        when(tableGroupDao.save(any(TableGroup.class))).thenReturn(this.단체주문테이블);
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(null);

        TableGroup 신규_단체지정 = new TableGroup();
        신규_단체지정.setOrderTables(List.of(this.치킨_주문_단체테이블));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.create(신규_단체지정));
    }

    @DisplayName("미존재 주문테이블가 포함된 단체지정으로 저장시 예외가 발생된다.")
    @Test
    void exception_createTableGoup_containNotExistOrderTable() {
        미존재_주문테이블포함_조회_DB내용();

        // given
        TableGroup 신규_단체지정 = new TableGroup();
        신규_단체지정.setOrderTables(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));
        
        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.create(신규_단체지정));
    }

    private void 미존재_주문테이블포함_조회_DB내용() {
        when(orderTableDao.findAllByIdIn(List.of(this.치킨_주문_단체테이블.getId(), this.치킨2_주문_단체테이블.getId()))).thenReturn(List.of(this.치킨_주문_단체테이블));
    }


    @DisplayName("단체지정이 될 주문테이블 다른 단체지정에 등록된 경우 예외가 발생된다.")
    @Test
    void exception_createTableGoup_existOrderTableInOtherTableGroup() {
        // given
        TableGroup 신규_단체지정 = new TableGroup();
        this.치킨_주문_단체테이블.setTableGroupId(this.단체주문테이블.getId());
        신규_단체지정.setOrderTables(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));
        
        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.create(신규_단체지정));
    }

    @DisplayName("단체지정이 해제된다.")
    @Test
    void update_tableUnGroup() {
        단체지정_해제전_DB내용();

        // when
        tableGroupService.ungroup(this.단체주문테이블.getId());

        // then
        verify(orderTableDao, atLeast(1)).save(any(OrderTable.class));
    }

    private void 단체지정_해제전_DB내용() {
        when(orderTableDao.findAllByTableGroupId(this.단체주문테이블.getId())).thenReturn(this.단체주문테이블.getOrderTables());
    }

    @DisplayName("주문테이블의 주문상태가 계산 단계가 아닐때 단체지정이 해제시 예외가 발생된다.")
    @Test
    void exception_updateTableUnGroup_notCompletionOrderStatus() {
        // given
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(),anyList())).thenReturn(true);
        단체지정_해제전_DB내용();

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableGroupService.ungroup(this.단체주문테이블.getId()));
    }
}
