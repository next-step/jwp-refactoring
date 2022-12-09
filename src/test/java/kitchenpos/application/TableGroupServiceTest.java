package kitchenpos.application;

import static java.util.Collections.singletonList;
import static kitchenpos.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.domain.TableGroupTestFixture.generateTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;
    private TableGroup 단체A;

    @BeforeEach
    void setUp() {
        주문테이블A = generateOrderTable(1L, null, 5, true);
        주문테이블B = generateOrderTable(2L, null, 4, true);
        단체A = generateTableGroup(1L, null, Arrays.asList(주문테이블A, 주문테이블B));
    }

    @DisplayName("주문 테이블들에 대해 단체를 설정한다.")
    @Test
    void createTableGroup() {
        // given
        given(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블A.getId(), 주문테이블B.getId()))).willReturn(Arrays.asList(주문테이블A, 주문테이블B));
        given(tableGroupDao.save(단체A)).willReturn(단체A);
        given(orderTableDao.save(주문테이블A)).willReturn(주문테이블A);
        given(orderTableDao.save(주문테이블B)).willReturn(주문테이블B);

        // when
        TableGroup saveTableGroup = tableGroupService.create(단체A);

        // then
        assertAll(
                () -> assertThat(주문테이블A.getTableGroupId()).isEqualTo(saveTableGroup.getId()),
                () -> assertThat(주문테이블B.getTableGroupId()).isEqualTo(saveTableGroup.getId()),
                () -> assertThat(주문테이블A.isEmpty()).isFalse(),
                () -> assertThat(주문테이블B.isEmpty()).isFalse(),
                () -> assertThat(saveTableGroup.getCreatedDate()).isNotNull()
        );
    }

    @DisplayName("생성하고자 하는 단체에 속하는 주문 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void createTableGroupThrowErrorWhenOrderTableSizeIsSmallerThenTwo() {
        // given
        TableGroup tableGroup = generateTableGroup(2L, null, singletonList(주문테이블A));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("등록되지 않은 주문 테이블을 가진 단체는 생성할 수 없다.")
    @Test
    void createTableGroupThrowErrorWhenOrderTableIsNotExists() {
        // given
        given(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블A.getId(), 주문테이블B.getId()))).willReturn(singletonList(주문테이블A));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(단체A));
    }

    @DisplayName("단체 생성 시, 해당 단체에 속할 주문 테이블들 중 빈 테이블이 아닌 테이블이 있으면 예외가 발생한다.")
    @Test
    void createTableGroupThrowErrorWhenOrderTableIsNotEmpty() {
        // given
        OrderTable orderTable = generateOrderTable(null, 5, false);
        TableGroup tableGroup = generateTableGroup(3L, null, Arrays.asList(orderTable, 주문테이블A));
        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable.getId(), 주문테이블A.getId()))).willReturn(
                Arrays.asList(orderTable, 주문테이블A));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체 생성 시, 다른 단체에 포함된 주문 테이블이 있다면 예외가 발생한다.")
    @Test
    void createTableGroupThrowErrorWhenOrderTableInOtherTableGroup() {
        // given
        OrderTable orderTable = generateOrderTable(1L, 5, true);
        TableGroup tableGroup = generateTableGroup(3L, null, Arrays.asList(orderTable, 주문테이블A));
        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable.getId(), 주문테이블A.getId()))).willReturn(
                Arrays.asList(orderTable, 주문테이블A));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        given(orderTableDao.findAllByTableGroupId(단체A.getId())).willReturn(Arrays.asList(주문테이블A, 주문테이블B));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(주문테이블A.getId(), 주문테이블B.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(주문테이블A)).willReturn(주문테이블A);
        given(orderTableDao.save(주문테이블B)).willReturn(주문테이블B);

        // when
        tableGroupService.ungroup(단체A.getId());

        // then
        assertAll(
                () -> assertThat(주문테이블A.getTableGroupId()).isNull(),
                () -> assertThat(주문테이블B.getTableGroupId()).isNull()
        );
    }

    @DisplayName("단체 내 주문 테이블의 상태가 조리중이거나 식사중이면 단체 지정을 해제할 수 없다.")
    @Test
    void upGroupThrowErrorWhenOrderTableStatusIsCookingOrMeal() {
        // given
        given(orderTableDao.findAllByTableGroupId(단체A.getId())).willReturn(Arrays.asList(주문테이블A, 주문테이블B));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(주문테이블A.getId(), 주문테이블B.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(단체A.getId()));
    }
}
