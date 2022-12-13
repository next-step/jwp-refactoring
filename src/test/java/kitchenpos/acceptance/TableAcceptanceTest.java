package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

public class TableAcceptanceTest extends MockMvcAcceptanceTest {

    /**
     * given:
     * when: 테이블 생성 정보를 시도하면
     * then: 테이블이 생성된다.
     */
    @Test
    @DisplayName("테이블 생성 성공")
    void createTest() throws Exception {
        // given

        // when
        ResultActions 테이블_생성_요청_결과 = 테이블_생성_요청(0, true);

        // then
        테이블_생성_요청_결과
                .andExpect(status().isCreated())
                .andDo(print());
    }

    /**
     * given: 비어있지 않은 테이블을 생성하고
     * when: 비어있는 상태로 변경하면
     * then: 테이블이 비어있는 상태로 변경된다.
     */
    @Test
    @DisplayName("테이블 상태 변경")
    void changeEmptyTest() throws Exception {
        // given
        OrderTable 비어있지_않은_테이블 = 테이블_생성(2, false);

        // when
        ResultActions 테이블_상태_변경_결과 = 테이블_상태_변경_요청(비어있지_않은_테이블, true);

        // then
        테이블_상태_변경_결과
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empty", is(true)))
                .andDo(print());

    }

    /**
     * Feature: 단체지정된 테이블은 상태 변경 안됨.
     *  Background:
     *      given: 테이블이 기존에 등록되어 있음
     *      And: 기존에 등록된 테이블이 단체지정이 되어있음
     *
     *  Scenario: 단체지정된 테이블은 상태 변경이 불가하다.
     *      when: 단체지정이 되어있는 테이블의 상태 변경을 시도하면
     *      then: 상태 변경에 실패한다.
     */
    @Test
    @DisplayName("단체 지정된 테이블은 상태를 변경할 수 없다.")
    void changeEmptyFailTest1() throws Exception {
        // given
        OrderTable 빈_테이블1 = 테이블_생성(0, true);
        OrderTable 빈_테이블2 = 테이블_생성(0, true);
        테이블_그룹_생성(빈_테이블1, 빈_테이블2);

        // when & then
        assertThatThrownBy(
                () -> 테이블_상태_변경_요청(빈_테이블1, true)
        ).hasCause(new IllegalArgumentException());
    }

    /**
     * Feature: 계산완료가 안 된 테이블은 상태 변경 안됨.
     *  Background:
     *      given: 테이블이 기존에 등록되어 있음
     *      And: 테이블에 주문이 있음
     *      And: 주문이 계산완료 상태가 아님
     *
     *  Scenario: 계산완료가 안 된 테이블은 상태를 변경할 수 없다.
     *      when: 조리중이거나 식사중인 테이블을 비어있는 상태로 변경하려고 하면
     *      then: 상태 변경에 실패한다.
     */
    @Test
    @DisplayName("계산완료가 안 된 테이블은 비어있는 상태로 변경할 수 없다.")
    void changeEmptyFailTest2() throws Exception {
        // given
        OrderTable 비어있지_않은_테이블_1 =테이블_생성(2, false);
        OrderTable 비어있지_않은_테이블_2 = 테이블_생성(2, false);

        Product 상품1 = 상품_등록("상품1", 1000);
        Product 상품2 = 상품_등록("상품2", 2000);
        MenuGroup 그룹1 = 메뉴_그룹_추가("그룹1");
        Menu 메뉴1 = 메뉴_등록("메뉴1", 1000, 그룹1, Arrays.asList(
                new MenuProduct(상품1.getId(), 1),
                new MenuProduct(상품2.getId(), 1)
        ));
        주문_생성(비어있지_않은_테이블_1, 메뉴1);
        주문_생성(비어있지_않은_테이블_2, 메뉴1);

        // when & then
        assertThatThrownBy(
                () -> 테이블_상태_변경_요청(비어있지_않은_테이블_1, true)
        ).hasCause(new IllegalArgumentException());

        assertThatThrownBy(
                () -> 테이블_상태_변경_요청(비어있지_않은_테이블_2, true)
        ).hasCause(new IllegalArgumentException());
    }

    /**
     * given: 비어있지 않은 테이블을 생성하고
     * when: 생성된 테이블의 인원 수를 변경하면
     * then: 해당 테이블의 인원 수가 변경된다.
     */
    @Test
    @DisplayName("테이블 인원 수 변경 테스트")
    void changeNumberOfGuestTest() throws Exception {
        // given
        OrderTable 비어있지_않은_테이블 = 테이블_생성(2, false);

        // when
        ResultActions 테이블_인원_수_변경_결과 = 테이블_인원_수_변경(비어있지_않은_테이블, 4);

        // then
        테이블_인원_수_변경_결과
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfGuests", is(4)))
                .andDo(print());
    }

    /**
     * given: 비어있는 테이블을 생성하고
     * when: 생성된 테이블의 인원 수를 변경하면
     * then: 인원 수 변경에 실패한다.
     */
    @Test
    @DisplayName("비어있는 테이블의 인원 수를 변경할 수 없다.")
    void changeNumberOfGuestFailTest1() throws Exception {
        // given
        OrderTable 빈_테이블 = 테이블_생성(0, true);

        // when & then
        assertThatThrownBy(
                () -> 테이블_인원_수_변경(빈_테이블, 4)
        ).hasCause(new IllegalArgumentException());
    }

    /**
     * given: 비어있지 않은 테이블을 생성하고
     * when: 생성된 테이블의 인원 수를 음수로 변경하면
     * then: 인원 수 변경에 실패한다.
     */
    @Test
    @DisplayName("테이블의 인원 수를 음수로 변경할 수 없다.")
    void changeNumberOfGuestFailTest2() throws Exception {
        // given
        OrderTable 비어있지_않은_테이블 = 테이블_생성(2, false);

        // when & then
        assertThatThrownBy(
                () -> 테이블_인원_수_변경(비어있지_않은_테이블, -2)
        ).hasCause(new IllegalArgumentException());
    }

    private ResultActions 테이블_인원_수_변경(OrderTable table, int numberOfGuest) throws Exception {
        return mockPut("/api/tables/{orderTableId}/number-of-guests", table.getId(), new OrderTable(numberOfGuest));
    }

    private ResultActions 테이블_상태_변경_요청(OrderTable 비어있지_않은_테이블, boolean empty) throws Exception {
        return mockPut("/api/tables/{orderTableId}/empty", 비어있지_않은_테이블.getId(), new OrderTable(empty));
    }
}
