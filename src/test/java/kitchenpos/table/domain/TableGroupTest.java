package kitchenpos.table.domain;

import static kitchenpos.table.domain.OrderTableTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import kitchenpos.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단체 테이블 테스트")
class TableGroupTest {
    @Test
    @DisplayName("단체 테이블 객체 생성")
    void createTableGroup() {
        // when
        TableGroup actual = TableGroup.from(Arrays.asList(비어있는_테이블, 비어있는_테이블2));

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(TableGroup.class)
        );
    }

    @Test
    @DisplayName("단체 테이블 생성시 모두 비어있는 테이블이어야 한다.")
    void createTableGroupByNotEmptyTable() {
        // when & then
        assertThatThrownBy(() -> TableGroup.from(Arrays.asList(비어있는_테이블2, 주문테이블2)))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("모두 비어있는 테이블이어야 합니다.");
    }

    @Test
    @DisplayName("단체 테이블 생성시 단체 테이블이 존재하면 안된다")
    void createTableGroupByAlreadyExistGroup() {
        // when & then
        assertThatThrownBy(() -> TableGroup.from(Arrays.asList(비어있는_테이블, 단체테이블)))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("단체 테이블이 존재합니다.");
    }
}
