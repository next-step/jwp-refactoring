package kitchenpos.ui;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;

@DisplayName("주문 테이블 관련 기능")
@SpringBootTest
class TableRestControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private TableService tableService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TableRestController tableRestController;

	private OrderTable orderTable;

	@BeforeEach
	void setup() {
		// MockMvc
		mockMvc = MockMvcBuilders.standaloneSetup(tableRestController)
			.addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
			.alwaysDo(print())
			.build();

		orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setNumberOfGuests(2);
		orderTable.setEmpty(true);
	}

	@Test
	@DisplayName("주문 테이블을 생성할 수 있다.")
	public void create() throws Exception {
		// given
		given(tableService.create(any())).willReturn(orderTable);

		// when
		final ResultActions actions = 주문_테이블_생성_요청();

		// then
		주문_테이블_생성에_성공함(actions, orderTable);
	}

	@Test
	@DisplayName("주문 테이블 목록을 조회할 수 있다.")
	public void list() throws Exception {
		// given
		given(tableService.list()).willReturn(Arrays.asList(orderTable));

		// when
		final ResultActions actions = 주문_테이블_조회_요청();

		// then
		주문_테이블_조회에_성공함(actions, orderTable);
	}

	@Test
	@DisplayName("주문 테이블을 비어있는 상태로 만들 수 있다.")
	public void changeEmpty() throws Exception {
		// given
		OrderTable changeOrderTable = new OrderTable();
		changeOrderTable.setId(orderTable.getId());
		changeOrderTable.setNumberOfGuests(orderTable.getNumberOfGuests());
		changeOrderTable.setEmpty(true);

		given(tableService.changeEmpty(any(), any())).willReturn(changeOrderTable);

		// when
		final ResultActions actions = 주문_테이블_상태_변경_요청();

		// then
		주문_테이블_상태가_변경됨(actions, changeOrderTable);
	}

	@Test
	@DisplayName("주문 테이블 손님 숫자를 변경할 수 있다.")
	public void changeNumberOfGuests() throws Exception {
		// given
		OrderTable changeOrderTable = new OrderTable();
		changeOrderTable.setId(orderTable.getId());
		changeOrderTable.setNumberOfGuests(orderTable.getNumberOfGuests() + 4);

		given(tableService.changeNumberOfGuests(any(),any())).willReturn(changeOrderTable);

		// when
		final ResultActions actions = 주문_테이블_손님_숫자_변경_요청();

		// then
		주문_테이블_손님_숫자_변경됨(actions, changeOrderTable);
	}

	private ResultActions 주문_테이블_생성_요청() throws Exception {
		return mockMvc.perform(post("/api/tables")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(orderTable)));
	}

	private void 주문_테이블_생성에_성공함(ResultActions actions, OrderTable orderTable) throws Exception {
		actions.andExpect(status().isCreated())
			.andExpect(header().string("location", "/api/tables" + "/1"))
			.andExpect(content().string(containsString(String.valueOf(orderTable.getNumberOfGuests()))));
	}

	private ResultActions 주문_테이블_조회_요청() throws Exception {
		return mockMvc.perform(get("/api/tables")
			.contentType(MediaType.APPLICATION_JSON));
	}

	private void 주문_테이블_조회에_성공함(ResultActions actions, OrderTable orderTable) throws Exception {
		actions.andExpect(status().isOk())
			.andExpect(content().string(containsString(String.valueOf(orderTable.getNumberOfGuests()))));
	}

	private ResultActions 주문_테이블_상태_변경_요청() throws Exception {
		return mockMvc.perform(put("/api/tables/{orderTableId}/empty", orderTable.getId())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(orderTable)));
	}

	private void 주문_테이블_상태가_변경됨(ResultActions actions, OrderTable orderTable) throws Exception {
		actions.andExpect(status().isOk())
			.andExpect(content().string(containsString(String.valueOf(orderTable.getNumberOfGuests()))));
	}

	private ResultActions 주문_테이블_손님_숫자_변경_요청() throws Exception {
		return mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", orderTable.getId())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(orderTable)));
	}

	private void 주문_테이블_손님_숫자_변경됨(ResultActions actions, OrderTable orderTable) throws Exception {
		actions.andExpect(status().isOk())
			.andExpect(content().string(containsString(String.valueOf(orderTable.getNumberOfGuests()))));
	}
}
