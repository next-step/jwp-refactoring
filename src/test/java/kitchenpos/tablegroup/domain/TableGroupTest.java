package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.application.validator.OrderTableTableGroupCreateValidator;
import kitchenpos.tablegroup.infra.TableGroupRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class TableGroupTest {
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableTableGroupCreateValidator orderTableCreateTableGroupValidator;

    @DisplayName("단체 지정은 아이디, 생성시간, 주문 테이블로 구성되어 있다.")
    @Test
    void create() {
        final TableGroup tableGroup = TableGroup.create(Arrays.asList(1L, 2L), orderTableCreateTableGroupValidator);
        final TableGroup actual = tableGroupRepository.save(tableGroup);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderTableIds().size()).isEqualTo(2)
        );
    }

    @DisplayName("2개 미만일 경우 생성할 수 없다.")
    @Test
    void createFail() {
        // given, when
        ThrowableAssert.ThrowingCallable createCall = () -> TableGroup.create(Collections.singletonList(2L), orderTableCreateTableGroupValidator);
        // then
        assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
    }
}
