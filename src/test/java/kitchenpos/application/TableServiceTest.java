package kitchenpos.application;

import static kitchenpos.application.OrderBuilder.completionStatusOrder;
import static kitchenpos.application.OrderBuilder.cookingStatusOrder;
import static kitchenpos.application.OrderBuilder.mealStatusOrder;
import static kitchenpos.application.OrderTableBuilder.emptyOrderTableWithGuestNo;
import static kitchenpos.application.OrderTableBuilder.nonEmptyOrderTableWithGuestNo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class TableServiceTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;

    private OrderTable orderTable;
    private OrderTable savedOrderTable;
    
    @BeforeEach
    void setup() {
        orderTable = nonEmptyOrderTableWithGuestNo(2);
        savedOrderTable = tableService.create(orderTable);
    }

    @Test
    void 생성시_생성한오더테이블반환() {
        assertAll(
            () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(2),
            () -> assertThat(savedOrderTable.isEmpty()).isFalse()
        );
    }

    @Test
    void 조회시_존재하는오더테이블목록반환() {
        assertThat(tableService.list()).isNotEmpty();
    }

    @Test
    void 빈테이블로변경시_주문테이블이존재하지않는경우_예외발생() {
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), savedOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈테이블로변경시_해당주문이조리중일경우_예외발생() {
        orderDao.save(cookingStatusOrder(savedOrderTable.getId()));
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈테이블로변경시_해당주문이식사중일경우_예외발생() {
        orderDao.save(mealStatusOrder(savedOrderTable.getId()));
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), emptyOrderTableWithGuestNo(2)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈테이블로변경시_해당주문이완료했을경우_변경성공() {
        orderDao.save(completionStatusOrder(savedOrderTable.getId()));
        assertThat(tableService.changeEmpty(savedOrderTable.getId(), emptyOrderTableWithGuestNo(2))
            .isEmpty()).isTrue();
    }

    @Test
    void 빈테이블로변경시_해당주문이존재하지않는경우_변경성공() {
        assertThat(tableService.changeEmpty(savedOrderTable.getId(), emptyOrderTableWithGuestNo(2))
            .isEmpty()).isTrue();
    }

    @Test
    void 방문손님수변경시_변경하려는수가0보다작을경우_예외발생() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), nonEmptyOrderTableWithGuestNo(-1)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 방문손님수변경시_주문테이블이존재하지않는경우_예외발생() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), nonEmptyOrderTableWithGuestNo(10)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 방문손님수변경시_주문테이블이비어있을경우_예외발생() {
        savedOrderTable = tableService.create(emptyOrderTableWithGuestNo(2));
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), nonEmptyOrderTableWithGuestNo(10)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 방문손님수변경시_주문테이블이비어있지않을경우_변경성공() {
        assertThat(tableService.changeNumberOfGuests(savedOrderTable.getId(), nonEmptyOrderTableWithGuestNo(10))
            .getNumberOfGuests()).isEqualTo(10);
    }


}
