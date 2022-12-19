package kitchenpos.application;


import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static java.util.Collections.singletonList;
import static kitchenpos.domain.OrderTableTestFixture.createOrderTable;
import static kitchenpos.domain.TableGroupTestFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("단체 지정 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private TableGroup 단체1;

    @BeforeEach
    void setUp() {
        주문테이블1 = createOrderTable(1L, null, 5, true);
        주문테이블2 = createOrderTable(2L, null, 4, true);
        단체1 = createTableGroup(1L, null, Arrays.asList(주문테이블1, 주문테이블2));
    }

    @DisplayName("주문 테이블들에 대해 단체를 설정한다.")
    @Test
    void 단체_생성() {
        // given
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()))).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(tableGroupDao.save(단체1)).thenReturn(단체1);
        when(orderTableDao.save(주문테이블1)).thenReturn(주문테이블1);
        when(orderTableDao.save(주문테이블2)).thenReturn(주문테이블2);
        // when
        TableGroup saveTableGroup = tableGroupService.create(단체1);
        // then
        assertAll(
                () -> assertThat(주문테이블1.getTableGroupId()).isEqualTo(saveTableGroup.getId()),
                () -> assertThat(주문테이블2.getTableGroupId()).isEqualTo(saveTableGroup.getId()),
                () -> assertThat(주문테이블1.isEmpty()).isFalse(),
                () -> assertThat(주문테이블2.isEmpty()).isFalse(),
                () -> assertThat(saveTableGroup.getCreatedDate()).isNotNull()
        );
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void 단체_지정_해제() {
        // given
        when(orderTableDao.findAllByTableGroupId(단체1.getId())).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(주문테이블1)).thenReturn(주문테이블1);
        when(orderTableDao.save(주문테이블2)).thenReturn(주문테이블2);
        // when
        tableGroupService.ungroup(단체1.getId());
        // then
        assertAll(
                () -> assertThat(주문테이블1.getTableGroupId()).isNull(),
                () -> assertThat(주문테이블2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("생성하고자 하는 단체에 속하는 주문 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void 단체_생성_예외1() {
        // given
        TableGroup tableGroup = createTableGroup(2L, null, singletonList(주문테이블1));
        // when & then
        Assertions.assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문 테이블을 가진 단체는 생성할 수 없다.")
    @Test
    void 단체_생성_예외2() {
        // given
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()))).thenReturn(singletonList(주문테이블1));
        // when & then
        Assertions.assertThatThrownBy(
                () -> tableGroupService.create(단체1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 생성 시, 해당 단체에 속할 주문 테이블들 중 빈 테이블이 아닌 테이블이 있으면 예외가 발생한다.")
    @Test
    void 단체_생성_예외3() {
        // given
        OrderTable orderTable = createOrderTable(null, null, 5, false);
        TableGroup tableGroup = createTableGroup(3L, null, Arrays.asList(orderTable, 주문테이블1));
        when(orderTableDao.findAllByIdIn(Arrays.asList(orderTable.getId(), 주문테이블1.getId()))).thenReturn(
                Arrays.asList(orderTable, 주문테이블1));

        // when & then
        Assertions.assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 생성 시, 다른 단체에 포함된 주문 테이블이 있다면 예외가 발생한다.")
    @Test
    void 단체_생성_예외4() {
        // given
        OrderTable orderTable = createOrderTable(null, 1L, 5, true);
        TableGroup tableGroup = createTableGroup(3L, null, Arrays.asList(orderTable, 주문테이블1));
        when(orderTableDao.findAllByIdIn(Arrays.asList(orderTable.getId(), 주문테이블1.getId()))).thenReturn(
                Arrays.asList(orderTable, 주문테이블1));
        // when & then
        Assertions.assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("단체 내 주문 테이블의 상태가 조리중이거나 식사중이면 단체 지정을 해제할 수 없다.")
    @Test
    void upGroupThrowErrorWhenOrderTableStatusIsCookingOrMeal() {
        // given
        when(orderTableDao.findAllByTableGroupId(단체1.getId())).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);
        // when & then
        Assertions.assertThatThrownBy(
                () -> tableGroupService.ungroup(단체1.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
