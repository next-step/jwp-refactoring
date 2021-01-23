package kitchenpos.ordertablegroup.application;

import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertablegroup.dto.OrderTableGroupRequest;
import kitchenpos.ordertablegroup.dto.OrderTableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
class OrderTableGroupServiceTest {

    @Autowired
    private OrderTableService orderTableService;

    @Autowired
    private OrderTableGroupService orderTableGroupService;

    @DisplayName("테이블 그룹을 등록한다.")
    @Test
    void create() {
        // given
        OrderTableGroupRequest tableGroup = new OrderTableGroupRequest(Arrays.asList(1L, 2L));

        // when
        OrderTableGroupResponse savedTableGroup = orderTableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(2);
    }

    @DisplayName("테이블 그룹 등록 예외 - 테이블 목록이 2개 이상이어야 한다.")
    @Test
    void create_exception1() {
        // given
        OrderTableGroupRequest tableGroup = new OrderTableGroupRequest(Arrays.asList(1L));

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderTableGroupService.create(tableGroup))
            .withMessage("테이블 목록이 2개 이상이어야 한다.");
    }

    @DisplayName("테이블 그룹 등록 예외 - 테이블 목록이 등록되어 있어야 한다.")
    @Test
    void create_exception2() {
        // given
        OrderTableGroupRequest tableGroup = new OrderTableGroupRequest(Arrays.asList(1L, 2L, 999999L));

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderTableGroupService.create(tableGroup))
            .withMessage("테이블 목록이 등록되어 있어야 한다.");
    }

    @DisplayName("테이블 그룹 등록 예외 - 테이블 목록이 이미 다른 테이블 그룹에 속해있지 않아야 한다.")
    @Test
    void create_exception3() {
        // given
        OrderTableGroupRequest tableGroup = new OrderTableGroupRequest(Arrays.asList(7L, 2L));

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderTableGroupService.create(tableGroup))
            .withMessage("테이블 목록이 이미 다른 테이블 그룹에 속해있지 않아야 한다.");
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        // when
        orderTableGroupService.ungroup(2L);

        // then
        OrderTable updatedOrderTable = orderTableService.findById(8L);
        assertThat(updatedOrderTable.getTableGroupId()).isNull();
    }

    @DisplayName("테이블 그룹 해제 예외 - `조리중`과 `식사중`에는 변경할 수 없다.")
    @Test
    void ungroup_exception1() {
        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderTableGroupService.ungroup(1L))
            .withMessage("`조리중`과 `식사중`에는 변경할 수 없다.");
    }

}
