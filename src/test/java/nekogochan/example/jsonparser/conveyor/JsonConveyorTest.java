package nekogochan.example.jsonparser.conveyor;

import nekogochan.example.jsonparser.conveyor.nodes.JsonArrayConveyor;
import nekogochan.example.jsonparser.conveyor.nodes.JsonObjectConveyor;
import nekogochan.example.jsonparser.conveyor.nodes.JsonValueConveyor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JsonConveyorTest {

  @ParameterizedTest
  @CsvSource(
    value = {
      "[     | A",
      "{     | O",
      "01234 | V",
      "sjdkf | V",
      "}     | N",
      "]     | N",
      ",     | N",
    },
    delimiter = '|'
  )
  void suitableConveyor(String jsonPart, String expectedConveyorCode) {
    var expectedConveyorType = switch (expectedConveyorCode) {
      case "A" -> JsonArrayConveyor.class;
      case "O" -> JsonObjectConveyor.class;
      case "V" -> JsonValueConveyor.class;
      default -> null;
    };
    var jsonConveyor = new JsonConveyor();

    var conveyor = jsonConveyor.suitableConveyor(jsonPart.charAt(0));

    assertEquals(
      expectedConveyorType,
      Optional.ofNullable(conveyor).map(x -> x.getClass()).orElse(null)
    );
  }

  @Test
  void suitableConveyor_forWhitespace() {
    suitableConveyor(" ", "N");
    suitableConveyor("\t", "N");
    suitableConveyor("\n", "N");
  }
}