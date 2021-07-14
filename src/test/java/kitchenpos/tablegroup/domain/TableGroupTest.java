package kitchenpos.tablegroup.domain;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;

@Disabled
@DisplayName("TableGroup 테스트 - OrderTableValidator로 해당 로직 이관")
class TableGroupTest {

//    @TestFactory
//    @DisplayName("단체 지정시 오류 확인")
//    List<DynamicTest> has_group_exception() {
//        TableGroup tableGroup = new TableGroup();
//        return Arrays.asList(
//                dynamicTest("비어있지 않은 테이블을 정산 그룹에 추가하려는 경우 오류.", () ->
//                        assertThatThrownBy(() -> tableGroup.addOrderTable(new OrderTable(3, false)))
//                                .isInstanceOf(IllegalArgumentException.class)
//                                .hasMessage("비어있지 않은 테이블은 정산 그룹에 포함시킬 수 없습니다.")
//                ),
//                dynamicTest("이미 단체지정이 되어 있는 주문테이블을 추가하려는 경우 오류.", () -> {
//                    // given
//                    OrderTable orderTable = new OrderTable(3, true);
//                    orderTable.setTableGroup(tableGroup);
//
//                    // then
//                    assertThatThrownBy(() -> tableGroup.addOrderTable(orderTable))
//                            .isInstanceOf(IllegalArgumentException.class)
//                            .hasMessage("정산 그룹에 포함된 테이블을 새로운 정산그룹에 포함시킬 수 없습니다.");
//                })
//        );
//    }
}
