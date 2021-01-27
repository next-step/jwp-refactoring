package api.order.ui;

import api.order.application.TableGroupService;
import api.order.dto.TableGroupRequest_Create;
import api.order.dto.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {
	private final TableGroupService tableGroupService;

	public TableGroupRestController(final TableGroupService tableGroupService) {
		this.tableGroupService = tableGroupService;
	}

	@PostMapping("/api/table-groups")
	public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupRequest_Create request) {
		final TableGroupResponse created = tableGroupService.create(request);
		final URI uri = URI.create("/api/table-groups/" + created.getId());
		return ResponseEntity.created(uri).body(created);
	}

	@DeleteMapping("/api/table-groups/{tableGroupId}")
	public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
		tableGroupService.ungroup(tableGroupId);
		return ResponseEntity.noContent().build();
	}
}
