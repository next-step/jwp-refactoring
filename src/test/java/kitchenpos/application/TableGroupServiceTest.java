package kitchenpos.application;

import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있는_주문_테이블_생성;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있지_않은_주문_테이블_생성;
import static kitchenpos.utils.generator.TableGroupFixtureGenerator.테이블_그룹_생성;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.order.application.OrderTableOrderStatusValidatorImpl;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:TableGroup")
class TableGroupServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderTableOrderStatusValidatorImpl tableOrderStatusValidator;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 단체석_첫번째_주문_테이블, 단체석_두번째_주문_테이블;
    private List<OrderTable> 단체석_주문_테이블_목록;
    private List<Long> 단체적_주문_테이블_번호_목록;
    private TableGroup 단체석;

    @BeforeEach
    void setUp() {
        단체석_첫번째_주문_테이블 = 비어있는_주문_테이블_생성();
        단체석_두번째_주문_테이블 = 비어있는_주문_테이블_생성();

        단체석_주문_테이블_목록 = Arrays.asList(단체석_첫번째_주문_테이블, 단체석_두번째_주문_테이블);
        단체적_주문_테이블_번호_목록 = 단체석_주문_테이블_목록.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        단체석 = TableGroup.of(단체적_주문_테이블_번호_목록, 단체석_주문_테이블_목록);
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    public void creatTableGroup() {
        OrderTable 첫번째_빈좌석 = 비어있는_주문_테이블_생성();
        OrderTable 두번째_빈좌석 = 비어있는_주문_테이블_생성();
        List<OrderTable> 빈좌석_목록 = Arrays.asList(첫번째_빈좌석, 두번째_빈좌석);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(빈좌석_목록);
        given(tableGroupRepository.save(any(TableGroup.class))).will(AdditionalAnswers.returnsFirstArg());

        // When
        tableGroupService.create(테이블_그룹_생성(첫번째_빈좌석, 두번째_빈좌석));

        // Then
        verify(orderTableRepository).findAllByIdIn(anyList());
        verify(tableGroupRepository).save(any(TableGroup.class));
    }

    @ParameterizedTest(name = "case[{index}] : {0} => {1}")
    @MethodSource
    @DisplayName("그룹핑할 주문 테이블이 2개 미만인 경우 예외 발생 검증")
    public void throwException_WhenOrderTableSizeIsLessThanMinimumGroupingTargetSize(
        final List<OrderTable> givenOrderTables,
        final String givenDescription
    ) {
        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .as(givenDescription)
            .isThrownBy(() -> tableGroupService.create(테이블_그룹_생성(단체석_첫번째_주문_테이블, 단체석_두번째_주문_테이블)));
    }

    private static Stream<Arguments> throwException_WhenOrderTableSizeIsLessThanMinimumGroupingTargetSize() {
        return Stream.of(
            Arguments.of(Collections.emptyList(), "테이블 그룹이 0개인 경우"),
            Arguments.of(Collections.singletonList(비어있는_주문_테이블_생성()), "테이블 그룹이 1개인 경우")
        );
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블이 포함된 경우 예외 발생 검증")
    public void throwException_WhenOrderTableIsNotExist() {
        // Given
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Collections.emptyList());

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableGroupService.create(테이블_그룹_생성(단체석_첫번째_주문_테이블, 단체석_두번째_주문_테이블)));

        verify(orderTableRepository).findAllByIdIn(anyList());
    }

    @Test
    @DisplayName("비어있는 주문 테이블이 포함된 경우 예외 발생 검증")
    public void throwException_WhenContainsIsNotEmptyOrderTable() {
        // Given
        List<OrderTable> givenContainsNotEmptyOrderTables = Arrays
            .asList(비어있지_않은_주문_테이블_생성(), 비어있는_주문_테이블_생성());
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(givenContainsNotEmptyOrderTables);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableGroupService.create(테이블_그룹_생성(단체석_첫번째_주문_테이블, 단체석_두번째_주문_테이블)));

        verify(orderTableRepository).findAllByIdIn(anyList());
    }

    @Test
    @DisplayName("이미 그룹핑 된 주문 테이블이 포함된 경우 예외 발생 검증")
    public void throwException_ContainsAlreadyHasTableGroupOrderTable() {
        // Given
        OrderTable givenAlreadyHasTableGroupOrderTable = 비어있는_주문_테이블_생성();
        givenAlreadyHasTableGroupOrderTable.allocateTableGroup(단체석);

        List<OrderTable> givenContainsAlreadyHasTableGroupOrderTables = Arrays
            .asList(givenAlreadyHasTableGroupOrderTable, 비어있는_주문_테이블_생성());
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(givenContainsAlreadyHasTableGroupOrderTables);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableGroupService.create(테이블_그룹_생성(단체석_첫번째_주문_테이블, 단체석_두번째_주문_테이블)));

        verify(orderTableRepository).findAllByIdIn(anyList());
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다.")
    public void ungroupTable() {
        // Given
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(단체석));

        // When
        tableGroupService.ungroup(단체석.getId());

        // Then
        verify(tableGroupRepository).findById(any());
        verify(tableOrderStatusValidator).validateOrderStatus(anyList());
    }
}
