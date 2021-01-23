package kitchenpos;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@RunWith(ArchUnitRunner.class) // Remove this line for JUnit 5!!
@AnalyzeClasses(packages = "kitchenpos")
public class KitchenPosArchitectureTest {

	@DisplayName("패키지 양방향 체크")
	@com.tngtech.archunit.junit.ArchTest
	public static void packageShouldBeFreeOfCycles(JavaClasses classes) {
		ArchRule freeOfCycles = slices().matching("kitchenpos.(*)..")
				.should().beFreeOfCycles();
		freeOfCycles.check(classes);
	}
}
