package nekogochan.example.jsonparser.conveyor.nodes;

import nekogochan.example.jsonparser.conveyor.JsonConveyor;
import nekogochan.example.jsonparser.exception.InvalidJsonArrayException;
import nekogochan.example.jsonparser.model.JsonArray;
import nekogochan.example.jsonparser.model.JsonNode;
import nekogochan.textwalker.charautomate.CharAutomate;

import java.util.ArrayList;
import java.util.List;

import static nekogochan.example.jsonparser.util.JsonCharAutomateRules.currentIsWhitespace;

public final class JsonArrayConveyor implements JsonElementConveyor<JsonArray> {
  private final JsonConveyor jsonConveyor;

  public JsonArrayConveyor(JsonConveyor jsonConveyor) {
    this.jsonConveyor = jsonConveyor;
  }

  @Override
  public JsonArray parse(CharAutomate auto) {
    try {
      return new JsonArray(internalParse(auto));
    } catch (IndexOutOfBoundsException ex) {
      throw new InvalidJsonArrayException(auto, "Unexpected end of json array, expects ']'");
    }
  }

  private List<JsonNode> internalParse(CharAutomate auto) {
    if (auto.current() != '[') {
      throw new InvalidJsonArrayException(auto, "Expects '['");
    }
    auto.goForward();
    var arr = new ArrayList<JsonNode>();
    parseElements(auto, arr);
    auto.goForward();
    return arr;
  }

  private void parseElements(CharAutomate auto, ArrayList<JsonNode> arr) {
    auto.skipWhile(currentIsWhitespace());
    if (auto.current() == ']') {
      return;
    }
    while (true) {
      var elementConveyor = jsonConveyor.suitableConveyor(auto.current());
      if (elementConveyor == null) {
        throw new InvalidJsonArrayException(auto, "Unexpected token");
      }
      arr.add(elementConveyor.parse(auto));
      auto.skipWhile(currentIsWhitespace());
      if (auto.current() == ',') {
        auto.goForward();
        auto.skipWhile(currentIsWhitespace());
        continue;
      }
      if (auto.current() == ']') {
        break;
      }
      throw new InvalidJsonArrayException(auto, "Unexpected token");
    }
  }
}
