package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Collections;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private TableService tableService;

    private OrderTable orderTable1;
    private OrderTable changeOrderTable;

    @BeforeEach
    void setUp() {
        super.setUp();

        orderTable1 = this.orderTableRepository.save(new OrderTable(4, false));
        changeOrderTable = new OrderTable();
    }

    @Test
    @DisplayName("테이블을 생성한다.")
    void create() {
        OrderTable orderTable = this.tableService.create(new OrderTable(4, false));

        assertThat(orderTable.getId()).isNotNull();
        assertThat(orderTable.getTableGroup()).isNull();
    }

    @Test
    @DisplayName("모든 테이블을 조회한다.")
    void list() {
        OrderTable orderTable2 = this.orderTableRepository.save(new OrderTable(4, false));

        assertThat(this.tableService.list()).containsExactly(orderTable1, orderTable2);
    }

    @Test
    @DisplayName("테이블의 빈 상태가 변경된다.")
    void changeEmpty() {
        changeOrderTable.setEmpty(true);

        OrderTable expectedOrderTable = this.tableService.changeEmpty(orderTable1.getId(), changeOrderTable);

        assertThat(expectedOrderTable.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("테이블 그룹에 포함될 경우 테이블의 빈 상태를 바꿀 수 없다.")
    void changeEmptyFail_existTableGroup() {
        TableGroup tableGroup = this.tableGroupRepository.save(new TableGroup(null));
        OrderTable orderTable = this.orderTableRepository.save(new OrderTable(tableGroup, 4, false));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableService.changeEmpty(orderTable.getId(), null));
    }

    @Test
    @DisplayName("주문 상태가 식사중이거나 조리중인 테이블이 있다면 빈 상태를 바꿀 수 없다.")
    void changeEmptyFail_existOrderCookingOrMeal() {
        Order order = new Order(orderTable1.getId(), Collections.singletonList(new OrderLineItem(1L, 1L)));
        this.orderRepository.save(order);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableService.changeEmpty(orderTable1.getId(), null));
    }

    @Test
    @DisplayName("테이블의 인원수를 변경한다.")
    void changeNumberOfGuests() {
        changeOrderTable.setNumberOfGuests(3);

        OrderTable expectedOrderTable = this.tableService.changeNumberOfGuests(orderTable1.getId(), changeOrderTable);

        assertThat(expectedOrderTable.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    @DisplayName("변경하고자 하는 손님의 숫자가 음수일 경우 에러를 던진다.")
    void changeNumberOfGuestsFail_numberOfGuests() {
        changeOrderTable.setNumberOfGuests(-1);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableService.changeNumberOfGuests(1L, changeOrderTable));
    }

    @Test
    @DisplayName("변경하고자 하는 테이블이 없을 경우 에러를 던진다.")
    void changeNumberOfGuestsFail_orderTable() {
        changeOrderTable.setNumberOfGuests(5);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableService.changeNumberOfGuests(100L, changeOrderTable));
    }

}
