package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;

@DataJpaTest
public class TableGroupValidatorTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("그룹 지정을 요청한 테이블 ID 개수가 지정한 개수 이상인지 테스트")
    @Test
    void validateOrderTableIdSize() {
        // given
        final TableGroupValidator tableGroupValidator = new TableGroupValidator(orderTableRepository);
        final TableGroupRequest tableGroupRequest = new TableGroupRequest();

        // when
        final Throwable throwable = catchThrowable(() -> tableGroupValidator.validate(tableGroupRequest));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹 지정 요청한 테이블 개수와 등록된 테이블 개수가 일치하는지 테스트")
    @Test
    void validateOrderTableSize() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(1));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(1));
        final TableGroupValidator tableGroupValidator = new TableGroupValidator(orderTableRepository);
        final List<OrderTableIdRequest> requests = Arrays.asList(new OrderTableIdRequest(orderTable.getId()),
            new OrderTableIdRequest(orderTable2.getId()), new OrderTableIdRequest(99999L));
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(requests);

        // when
        final Throwable throwable = catchThrowable(() -> tableGroupValidator.validate(tableGroupRequest));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("이용 중이거나 이미 그룹이 저장된 테이블이 있는지 테스트")
    @Test
    void validateOrderTables() {
        // given
        final OrderTable orderTable = new OrderTable(1);
        orderTable.changeEmpty(true);
        final OrderTable orderTable2 = new OrderTable(1);
        final OrderTable saved1 = orderTableRepository.save(orderTable);
        final OrderTable saved2 = orderTableRepository.save(orderTable2);
        final TableGroupValidator tableGroupValidator = new TableGroupValidator(orderTableRepository);
        final List<OrderTableIdRequest> requests = Arrays.asList(new OrderTableIdRequest(saved1.getId()),
            new OrderTableIdRequest(saved2.getId()));
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(requests);

        // when
        final Throwable throwable = catchThrowable(() -> tableGroupValidator.validate(tableGroupRequest));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);

    }
}
