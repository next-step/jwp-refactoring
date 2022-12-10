package kitchenpos.domain;

import static kitchenpos.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.domain.TableGroupTestFixture.generateTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import kitchenpos.common.constant.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단체 지정 관련 도메인 테스트")
public class TableGroupTest {

    OrderTable 단체등록된_주문테이블A;
    OrderTable 단체등록된_주문테이블B;
    OrderTable 주문테이블C;
    OrderTable 주문테이블D;

    @BeforeEach
    void setUp() {
        단체등록된_주문테이블A = generateOrderTable(1L, null, 5, true);
        단체등록된_주문테이블B = generateOrderTable(2L, null, 5, true);
        주문테이블C = generateOrderTable(3L, null, 5, true);
        주문테이블D = generateOrderTable(4L, null, 5, true);
        generateTableGroup(Arrays.asList(단체등록된_주문테이블A, 단체등록된_주문테이블B));
    }

    @DisplayName("단체 지정 시, 이미 단체가 지정된 주문 테이블을 포함하면 에러가 발생한다.")
    @Test
    void createTableGroupThrowErrorWhenOrderTableAlreadyHasTableGroup() {
        // when & then
        assertThatThrownBy(() -> generateTableGroup(Arrays.asList(단체등록된_주문테이블A, 주문테이블C)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.주문_테이블에_이미_단체_그룹_지정됨.getErrorMessage());
    }

    @DisplayName("단체를 지정한다.")
    @Test
    void createTableGroup() {
        // when
        TableGroup tableGroup = generateTableGroup(Arrays.asList(주문테이블C, 주문테이블D));

        // then
        assertAll(
                () -> assertThat(tableGroup.getOrderTables().unmodifiableOrderTables()).containsExactly(주문테이블C, 주문테이블D),
                () -> assertThat(tableGroup).isEqualTo(주문테이블C.getTableGroup()),
                () -> assertThat(tableGroup).isEqualTo(주문테이블D.getTableGroup())
        );
    }
}
