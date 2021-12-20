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
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
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

    private OrderTable 빈_주문테이블_1;
    private OrderTable 빈_주문테이블_2;
    private List<OrderTable> 빈_테이블목록;
    private List<OrderTable> 손님있는_테이블목록;
    private TableGroup 단체지정_테이블;
    private Long 단체지정_번호 = 1L;


    @BeforeEach
    void setUp() {
        // given
        빈_주문테이블_1 = 주문테이블_생성(1L, null, true, 1);
        빈_주문테이블_2 = 주문테이블_생성(2L, null, true, 1);

        빈_테이블목록 = Arrays.asList(빈_주문테이블_1, 빈_주문테이블_2);
        손님있는_테이블목록 = Arrays.asList(
            주문테이블_생성(1L, 단체지정_번호, false, 1),
            주문테이블_생성(2L, 단체지정_번호, false, 1));

        단체지정_테이블 = 단체지정생성(단체지정_번호, 빈_테이블목록);
    }


    @Test
    @DisplayName("`단체 지정`은 등록 할 수 있다.")
    void 단체지정_등록() {
        // given
        given(orderTableDao.findAllByIdIn(any())).willReturn(빈_테이블목록);
        given(tableGroupDao.save(단체지정_테이블)).willReturn(단체지정_테이블);

        // when
        TableGroup 등록된_단체지정 = tableGroupService.create(단체지정_테이블);

        // then
        assertThat(등록된_단체지정).isNotNull();
    }

    @Test
    @DisplayName("`단체 지정` 해지는 `주문 테이블`에서 `단체 지정`번호를 없앤다.")
    void 단체지정_해지() {
        // then
        assertThat(손님있는_테이블목록).extracting("tableGroupId").containsExactly(단체지정_번호, 단체지정_번호);

        // given
        given(orderTableDao.findAllByTableGroupId(단체지정_테이블.getId())).willReturn(손님있는_테이블목록);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(any(), any()),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        // when
        tableGroupService.ungroup(단체지정_테이블.getId());
        // then
        assertThat(단체지정_테이블.getOrderTables()).extracting("tableGroupId")
            .containsExactly(null, null);
    }

    @Test
    @DisplayName("`단체 지정` 등록에 속한 `주문 테이블`은 모두 `빈 테이블` 아닌 상태로 변경 된다.")
    void 단체지정_해지_빈테이블_변경_검증() {
        // given
        given(orderTableDao.findAllByIdIn(any())).willReturn(빈_테이블목록);
        given(tableGroupDao.save(단체지정_테이블)).willReturn(단체지정_테이블);

        // when
        TableGroup 등록된_단체지정 = tableGroupService.create(단체지정_테이블);

        // then
        assertThat(등록된_단체지정.getOrderTables()).extracting("empty").containsExactly(false, false);
    }

    @Test
    @DisplayName("`단체 지정`은 `주문 테이블`이 최소 2개이상 이어야 등록 할 수 있다.")
    void 주문테이블_한개_실패() {
        // given
        TableGroup 단체지정_한개테이블 = 단체지정생성(1L, Collections.singletonList(빈_주문테이블_1));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupService.create(단체지정_한개테이블);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`단체 지정`은 `주문 테이블`이 다른 `단체 지정`에 속해있지 않아야 등록 할 수 있다.")
    void 이미_지정된_주문테이블_실패() {
        // given
        given(orderTableDao.findAllByIdIn(any())).willReturn(손님있는_테이블목록);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupService.create(단체지정_테이블);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`단체 지정`에 속할 `주문 테이블`은 모두 등록되어 있어야 한다.")
    void 미등록_주문테이블_실패() {
        // given
        given(orderTableDao.findAllByIdIn(any())).willReturn(Collections.emptyList());

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupService.create(단체지정_테이블);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`단체 지정`에 속한 `주문 테이블`들의 `주문상태`가 `조리`,`식사`인 경우 해지 할 수 없다. ")
    void 해지할수_없는_주문상태_실패() {
        // given
        given(orderTableDao.findAllByTableGroupId(단체지정_테이블.getId())).willReturn(빈_테이블목록);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(any(), any()),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupService.ungroup(단체지정_테이블.getId());

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }
}
