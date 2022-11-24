package nekogochan.example.jsonparser.conveyor.nodes;

import nekogochan.example.jsonparser.conveyor.JsonConveyor;
import nekogochan.example.jsonparser.exception.InvalidJsonArrayException;
import nekogochan.example.jsonparser.exception.InvalidJsonException;
import nekogochan.example.jsonparser.model.JsonArray;
import nekogochan.example.jsonparser.model.JsonNode;
import nekogochan.example.jsonparser.model.JsonValue;
import nekogochan.textwalker.charautomate.CharArrayAutomate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static nekogochan.example.jsonparser.util.TestUtils.args;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonArrayConveyorTest {

  public static Stream<Arguments> validValueArrays_args() {
    return args(Map.of(
      "[1, 2, 3]", "1,2,3",
      "[1,2,3]", "1,2,3",
      "[]", ""
    ), x -> {
      var y = x.split(",");
      if (y.length == 1 && Objects.equals(y[0], "")) {
        y = new String[0];
      }
      return stream(y).mapToInt(Integer::parseInt)
                      .toArray();
    });
  }

  public static Stream<Arguments> invalidValueArrays_args() {
    return args(List.of(
      "   []    ",
      "     [   1,      2, 3]",
      "[1, 2, 3",
      "[1, 2, 3,]",
      "[,,,]"
    ));
  }

  @ParameterizedTest
  @MethodSource("invalidValueArrays_args")
  void invalidValueArrays(String rawJson) {
    var arrayConveyor = new JsonConveyor().forArray();
    assertThrows(InvalidJsonException.class, () -> arrayConveyor.parse(new CharArrayAutomate(rawJson)));
  }

  @ParameterizedTest
  @MethodSource("validValueArrays_args")
  void validValueArrays(String rawJson, int[] expectedValues) {
    var jsonConveyor = new JsonConveyor().forArray();

    var values = jsonConveyor.parse(new CharArrayAutomate(rawJson))
                             .values()
                             .stream()
                             .mapToInt(JsonNode::asInt)
                             .toArray();

    assertArrayEquals(expectedValues, values);
  }

  @Test
  void innerArrayTest() {
    var rawJson = """
                  [
                    1,
                    2,
                    [3, 4, 5],
                    6]
                  """;
    var conveyor = new JsonConveyor().forArray();

    var json = conveyor.parse(new CharArrayAutomate(rawJson)).values();

    assertEquals(1, json.get(0).asInt());
    assertEquals(2, json.get(1).asInt());
    assertEquals(JsonArray.class, json.get(2).getClass());
    assertEquals(3, json.get(2).asArr().get(0).asInt());
    assertEquals(4, json.get(2).asArr().get(1).asInt());
    assertEquals(5, json.get(2).asArr().get(2).asInt());
    assertEquals(6, json.get(3).asInt());
  }

  @Test
  void skipEndOfArrayAfterParse() {
    var raw = "[1, 2, 3]       ";
    var auto = new CharArrayAutomate(raw);
    var conveyor = new JsonConveyor().forArray();
    conveyor.parse(auto);
    assertEquals(' ', auto.current());
    assertEquals(9, auto.currentPos());
  }
}