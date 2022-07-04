package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kitchenpos.table.domain.fixture.OrderTableFixtureFactory;
import kitchenpos.table.domain.fixture.TableGroupFixtureFactory;
import kitchenpos.table.exception.CannotUngroupException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableUngroupDomainServiceTest {
    @Mock
    private TableOrderStatusChecker tableOrderStatusChecker;

    @Mock
    private TableGroupRepository tableGroupRepository;

    private TableUngroupDomainService tableUngroupDomainService;

    @BeforeEach
    void setUp() {
        tableUngroupDomainService = new TableUngroupDomainService(tableOrderStatusChecker, tableGroupRepository);
    }

    @Test
    @DisplayName("모든 주문 테이블이 계산완료된 경우 테이블 그룹 해제 성공")
    void 테이블그룹_지정해제_계산완료시() {
        Long tableGroupId = 1L;
        OrderTable emptyTable = OrderTableFixtureFactory.createEmptyTableOrderWithId(1L);
        OrderTable emptyTable2 = OrderTableFixtureFactory.createEmptyTableOrderWithId(2L);
        TableGroup tableGroup = TableGroupFixtureFactory.createTableGroup(emptyTable, emptyTable2);
        when(tableGroupRepository.findById(tableGroupId)).thenReturn(Optional.of(tableGroup));
        when(tableOrderStatusChecker.isExistTablesBeforeBillingStatus(
                Lists.newArrayList(emptyTable.getId(), emptyTable2.getId())))
                .thenReturn(false);

        tableUngroupDomainService.ungroup(tableGroupId);

        Assertions.assertAll("테이블 그룹에서 지정 해제되었는지 확인"
                , () -> assertThat(emptyTable.getTableGroup()).isNull()
                , () -> assertThat(emptyTable.isEmpty()).isFalse()
                , () -> assertThat(emptyTable2.getTableGroup()).isNull()
                , () -> assertThat(emptyTable2.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("계산완료되지 않은 주문 테이블이 있는 경우 테이블 그룹 해제 실패")
    void 테이블그룹_지정해제_계산완료전() {
        Long tableGroupId = 1L;
        OrderTable emptyTable = OrderTableFixtureFactory.createEmptyTableOrderWithId(1L);
        OrderTable emptyTable2 = OrderTableFixtureFactory.createEmptyTableOrderWithId(2L);
        TableGroup tableGroup = TableGroupFixtureFactory.createTableGroup(emptyTable, emptyTable2);
        when(tableGroupRepository.findById(tableGroupId)).thenReturn(Optional.of(tableGroup));
        when(tableOrderStatusChecker.isExistTablesBeforeBillingStatus(
                Lists.newArrayList(emptyTable.getId(), emptyTable2.getId())))
                .thenReturn(true);

        assertThatThrownBy(() -> tableUngroupDomainService.ungroup(tableGroupId))
                .isInstanceOf(CannotUngroupException.class);
    }
}
