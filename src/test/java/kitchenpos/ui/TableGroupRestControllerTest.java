package kitchenpos.ui;

import static kitchenpos.common.TestFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.http.MediaType;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.dto.TableGroupRequest;

@DisplayName("TableGroupRestController 테스트")
class TableGroupRestControllerTest extends BaseControllerTest {

	@DisplayName("TableGroup 생성 요청")
	@Test
	void create() throws Exception {
		int expectedId = 3;

		TableGroupRequest tableGroup = TableGroupRequest.of(Arrays.asList(예제테이블1_ID, 예제테이블2_ID));

		mockMvc.perform(post("/api/table-groups")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(tableGroup)))
			.andDo(print())
			.andExpect(header().string("Location", "/api/table-groups/" + expectedId))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(expectedId))
			.andExpect(jsonPath("$.orderTables", Matchers.hasSize(2)));
	}

	@DisplayName("TableGroup 생성 요청시 테이블 정보가 2개보다 작거나 없으면 BadRequest가 발생한다.")
	@ParameterizedTest
	@NullSource
	@MethodSource("paramCreateBadRequest")
	void createBadRequest(List<Long> orderTables) throws Exception {

		TableGroupRequest tableGroup = TableGroupRequest.of(orderTables);

		mockMvc.perform(post("/api/table-groups")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(tableGroup)))
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	public static Stream<Arguments> paramCreateBadRequest() {
		return Stream.of(
			Arguments.of(Collections.emptyList()),
			Arguments.of(Collections.singletonList(예제테이블1_ID))
		);
	}

	@DisplayName("TableGroup 해제 요청")
	@Test
	void ungroup() throws Exception {
		long expectedId = 1L;

		mockMvc.perform(delete("/api/table-groups/{groupId}", expectedId))
			.andDo(print())
			.andExpect(status().isNoContent());
	}
}