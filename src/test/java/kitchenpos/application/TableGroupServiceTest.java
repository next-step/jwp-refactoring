package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.repository.TableGroupRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private TableService tableService;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private TableGroup tableGroup;
    @Mock
    private OrderTable orderTable;
    @Mock
    private OrderTable orderTable2;
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(tableService, tableGroupRepository);
    }

    @Test
    void 단체_지정을_등록할_수_있다() {
        TableGroup tableGroup = new TableGroup();
        given(tableService.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable, orderTable2));
        given(orderTable.isEmpty()).willReturn(true);
        given(orderTable2.isEmpty()).willReturn(true);
        given(orderTable.getTableGroup()).willReturn(null);
        given(orderTable2.getTableGroup()).willReturn(null);
        given(tableGroupRepository.save(any())).willReturn(tableGroup);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new OrderTableRequest(1L, 1, true), new OrderTableRequest(2L, 1, true)));

        TableGroup createTableGroup = tableGroupService.create(tableGroupRequest);

        assertThat(createTableGroup).isEqualTo(tableGroup);
    }

    @Test
    void 두개_이상의_주문_테이블만_단체_지정이_가능하다() {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Collections.singletonList(new OrderTableRequest(1L, 1, true)));

        ThrowingCallable 두개_미만의_주문_테이블_단체_지정 = () -> tableGroupService.create(tableGroupRequest);

        assertThatIllegalArgumentException().isThrownBy(두개_미만의_주문_테이블_단체_지정);
    }

    @Test
    void 주문_테이블은_필수로_지정해야_한다() {
        TableGroupRequest 주문_테이블을_지정하지_않은_경우 = new TableGroupRequest();

        ThrowingCallable 주문_테이블을_지정하지_않은_단체_지정 = () -> tableGroupService.create(주문_테이블을_지정하지_않은_경우);

        assertThatIllegalArgumentException().isThrownBy(주문_테이블을_지정하지_않은_단체_지정);
    }

    @Test
    void 등록_된_주문_테이블만_단체_지정이_가능하다() {
        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        given(tableService.findAllByIdIn(any())).willThrow(IllegalArgumentException.class);

        ThrowingCallable 등록되지_않은_주문_테이블_단체_지정 = () -> tableGroupService.create(tableGroupRequest);

        assertThatIllegalArgumentException().isThrownBy(등록되지_않은_주문_테이블_단체_지정);
    }

    @Test
    void 빈_테이블이_아닌_주문_테이블은_단체_지정이_불가능하다() {
        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        given(tableService.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable, orderTable2));
        given(orderTable.isEmpty()).willReturn(false);

        ThrowingCallable 빈_테이블_단체지정 = () -> tableGroupService.create(tableGroupRequest);

        assertThatIllegalArgumentException().isThrownBy(빈_테이블_단체지정);
    }

    @Test
    void 이미_단체_지정이_된_주문_테이블은_단체_지정이_불가능하다() {
        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        TableGroup tableGroup = new TableGroup();
        given(tableService.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable, orderTable2));
        given(orderTable.isEmpty()).willReturn(true);
        given(orderTable.getTableGroup()).willReturn(tableGroup);

        ThrowingCallable 이미_단체_지정이_된_주문_테이블_단체지정 = () -> tableGroupService.create(tableGroupRequest);

        assertThatIllegalArgumentException().isThrownBy(이미_단체_지정이_된_주문_테이블_단체지정);
    }

    @Test
    void 단체_지정을_해제할_수_있다() {
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));

        ThrowingCallable 단체_지정을_해제할_수_있다 = () -> tableGroupService.ungroup(1L);

        assertThatNoException().isThrownBy(단체_지정을_해제할_수_있다);
    }

    @Test
    void 주문_테이블에_조리_식사_상태가_포함된_주문이_있을경우_해제가_불가능하다() {
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));
        given(tableGroup.validateOrderStatus(any())).willThrow(IllegalArgumentException.class);
        ThrowingCallable 주문_테이블에_조리_식사_상태가_포함된_주문이_있을경우 = () -> tableGroupService.ungroup(1L);

        assertThatIllegalArgumentException().isThrownBy(주문_테이블에_조리_식사_상태가_포함된_주문이_있을경우);
    }
}
