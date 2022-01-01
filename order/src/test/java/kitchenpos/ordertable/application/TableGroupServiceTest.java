package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.domain.Validator;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroupRepository;
import kitchenpos.ordertable.dto.TableGroupRequest;
import kitchenpos.ordertable.dto.TableGroupResponse;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private Validator<TableGroup> validator;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("주문 테이블들을 단체 지정할 수 있다.")
    @Test
    void group() {
        // given
        final TableGroup expected = TableGroupFixture.of(1L);
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(expected);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(
            Arrays.asList(
                OrderTableFixture.of(1L),
                OrderTableFixture.of(2L)
            )
        );

        final TableGroupRequest request = TableGroupFixture.ofRequest(Arrays.asList(1L, 2L));

        // when
        final TableGroupResponse response = tableGroupService.group(request);

        // then
        assertAll(
            () -> assertThat(response.getId()).isNotNull(),
            () -> assertThat(response.getOrderTables().size()).isEqualTo(2)
        );
    }

    @DisplayName("주문 테이블 단체 지정을 해체할 수 있다.")
    @Test
    void ungroup() {
        // given
        final TableGroup expected = TableGroupFixture.of(1L);
        final OrderTables tables = OrderTableFixture.ofList(
            OrderTableFixture.of(1L),
            OrderTableFixture.of(2L)
        );
        expected.group(tables);

        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.of(expected));

        // when
        final ThrowableAssert.ThrowingCallable response = () ->
            tableGroupService.ungroup(expected.getId());

        // then
        assertThatNoException().isThrownBy(response);
    }
}
