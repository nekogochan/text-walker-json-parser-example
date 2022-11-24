package nekogochan.example.jsonparser.conveyor.nodes;

import nekogochan.example.jsonparser.conveyor.JsonConveyor;
import nekogochan.example.jsonparser.model.JsonNode;
import nekogochan.example.jsonparser.model.JsonValue;
import nekogochan.textwalker.charautomate.CharArrayAutomate;
import org.junit.jupiter.api.Test;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonObjectConveyorTest {

  @Test
  void simpleObjectTest() {
    var rawJson = """
                  {"love": "anime",
                   "dick-size": 10,
                   "cat" : "Tomas"}""";
    var conveyor = new JsonConveyor().forObject();
    var obj = conveyor.parse(new CharArrayAutomate(rawJson));
    assertEquals("anime", obj.get("love").asStr());
    assertEquals(10, obj.get("dick-size").asInt());
    assertEquals("Tomas", obj.get("cat").asStr());
  }

  @Test
  void simpleNestedObjectTest() {
    var rawJson = """
                  {"start": {"x": 10,
                             "y": 20},
                   "end": {"x": 20,
                           "y": 40}}""";
    var conveyor = new JsonConveyor().forObject();
    var obj = conveyor.parse(new CharArrayAutomate(rawJson));
    assertEquals("{\"start\":{\"x\":10,\"y\":20},\"end\":{\"x\":20,\"y\":40}}", obj.toRawJson());
  }

  @Test
  void simpleNestedArrayTest() {
    var rawJson = """
                  {"type": "Cat",
                   "value": [
                       "Tomas",
                       "Oleg",
                       "Dmitry"
                   ],
                   "size": 3}""";
    var conveyor = new JsonConveyor().forObject();
    var obj = conveyor.parse(new CharArrayAutomate(rawJson));
    assertEquals("{\"type\":\"Cat\",\"value\":[\"Tomas\",\"Oleg\",\"Dmitry\"],\"size\":3}", obj.toRawJson());
  }

  @Test
  void testValuesAreValid() {
    var rawJson = """
                  {"widget": {
                      "debug": "on",
                      "window": {
                          "title": "Sample Konfabulator Widget",
                          "name": "main_window",
                          "width": 500,
                          "height": 500
                      },
                      "images": [
                          "Images/Sun.png",
                          "Images/Moon.png"
                      ]
                  }}""";
    var jsonConveyor = new JsonConveyor();
    var objectConveyor = jsonConveyor.forObject();
    var json = objectConveyor.parse(new CharArrayAutomate(rawJson));

    var widget = json.get("widget").asObj();

    assertEquals("on", widget.get("debug").asStr());

    var window = widget.get("window").asObj();
    assertEquals("Sample Konfabulator Widget", window.get("title").asStr());
    assertEquals("main_window", window.get("name").asStr());
    assertEquals(500, window.get("width").asInt());
    assertEquals(500, window.get("height").asInt());

    var images = widget.get("images").asArr()
                       .stream()
                       .map(JsonNode::asStr)
                       .collect(toList());
    assertTrue(images.contains("Images/Sun.png"));
    assertTrue(images.contains("Images/Moon.png"));
  }
}