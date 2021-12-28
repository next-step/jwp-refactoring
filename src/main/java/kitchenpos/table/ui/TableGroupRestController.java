package kitchenpos.table.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.utils.ResponseUtils;

@RequestMapping(TableGroupRestController.BASE_URL)
@RestController
public class TableGroupRestController {

	public static final String BASE_URL = "/api/table-groups";

	private final TableGroupService tableGroupService;

	public TableGroupRestController(final TableGroupService tableGroupService) {
		this.tableGroupService = tableGroupService;
	}

	@PostMapping
	public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupRequest tableGroup) {
		final TableGroupResponse created = tableGroupService.create(tableGroup);
		final URI uri = ResponseUtils.createdUrl(BASE_URL, created.getId());
		return ResponseEntity.created(uri)
			.body(created)
			;
	}

	@DeleteMapping("/{tableGroupId}")
	public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
		tableGroupService.ungroup(tableGroupId);
		return ResponseEntity.noContent()
			.build()
			;
	}
}
