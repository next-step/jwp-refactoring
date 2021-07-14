package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderTableValidator orderTableValidator;

    @TestFactory
    @DisplayName("단체지정 대상 유효성 검증 로직 테스트")
    List<DynamicTest> orderTable_is_empty_or_hasTableGroups() {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L, 2L));
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = new OrderTable(3, true);
        OrderTable orderTable2 = new OrderTable(3, false);
        OrderTable orderTable3 = new OrderTable(3, true);

        return Arrays.asList(
                dynamicTest("단체지정 테이블 중 비어있지 않은 테이블이 존재할 경우 오류 발생.", () -> {
                    // given
                    given(orderTableRepository.findByIdIn(any(List.class))).willReturn(Arrays.asList(orderTable1, orderTable2));

                    // then
                    assertThatThrownBy(() -> orderTableValidator.validateOrderTableIsEmptyOrHasTableGroups(Arrays.asList(1L, 2L)))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("비어있지 않은 테이블은 정산 그룹에 포함시킬 수 없습니다.");
                }),
                dynamicTest("단체지정 테이블 중 테이블 그룹이 지정되어 있는 테이블이 존재할 경우 오류 발생.", () -> {
                    // given
//                    orderTable1.setTableGroup(new TableGroup());
                    orderTable1.groupBy(1L);
                    given(orderTableRepository.findByIdIn(any(List.class))).willReturn(Arrays.asList(orderTable1, orderTable3));

                    // then
                    assertThatThrownBy(() -> orderTableValidator.validateOrderTableIsEmptyOrHasTableGroups(Arrays.asList(1L, 2L)))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("정산 그룹에 포함된 테이블을 새로운 정산그룹에 포함시킬 수 없습니다.");
                })
        );
    }
}
