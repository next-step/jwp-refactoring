package kitchenpos.application.tablegroup;

import kitchenpos.application.tablegroup.TableGroupService;
import kitchenpos.order.validator.OrderTableGroupUnGroupValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.validator.OrderTableTableGroupCreateValidator;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.application.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.application.tablegroup.dto.TableGroupResponse;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.OrderTableFixture.getOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@DisplayName("단체지정 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderTableTableGroupCreateValidator orderTableCreateTableGroupValidator;
    @Mock
    private OrderTableGroupUnGroupValidator orderUnGroupTableGroupValidator;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @InjectMocks
    private TableGroupService tableGroupService;


    @DisplayName("주문 테이블 목록을 통해 단체 지정을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        final List<Long> orderTableIds = Arrays.asList(1L, 2L);
        final TableGroupCreateRequest createRequest = getCreateRequest(orderTableIds);
        final TableGroup expectedTableGroup = getTableGroup(1L, orderTableIds);

        doNothing().when(orderTableCreateTableGroupValidator).validate(anyList());
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(expectedTableGroup);

        // when
        TableGroupResponse tableGroup = tableGroupService.create(createRequest);
        // then
        assertThat(tableGroup).isEqualTo(TableGroupResponse.of(expectedTableGroup));
    }

    @DisplayName("단체 지정을 못하는 경우")
    @Nested
    class CreateFail {

        @DisplayName("주문 테이블은 2개 미만 인경우")
        @Test
        void createByTwoLessOrderTable() {
            // given
            final List<Long> orderTableIds = Collections.singletonList(1L);
            final TableGroupCreateRequest createRequest = getCreateRequest(orderTableIds);
            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableGroupService.create(createRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블 목록 각각의 아이디가 유효하지 않을 경우")
        @Test
        void createByNotExistOrderTable() {
            // given
            final List<Long> orderTableIds = Arrays.asList(1L, 2L);
            final TableGroupCreateRequest createRequest = getCreateRequest(orderTableIds);

            doThrow(new IllegalArgumentException()).when(orderTableCreateTableGroupValidator).validate(anyList());

            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableGroupService.create(createRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("단체 지정의 아이디를 통해서 단체 지정을 해제 할 수 있다.")
    @Test
    void ungroup() {
        // given
        OrderTable firstOrderTable = getOrderTable(1L, false, 4);
        OrderTable secondOrderTable = getOrderTable(2L, false, 4);
        final List<Long> orderTableIds = Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId());
        TableGroup tableGroup = getTableGroup(1L, orderTableIds);

        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));
        doNothing().when(orderUnGroupTableGroupValidator).validate(anyList());
        // when
        final Executable executable = () -> tableGroupService.ungroup(tableGroup.getId());
        // then
        assertDoesNotThrow(executable);
    }


    @DisplayName("주문 테이블이 조리나 식사 상태일 경우 해제 할 수 없다.")
    @Test
    void ungroupFail() {
        // given
        OrderTable firstOrderTable = getOrderTable(1L, false, 4);
        OrderTable secondOrderTable = getOrderTable(2L, false, 4);
        final List<Long> orderTableIds = Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId());
        TableGroup tableGroup = getTableGroup(1L, orderTableIds);

        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));
        doThrow(new IllegalArgumentException()).when(orderUnGroupTableGroupValidator).validate(anyList());

        // when
        ThrowableAssert.ThrowingCallable callable = () -> tableGroupService.ungroup(tableGroup.getId());
        // then
        assertThatThrownBy(callable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroupCreateRequest getCreateRequest(List<Long> orderTableIds) {
        return TableGroupCreateRequest.of(orderTableIds);
    }

    private TableGroup getTableGroup(Long id, List<Long> orderTableIds) {
        return TableGroup.generate(id, orderTableIds);
    }
}
