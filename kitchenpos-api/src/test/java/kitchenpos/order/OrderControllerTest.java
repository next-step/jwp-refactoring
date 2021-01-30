package kitchenpos.order;

import kitchenpos.common.BaseContollerTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class OrderControllerTest extends BaseContollerTest {

    private OrderRequest orderRequest;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    public void setup() {
        this.orderRequest = this.createOrderRequest(OrderStatus.COOKING);
    }

    public OrderRequest createOrderRequest(OrderStatus orderStatus) {
        return new OrderRequest(
                this.orderTableRepository.save(new OrderTable(3, false)).getId()
                , orderStatus.name()
                , LocalDateTime.now()
                , Arrays.asList(new OrderLineItemRequest[]{this.createOrderLineItem()})
                );
    }

    private OrderLineItemRequest createOrderLineItem() {
        Menu menu = new Menu("테스트메뉴", BigDecimal.valueOf(10000));
        menu.changeMenuGroup(this.menuGroupRepository.save(new MenuGroup("테스트메뉴그룹")));

        return new OrderLineItemRequest(this.menuRepository.save(menu).getId(), 3);
    }

    @Test
    @DisplayName("새로운 주문을 등록합니다.")
    void create() throws Exception {
        주문_등록_요청(this.orderRequest, status().isCreated());
    }

    @Test
    @DisplayName("모든 주문을 조회합니다.")
    void findAll() throws Exception {
        주문_등록_요청(this.orderRequest, status().isCreated());

        MvcResult mvcResult = this.mockMvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(".id").exists())
                .andReturn();
    }

    private void 주문_등록_요청(OrderRequest orderRequest, ResultMatcher xxServerError) throws Exception {
        this.mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(orderRequest)
                ))
                .andDo(print())
                .andExpect(jsonPath(".id").exists())
                .andExpect(xxServerError)
        ;
    }
}
