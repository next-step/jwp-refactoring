package kitchenpos.tablegroup.domain;

import kitchenpos.tablegroup.infra.TableGroupRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class TableGroupTest {
    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("단체 지정은 아이디, 생성시간, 주문 테이블로 구성되어 있다.")
    @Test
    void create() {
        final TableGroup tableGroup = TableGroup.of(Arrays.asList(1L, 2L));
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
        ThrowableAssert.ThrowingCallable createCall = () -> TableGroup.of(Collections.singletonList(2L));
        // then
        assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
    }
}
