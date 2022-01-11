package binary_type_inference;

import static org.assertj.core.api.Assertions.*;

import ghidra.GhidraApplicationLayout;
import ghidra.framework.Application;
import ghidra.framework.ApplicationConfiguration;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.junit.Before;
import org.junit.Test;

public class BinaryTypeInferenceTest {
  private static final Path projectDir = Path.of(System.getProperty("user.dir"));
  private static final Path testDataDir =
      projectDir.getParent().resolve("binary_type_inference").resolve("test_data");

  @Before
  public void setUp() throws IOException {
    // Required for Ghidra search paths to 'os' binary directories
    if (!Application.isInitialized()) {
      Application.initializeApplication(
          new GhidraApplicationLayout(), new ApplicationConfiguration());
    }
  }

  @Test
  public void inferTypes() throws IOException {
    var demo =
        new BinaryTypeInference(
            Path.of(Application.getOSFile(BinaryTypeInference.DEFAULT_TOOL_NAME).getAbsolutePath()),
            testDataDir.resolve("list_test.o"),
            testDataDir.resolve("list_test.json"),
            testDataDir.resolve("list_lattice.json"),
            testDataDir.resolve("list_additional_constraints"));
    var result = demo.inferTypes();
    assertThat(result.success())
        .withFailMessage(new String(result.getStderr().readAllBytes(), StandardCharsets.UTF_8))
        .isTrue();

    var lastResult = demo.getLastResult();
    assertThat(lastResult.orElseThrow()).isSameAs(result);
  }
}
