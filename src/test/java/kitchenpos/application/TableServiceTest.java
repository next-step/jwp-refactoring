package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.transaction.Transactional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.creator.OrderTableCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableServiceTest {

    @Autowired
    OrderTableRepository orderTableRepository;
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    TableService tableService;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Autowired
    OrderTableCreator orderTableCreator;

    OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
    }


    @AfterEach
    void tearDown() {
        orderRepository.deleteAllInBatch();
        orderTableRepository.deleteAllInBatch();
        tableGroupRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("주문 테이블 저장")
    public void save() {
        OrderTable savedTable = tableService.create(new OrderTableRequest());
        assertThat(savedTable.getId()).isNotNull();
    }

    @Test
    @DisplayName("전체 테이블 조회 정상")
    public void list() {
        tableService.create(new OrderTableRequest());
        tableService.create(new OrderTableRequest());

        assertThat(tableService.list()).hasSize(2);
    }

    @Test
    @DisplayName("테이블 빈값 여부 세팅할 테이블 미존재")
    public void changeEmptyNotFoundException() {
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(10L, new OrderTableRequest()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Transactional
    @DisplayName("단체로 지정된 테이블의 자리 착석여부 변경시 예외처리")
    public void changeEmtpyAlreadyInTableGroup() {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());

        orderTable.setEmpty(true);
        orderTable.setTableGroup(tableGroup);

        OrderTable changeOrderTable = new OrderTable();
        changeOrderTable.setEmpty(false);

        orderTableRepository.save(orderTable);

        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), OrderTableRequest.of(changeOrderTable)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("변경할 테이블의 주문건 중 COOKING 또는 MEAL 상태인 건 존재시 변경 불가")
    public void changeEmtpyInCookingOrMeal() {
        orderTable.setEmpty(true);

        OrderTable changeOrderTable = new OrderTable();
        changeOrderTable.setEmpty(false);

        Order order = new Order(orderTable, OrderStatus.COOKING);
        orderRepository.save(order);


        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), OrderTableRequest.of(changeOrderTable)))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("테이블 empty 상태 변경 정상 처리")
    public void changEmptySuccess() {
        orderTable.setEmpty(true);

        OrderTable changeOrderTable = new OrderTable();
        changeOrderTable.setId(1L);
        changeOrderTable.setEmpty(false);

        orderTableRepository.save(orderTable);

        assertThat(tableService.changeEmpty(orderTable.getId(), OrderTableRequest.of(changeOrderTable)).isEmpty()).isEqualTo(
                changeOrderTable.isEmpty());
    }

    @Test
    @DisplayName("0미만 고객수로 변경 시도 시 에러 반환")
    public void changeNumerOfGuestsUnderZero() {
        orderTableRepository.save(orderTable);
        orderTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, OrderTableRequest.of(orderTable))).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("없는 테이블 고객수 변경 시도 시 에러 반환")
    public void changeNumerOfGuestsEmptyTable() {
        orderTable.setNumberOfGuests(2);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, OrderTableRequest.of(orderTable))).isInstanceOf(
                IllegalArgumentException.class);
    }


    @Test
    @DisplayName("테이블 고객수 변경 정상 처리")
    public void changeNumerOfGuestsSuccess() {
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        OrderTable changeOrderTable = new OrderTable();
        changeOrderTable.setNumberOfGuests(2);

        Assertions.assertThat(tableService.changeNumberOfGuests(savedOrderTable.getId(), OrderTableRequest.of(changeOrderTable)).getNumberOfGuests())
                .isEqualTo(changeOrderTable.getNumberOfGuests());
    }
}