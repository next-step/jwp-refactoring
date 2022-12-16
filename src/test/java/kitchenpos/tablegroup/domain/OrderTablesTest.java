package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class OrderTablesTest {


    @Test
    void 주문_테이블을_추가할_수_있다() {
        OrderTables orderTables = new OrderTables();

        orderTables.addAllOrderTables(Arrays.asList(new OrderTable(1, false), new OrderTable(1, false)));

        assertThat(orderTables.getOrderTables()).hasSize(2);
    }

    @Test
    void 빈_테이블이_아닌_주문_테이블이_있을_경우_단체_지정_불가() {
        OrderTables orderTables = new OrderTables();

        ThrowingCallable 빈_테이블이_아닌_주문_테이블이_포함_될_경우 = () -> orderTables
                .validateOrderTableEmptyOrNonNull(Arrays.asList(new OrderTable(1, false), new OrderTable(1, false)));

        assertThatIllegalArgumentException().isThrownBy(빈_테이블이_아닌_주문_테이블이_포함_될_경우);
    }

    @Test
    void 이미_단체_지정이_된_주문_테이블이_있을_경우_단체_지정_불가() {
        OrderTables orderTables = new OrderTables();
        OrderTable 이미_단체_지정이_된_주문_테이블 = new OrderTable(1, true);
        이미_단체_지정이_된_주문_테이블
                .changeTableGroup(new TableGroup(Arrays.asList(new OrderTable(1, true), new OrderTable(2, true))));

        ThrowingCallable 이미_단체_지정이_된_주문_테이블이_포함_될_경우 = () -> orderTables
                .validateOrderTableEmptyOrNonNull(Arrays.asList(이미_단체_지정이_된_주문_테이블, new OrderTable(1, true)));

        assertThatIllegalArgumentException().isThrownBy(이미_단체_지정이_된_주문_테이블이_포함_될_경우);
    }

    @Test
    void 주문_테이블은_필수로_지정_해야_한다() {
        OrderTables orderTables = new OrderTables();

        ThrowingCallable 주문_테이블을_지정하지_않은_경우 = orderTables::validateOrderTablesSize;

        assertThatIllegalArgumentException().isThrownBy(주문_테이블을_지정하지_않은_경우);
    }

    @Test
    void 주문_테이블은_2개이상_지정_해야_한다() {
        OrderTables orderTables = new OrderTables();
        orderTables.addAllOrderTables(Collections.singletonList(new OrderTable(1, false)));

        ThrowingCallable 주문_테이블은_2개_이하로_지정한_경우 = orderTables::validateOrderTablesSize;

        assertThatIllegalArgumentException().isThrownBy(주문_테이블은_2개_이하로_지정한_경우);
    }
}
