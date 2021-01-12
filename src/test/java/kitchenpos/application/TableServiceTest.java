package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 테이블에 대한 비즈니스 로직")
class TableServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @MockBean
    private TableService tableService;

    @DisplayName("주문 테이블를 생성할 수 있다.")
    @Test
    void create() {
        // given


        // when


        // then

    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        // given


        // when


        // then

    }

    @DisplayName("주문 테이블의 등록 가능 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given


        // when


        // then

    }

    @DisplayName("이미 단체 지정이 되어 있다면 상태를 변경할 수 없다.")
    @Test
    void inTableGroup() {
        // given


        // when


        // then

    }

    @DisplayName("주문이 조리 중이거나 식사 중일때는 상태를 변경할 수 없다.")
    @Test
    void test() {
        // given


        // when


        // then

    }

    @DisplayName("주문 테이블에 손님 수를 등록한다.")
    @Test
    void numberOfGuests() {
        // given


        // when


        // then

    }

    @DisplayName("손님 수는 0보다 작을 수 없다.")
    @Test
    void numberOfGuestsRange() {
        // given


        // when


        // then

    }

    @DisplayName("주문 등록 불가 상태인 주문 테이블인 경우 등록할 수 없다.")
    @Test
    void emptyTable() {
        // given


        // when


        // then

    }

}
