package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.order.domain.TableGroupTestFixture.generateTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
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
        단체등록된_주문테이블A = generateOrderTable(1L, 5, true);
        단체등록된_주문테이블B = generateOrderTable(2L, 5, true);
        주문테이블C = generateOrderTable(3L, 5, true);
        주문테이블D = generateOrderTable(4L, 5, true);
        generateTableGroup(1L, Arrays.asList(단체등록된_주문테이블A, 단체등록된_주문테이블B));
    }

    @DisplayName("단체 지정 시, 이미 단체가 지정된 주문 테이블을 포함하면 에러가 발생한다.")
    @Test
    void createTableGroupThrowErrorWhenOrderTableAlreadyHasTableGroup() {
        // when & then
        assertThatThrownBy(() -> generateTableGroup(2L, Arrays.asList(단체등록된_주문테이블A, 주문테이블C)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.단체_그룹_지정되어_있음.getErrorMessage());
    }

    @DisplayName("단체를 지정한다.")
    @Test
    void createTableGroup() {
        // when
        TableGroup tableGroup = generateTableGroup(1L, Arrays.asList(주문테이블C, 주문테이블D));

        // then
        assertThat(tableGroup.getId()).isNotNull();
    }
}
