package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @Mock
    private OrderTable orderTable;

    @Mock
    private OrderTable orderTable2;
    private OrderTable orderTable3;
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @Test
    void 단체_지정을_등록할_수_있다() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable, orderTable2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable, orderTable2));
        given(orderTable.isEmpty()).willReturn(true);
        given(orderTable.getTableGroupId()).willReturn(null);
        given(orderTable2.isEmpty()).willReturn(true);
        given(orderTable2.getTableGroupId()).willReturn(null);
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);

        TableGroup createTableGroup = tableGroupService.create(tableGroup);

        assertThat(createTableGroup).isEqualTo(tableGroup);
    }

    @Test
    void 두개_이상의_주문_테이블만_단체_지정이_가능하다() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Collections.singletonList(orderTable));

        ThrowingCallable 두개_미만의_주문_테이블_단체_지정 = () -> tableGroupService.create(tableGroup);

        assertThatIllegalArgumentException().isThrownBy(두개_미만의_주문_테이블_단체_지정);
    }

    @Test
    void 주문_테이블은_필수로_지정해야_한다() {
        TableGroup 주문_테이블을_지정하지_않은_경우 = new TableGroup(1L, LocalDateTime.now(), null);

        ThrowingCallable 주문_테이블을_지정하지_않은_단체_지정 = () -> tableGroupService.create(주문_테이블을_지정하지_않은_경우);

        assertThatIllegalArgumentException().isThrownBy(주문_테이블을_지정하지_않은_단체_지정);
    }

    @Test
    void 등록_된_주문_테이블만_단체_지정이_가능하다() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable, orderTable2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Collections.singletonList(orderTable3));

        ThrowingCallable 등록되지_않은_주문_테이블_단체_지정 = () -> tableGroupService.create(tableGroup);

        assertThatIllegalArgumentException().isThrownBy(등록되지_않은_주문_테이블_단체_지정);
    }

    @Test
    void 빈_테이블이_아닌_주문_테이블은_단체_지정이_불가능하다() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable, orderTable2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable, orderTable2));
        given(orderTable.isEmpty()).willReturn(false);

        ThrowingCallable 빈_테이블_단체지정 = () -> tableGroupService.create(tableGroup);

        assertThatIllegalArgumentException().isThrownBy(빈_테이블_단체지정);
    }

    @Test
    void 이미_단체_지정이_된_주문_테이블은_단체_지정이_불가능하다() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable, orderTable2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable, orderTable2));
        given(orderTable.isEmpty()).willReturn(true);
        given(orderTable.getTableGroupId()).willReturn(2L);

        ThrowingCallable 이미_단체_지정이_된_주문_테이블_단체지정 = () -> tableGroupService.create(tableGroup);

        assertThatIllegalArgumentException().isThrownBy(이미_단체_지정이_된_주문_테이블_단체지정);
    }

    @Test
    void 단체_지정을_해제할_수_있다() {
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(Collections.singletonList(orderTable));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        ThrowingCallable 단체_지정을_해제할_수_있다 = () -> tableGroupService.ungroup(1L);

        assertThatNoException().isThrownBy(단체_지정을_해제할_수_있다);
    }

    @Test
    void 주문_테이블에_조리_식사_상태가_포함된_주문이_있을경우_해제가_불가능하다() {
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(Collections.singletonList(orderTable));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        ThrowingCallable 주문_테이블에_조리_식사_상태가_포함된_주문이_있을경우 = () -> tableGroupService.ungroup(1L);

        assertThatIllegalArgumentException().isThrownBy(주문_테이블에_조리_식사_상태가_포함된_주문이_있을경우);
    }
}
