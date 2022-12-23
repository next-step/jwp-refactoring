package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.dto.TableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

public class TableGroupAcceptanceTest extends MockMvcAcceptanceTest {

    /**
     * Feature: 테이블 단체 지정
     *  Background
     *      given: 기존에 테이블이 등록되어 있음
     *
     *  Scenario: 테이블 단체 지정 관리
     *      when: 테이블 그룹을 생성하면
     *      then: 대상 테이블들이 단체로 지정되고,
     *      when: 단체로 지정된 테이블의 단체 지정을 해제하면
     *      then: 대상 테이블의 단체가 해제된다.
     */
    @Test
    @DisplayName("테이블 단체 지정 테스트")
    void createAndUnGroupTest() throws Exception {
        // given
        OrderTableResponse 테이블_1 = 테이블_생성(0, true);
        OrderTableResponse 테이블_2 = 테이블_생성(0, true);

        // when
        ResultActions 테이블_그룹_생성_결과 = 테이블_그룹_생성_요청(new TableRequest(테이블_1.getId()), new TableRequest(테이블_2.getId()));

        // then
        테이블_그룹_생성_결과
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andDo(print());

        // when
        TableGroup 테이블_그룹 = getObjectByResponse(테이블_그룹_생성_결과, TableGroup.class);
        ResultActions 테이블_그룹_제거_결과 = 테이블_그룹_제거_요청(테이블_그룹.getId());

        // then
        테이블_그룹_제거_결과
                .andExpect(status().isNoContent())
                .andDo(print());

    }

    /**
     *  given:
     *  when: 테이블없이 단체 지정을 시도하면
     *  then: 테이블이 단체로 지정되지 않는다.
     */
    @Test
    @DisplayName("테이블 없이 단체 지정을 시도하면, 단체 지정이 실패한다.")
    void createFailTest1(){
        // given

        // when & then
        assertThatThrownBy(
                () -> 테이블_그룹_생성_요청()
        ).hasCause(new IllegalArgumentException());
    }

    /**
     *  given:
     *  when: 존재하지 않는 테이블을 단체 지정을 시도하면
     *  then: 테이블이 단체로 지정되지 않는다.
     */
    @Test
    @DisplayName("존재하지 않는 테이블을 단체 지정을 시도하면, 단체 지정이 실패한다.")
    void createFailTest2() throws Exception {
        // given
        OrderTableResponse 저장된_테이블 = 테이블_생성(0, true);
        OrderTable 저장되지_않은_테이블 = new OrderTable(0, true);

        // when & then
        assertThatThrownBy(
                () -> 테이블_그룹_생성_요청(new TableRequest(저장된_테이블.getId()), new TableRequest(저장되지_않은_테이블.getId()))
        ).hasCause(new IllegalArgumentException());
    }

    /**
     *  given: 비어있지 않은 테이블을 생성하고
     *  when: 비어있지 않은 테이블을 포함해서 단체지정을 시도하면
     *  then: 테이블이 단체로 지정되지 않는다.
     */
    @Test
    @DisplayName("비어있지 않은 테이블은 단체로 지정할 수 없다.")
    void createFailTest3() throws Exception {
        // given
        OrderTableResponse 비어있지_않은_테이블 = 테이블_생성(2, true);
        OrderTableResponse 빈_테이블 = 테이블_생성(0, false);

        // when & then
        assertThatThrownBy(
                () -> 테이블_그룹_생성_요청(new TableRequest(비어있지_않은_테이블.getId()), new TableRequest(빈_테이블.getId()))
        ).hasCause(new IllegalArgumentException());
    }

    /**
     *  given: 테이블들을 생성하고, 단체 지정을 하고
     *  when: 단체 지정된 테이블을 다른 단체로 지정하려고 하면
     *  then: 테이블이 새로운 단체로 지정되지 않는다.
     */
    @Test
    @DisplayName("이미 단체로 지정된 테이블은 새로 단체로 지정할 수 없다.")
    void createFailTest4() throws Exception {
        // given
        OrderTableResponse 테이블_1 = 테이블_생성(0, true);
        OrderTableResponse 테이블_2 = 테이블_생성(0, true);
        테이블_그룹_생성_요청(new TableRequest(테이블_1.getId()), new TableRequest(테이블_2.getId()));

        // when & then
        OrderTableResponse 테이블_3 = 테이블_생성(0, true);
        assertThatThrownBy(
                () -> 테이블_그룹_생성_요청(new TableRequest(테이블_1.getId()), new TableRequest(테이블_3.getId()))
        ).hasCause(new IllegalArgumentException());
    }


    /**
     *  Background
     *      given: 상품이 등록되어 있음
     *      And: 메뉴 그룹이 등록되어 있음
     *      And: 메뉴가 등록되어 있음
     *      And: 기존에 생성된 테이블에 주문이 들어가 있음
     *
     *  Scenario: 주문이 있는 테이블의 단체 지정 해제
     *      when: 주문상태가 조리중인 테이블을 단체를 해제하려고 하면
     *      then: 주문상태가 계산완료가 아니기 때문에 단체 지정을 해제할 수 없다.
     *      when: 주문상태가 식사중인 테이블을 단체를 해제하려고 하면
     *      then: 주문상태가 계산완료가 아니기 때문에 단체 지정을 해제할 수 없다.
     */
    @Test
    @DisplayName("단체 지정 된 테이블 중 계산완료 상태가 아닌 테이블이 있으면, 단체를 해제할 수 없다.")
    void unGroupFailTest() throws Exception {
        // given
        OrderTableResponse 테이블_1 = 테이블_생성(2, true);
        OrderTableResponse 테이블_2 = 테이블_생성(2, true);
        TableGroupResponse tableGroup = 테이블_그룹_생성(new TableRequest(테이블_1.getId()), new TableRequest(테이블_2.getId()));

        Product product1 = 상품_등록("상품1", 1000);
        Product product2 = 상품_등록("상품2", 2000);
        MenuGroup group1 = 메뉴_그룹_추가("그룹1");
        MenuResponse 메뉴1 = 메뉴_등록("메뉴1", 1000, group1, Arrays.asList(
                new MenuProduct(product1, 1),
                new MenuProduct(product2, 1)
        ));

        // when & then
        주문_생성(테이블_1, 메뉴1);
        assertThatThrownBy(
                () -> 테이블_그룹_제거_요청(tableGroup.getId())
        ).hasCause(new IllegalArgumentException());

    }
}
