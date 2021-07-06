package kitchenpos.ui.order;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.TableRestController;

@DisplayName("주문 테이블 컨트롤러 테스트")
@WebMvcTest(TableRestController.class)
public class OrderTableRestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private TableService orderTableService;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
			.alwaysDo(print())
			.build();
	}

	@DisplayName("create")
	@Test
	public void create() throws Exception {
		OrderTable orderTable = new OrderTable(1L, 1L, 0, true);
		given(orderTableService.create(any())).willReturn(orderTable);

		mockMvc.perform(post("/api/tables")
			.content(objectMapper.writeValueAsString(orderTable))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("location", "/api/tables/" + orderTable.getId()))
		;
	}

	@DisplayName("list")
	@Test
	public void list() throws Exception {
		OrderTable orderTable = new OrderTable(1L, 1L, 0, true);
		given(orderTableService.list()).willReturn(Arrays.asList(orderTable));

		mockMvc.perform(get("/api/tables"))
			.andExpect(status().isOk())
		;
	}

	@DisplayName("changeEmpty")
	@Test
	public void changeEmpty() throws Exception {
		OrderTable orderTable = new OrderTable(1L, 1L, 0, true);
		given(orderTableService.changeEmpty(anyLong(), any())).willReturn(orderTable);

		mockMvc.perform(put("/api/tables/" + orderTable.getId() +"/empty")
			.content(objectMapper.writeValueAsString(orderTable))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
		;
	}

	@DisplayName("changeNumberOfGuests")
	@Test
	public void changeNumberOfGuests() throws Exception {
		OrderTable orderTable = new OrderTable(1L, 1L, 0, true);
		given(orderTableService.changeNumberOfGuests(anyLong(), any())).willReturn(orderTable);

		mockMvc.perform(put("/api/tables/" + orderTable.getId() +"/number-of-guests")
			.content(objectMapper.writeValueAsString(orderTable))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
		;
	}

}
