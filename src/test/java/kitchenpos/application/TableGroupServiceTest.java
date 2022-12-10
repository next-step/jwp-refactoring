package kitchenpos.application;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static kitchenpos.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.domain.TableGroupTestFixture.generateTableGroup;
import static kitchenpos.domain.TableGroupTestFixture.generateTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
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
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;
    private OrderTable 주문테이블C;
    private OrderTable 주문테이블D;

    @BeforeEach
    void setUp() {
        주문테이블A = generateOrderTable(1L, null, 5, true);
        주문테이블B = generateOrderTable(2L, null, 4, true);
        주문테이블C = generateOrderTable(3L, null, 5, true);
        주문테이블D = generateOrderTable(4L, null, 4, true);
    }

    @DisplayName("주문 테이블들에 대해 단체를 설정한다.")
    @Test
    void createTableGroup() {
        // given
        TableGroupRequest tableGroupRequest = generateTableGroupRequest(Arrays.asList(주문테이블C.getId(), 주문테이블D.getId()));
        TableGroup 단체 = generateTableGroup(1L, Arrays.asList(주문테이블C, 주문테이블D));
        given(orderTableRepository.findById(주문테이블C.getId())).willReturn(Optional.of(주문테이블A));
        given(orderTableRepository.findById(주문테이블D.getId())).willReturn(Optional.of(주문테이블B));
        given(tableGroupRepository.save(any())).willReturn(단체);

        // when
        TableGroupResponse saveTableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertAll(
                () -> assertThat(주문테이블C.getTableGroup().getId()).isEqualTo(saveTableGroup.getId()),
                () -> assertThat(주문테이블D.getTableGroup().getId()).isEqualTo(saveTableGroup.getId()),
                () -> assertThat(주문테이블C.isEmpty()).isFalse(),
                () -> assertThat(주문테이블D.isEmpty()).isFalse(),
                () -> assertThat(saveTableGroup.getCreatedDate()).isNotNull()
        );
    }

    @DisplayName("생성하고자 하는 단체에 속하는 주문 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void createTableGroupThrowErrorWhenOrderTableSizeIsSmallerThenTwo() {
        // given
        TableGroupRequest tableGroupRequest = generateTableGroupRequest(singletonList(주문테이블A.getId()));
        given(orderTableRepository.findById(주문테이블A.getId())).willReturn(Optional.of(주문테이블A));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage(ErrorCode.주문_테이블은_2개_이상여야함.getErrorMessage());
    }

    @DisplayName("등록되지 않은 주문 테이블을 가진 단체는 생성할 수 없다.")
    @Test
    void createTableGroupThrowErrorWhenOrderTableIsNotExists() {
        // given
        TableGroupRequest tableGroupRequest = generateTableGroupRequest(Arrays.asList(주문테이블A.getId(), 주문테이블B.getId()));
        given(orderTableRepository.findById(주문테이블A.getId())).willReturn(Optional.of(주문테이블A));
        given(orderTableRepository.findById(주문테이블B.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage(ErrorCode.존재하지_않는_주문_테이블.getErrorMessage());
    }

    @DisplayName("단체 생성 시, 해당 단체에 속할 주문 테이블이 없으면 예외가 발생한다.")
    @Test
    void createTableGroupThrowErrorWhenOrderTableIsNotEmpty() {
        // given
        TableGroupRequest tableGroupRequest = generateTableGroupRequest(emptyList());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage(ErrorCode.주문_테이블_집합은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("단체 생성 시, 다른 단체에 포함된 주문 테이블이 있다면 예외가 발생한다.")
    @Test
    void createTableGroupThrowErrorWhenOrderTableInOtherTableGroup() {
        // given
        generateTableGroup(1L, Arrays.asList(주문테이블C, 주문테이블D));
        TableGroupRequest tableGroupRequest = generateTableGroupRequest(Arrays.asList(주문테이블B.getId(), 주문테이블C.getId()));
        given(orderTableRepository.findById(주문테이블B.getId())).willReturn(Optional.of(주문테이블B));
        given(orderTableRepository.findById(주문테이블C.getId())).willReturn(Optional.of(주문테이블C));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage(ErrorCode.주문_테이블에_이미_단체_그룹_지정됨.getErrorMessage());
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        TableGroup 단체 = generateTableGroup(Arrays.asList(주문테이블A, 주문테이블B));
        given(tableGroupRepository.findById(단체.getId())).willReturn(Optional.of(단체));
        given(orderTableRepository.findAllByTableGroupId(단체.getId())).willReturn(Arrays.asList(주문테이블A, 주문테이블B));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(주문테이블A.getId(), 주문테이블B.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        // when
        tableGroupService.ungroup(단체.getId());

        // then
        assertAll(
                () -> assertThat(주문테이블A.getTableGroup()).isNull(),
                () -> assertThat(주문테이블B.getTableGroup()).isNull()
        );
    }

    @DisplayName("단체 내 주문 테이블의 상태가 조리중이거나 식사중이면 단체 지정을 해제할 수 없다.")
    @Test
    void upGroupThrowErrorWhenOrderTableStatusIsCookingOrMeal() {
        // given
        TableGroup 단체 = generateTableGroup(Arrays.asList(주문테이블A, 주문테이블B));
        given(tableGroupRepository.findById(단체.getId())).willReturn(Optional.of(단체));
        given(orderTableRepository.findAllByTableGroupId(단체.getId())).willReturn(Arrays.asList(주문테이블A, 주문테이블B));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(주문테이블A.getId(), 주문테이블B.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(단체.getId()));
    }
}
