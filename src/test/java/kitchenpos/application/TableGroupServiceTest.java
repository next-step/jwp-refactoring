package kitchenpos.application;

import static kitchenpos.application.fixture.OrderTableFixture.주문테이블_생성;
import static kitchenpos.application.fixture.TableGroupFixture.단체지정생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("`단체 지정`은 등록 할 수 있다.")
    void create() {
        // given
        OrderTable 주문테이블_1 = 주문테이블_생성(1L);
        OrderTable 주문테이블_2 = 주문테이블_생성(2L);
        List<OrderTable> 주문테이블목록 = Arrays.asList(주문테이블_1, 주문테이블_2);
        TableGroup 단체지정 = 단체지정생성(1L, 주문테이블목록);

        given(orderTableDao.findAllByIdIn(any())).willReturn(주문테이블목록);
        given(tableGroupDao.save(단체지정)).willReturn(단체지정);

        // when
        TableGroup 등록된_단체지정 = tableGroupService.create(단체지정);

        // then
        assertThat(등록된_단체지정).isNotNull();
    }

    @Test
    @DisplayName("`단체 지정`은 `주문 테이블`이 최소 2개이상 이어야 등록 할 수 있다.")
    void create_fail1() {
        // given
        OrderTable 주문테이블 = 주문테이블_생성(1L);
        List<OrderTable> 주문테이블목록 = Collections.singletonList(주문테이블);
        TableGroup 단체지정 = 단체지정생성(1L, 주문테이블목록);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupService.create(단체지정);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`단체 지정` 등록에 속한 `주문 테이블`은 모두 `빈 테이블` 아닌 상태로 변경 된다.")
    void create_fail2() {
        // given
        OrderTable 주문테이블_1 = 주문테이블_생성(1L);
        OrderTable 주문테이블_2 = 주문테이블_생성(2L);
        주문테이블_2.setEmpty(false);

        List<OrderTable> 주문테이블목록 = Arrays.asList(주문테이블_1, 주문테이블_2);
        TableGroup 단체지정 = 단체지정생성(1L, 주문테이블목록);

        given(orderTableDao.findAllByIdIn(any())).willReturn(주문테이블목록);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupService.create(단체지정);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`단체 지정`은 `주문 테이블`이 다른 `단체 지정`에 속해있지 않아야 등록 할 수 있다.")
    void create_fail3() {
        // given
        OrderTable 주문테이블_1 = 주문테이블_생성(1L);
        OrderTable 주문테이블_2 = 주문테이블_생성(2L);
        주문테이블_2.setTableGroupId(1L);

        List<OrderTable> 주문테이블목록 = Arrays.asList(주문테이블_1, 주문테이블_2);
        TableGroup 단체지정 = 단체지정생성(1L, 주문테이블목록);

        given(orderTableDao.findAllByIdIn(any())).willReturn(주문테이블목록);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupService.create(단체지정);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`단체 지정`에 속할 `주문 테이블`은 모두 등록되어 있어야 한다.")
    void create_fail4() {
        // given
        OrderTable 주문테이블_1 = 주문테이블_생성(1L);
        OrderTable 주문테이블_2 = 주문테이블_생성(2L);

        List<OrderTable> 주문테이블목록 = Arrays.asList(주문테이블_1, 주문테이블_2);
        TableGroup 단체지정 = 단체지정생성(1L, 주문테이블목록);

        given(orderTableDao.findAllByIdIn(any())).willReturn(Collections.emptyList());

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupService.create(단체지정);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`단체 지정` 해지는 `주문 테이블`에서 `단체 지정`번호를 없앤다.")
    void ungroup1() {
        // given
        Long 단체지정번호 = 1L;
        OrderTable 주문테이블_1 = 주문테이블_생성(1L);
        OrderTable 주문테이블_2 = 주문테이블_생성(2L);

        주문테이블_1.setTableGroupId(단체지정번호);
        주문테이블_2.setTableGroupId(단체지정번호);
        List<OrderTable> 주문테이블목록 = Arrays.asList(주문테이블_1, 주문테이블_2);

        // when
        assertThat(주문테이블목록).extracting("tableGroupId").containsExactly(단체지정번호, 단체지정번호);

        // given
        given(orderTableDao.findAllByTableGroupId(단체지정번호)).willReturn(주문테이블목록);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        // when
        tableGroupService.ungroup(단체지정번호);
        assertThat(주문테이블목록).extracting("tableGroupId").containsExactly(null, null);
    }

    @Test
    @DisplayName("`단체 지정`에 속한 `주문 테이블`들의 `주문상태`가 `조리`,`식사`인 경우 해지 할 수 없다. ")
    void ungroup2() {
        // given
        Long 단체지정번호 = 1L;
        OrderTable 주문테이블_1 = 주문테이블_생성(1L);
        OrderTable 주문테이블_2 = 주문테이블_생성(2L);
        List<OrderTable> 주문테이블목록 = Arrays.asList(주문테이블_1, 주문테이블_2);

        given(orderTableDao.findAllByTableGroupId(단체지정번호)).willReturn(주문테이블목록);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupService.ungroup(단체지정번호);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }
}