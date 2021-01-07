package kitchenpos;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

public class ArchTest {

	@DisplayName("패키지간 순환 참조를 검증합니다.")
	@Test
	void packageCycleTests() {
		JavaClasses classes = new ClassFileImporter()
			.withImportOption(
				new com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests())
			.importPackages("kitchenpos");

		ArchRule freeOfCycles = slices().matching("kitchenpos.(*)..")
			.should().beFreeOfCycles();
		freeOfCycles.check(classes);
	}
}
