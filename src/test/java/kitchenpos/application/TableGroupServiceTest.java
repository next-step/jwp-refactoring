package kitchenpos.application;

import static kitchenpos.application.fixture.OrderTableFixture.빈_테이블;
import static kitchenpos.application.fixture.TableGroupFixture.단체지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.application.TableGroupService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 테이블 관리 기능")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("`단체 지정`은 등록 할 수 있다.")
    void 단체지정_등록() {
        // given
        TableGroup 단체지정 = 단체지정(Arrays.asList(빈_테이블(), 빈_테이블()));
        TableGroupRequest 요청_파라미터 = new TableGroupRequest(Arrays.asList(1L, 2L));
        given(orderTableRepository.findAllById(any())).willReturn(Arrays.asList(빈_테이블(), 빈_테이블()));
        given(tableGroupRepository.save(any())).willReturn(단체지정);

        // when
        TableGroupResponse 등록된_단체지정 = tableGroupService.create(요청_파라미터);

        // then
        assertThat(등록된_단체지정).isNotNull();
    }

    @Test
    @DisplayName("`단체 지정` 해지는 `주문 테이블`에서 `단체 지정`번호를 없앤다.")
    void 단체지정_해지() {
        // given
        OrderTables 주문테이블_목록 = OrderTables.of(Arrays.asList(빈_테이블(), 빈_테이블()));
        TableGroup 단체지정 = 단체지정(주문테이블_목록.getOrderTables());

        given(tableGroupRepository.findById(any())).willReturn(Optional.of(단체지정));
        given(orderRepository.findAllByOrderTableIn(any())).willReturn(Collections.emptyList());

        // when
        tableGroupService.ungroup(1L);

        // then
        assertThat(단체지정.getOrderTables()).extracting("tableGroupId").containsExactly(null, null);
    }

    @Test
    @DisplayName("`단체 지정`에 속할 `주문 테이블`은 모두 등록되어 있어야 한다.")
    void 미등록_주문테이블_실패() {
        // given
        TableGroupRequest 요청_파라미터 = new TableGroupRequest(Arrays.asList(1L, 2L));
        given(orderTableRepository.findAllById(any())).willReturn(Collections.emptyList());

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupService.create(요청_파라미터);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }
}
