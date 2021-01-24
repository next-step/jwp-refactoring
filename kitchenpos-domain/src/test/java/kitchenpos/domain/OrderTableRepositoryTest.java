package kitchenpos.domain;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class OrderTableRepositoryTest {
    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @DisplayName("단체 지정")
    @Test
    void group() {
    }

    @DisplayName("주문 테이블 등록")
    @Test
    void create() {
        OrderTable expected = new OrderTable(0, false);
        OrderTable actual = orderTableRepository.save(expected);
        Assertions.assertThat(expected == actual).isTrue();
    }

    @DisplayName("주문 테이블 비어있는지 여부 변경")
    @Test
    void changeEmpty() {
        OrderTable expected = orderTableRepository.save(new OrderTable(0, false));
        expected.changeEmpty(true);

        OrderTable actual = orderTableRepository.findById(expected.getId()).get();
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("손님의 수 변경")
    @Test
    void changeNumberOfGuests() {
        OrderTable expected = orderTableRepository.save(new OrderTable(0, false));
        expected.changeNumberOfGuests(10);

        OrderTable actual = orderTableRepository.findById(expected.getId()).get();
        assertThat(actual.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("손님의 수 변경 예외 - 0보다 작을 경우")
    @Test
    void validNumberOfGuests() {
        OrderTable expected = orderTableRepository.save(new OrderTable(0, false));

        Assertions.assertThatThrownBy(() -> {
            expected.changeNumberOfGuests(-1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 비어있는지 여부 변경 예외 - 단체 지정인 경우")
    @Test
    void validExistTableGroup() {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        OrderTable expected = orderTableRepository.save(new OrderTable(0, false));
        expected.changeTableGroupId(tableGroup.getId());

        Assertions.assertThatThrownBy(() -> {
            expected.changeEmpty(true);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
