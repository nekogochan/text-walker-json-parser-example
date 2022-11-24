package nekogochan.example.jsonparser.conveyor.nodes;

import nekogochan.example.jsonparser.model.JsonNode;
import nekogochan.textwalker.charautomate.CharAutomate;

public sealed interface JsonElementConveyor<T extends JsonNode> permits JsonArrayConveyor, JsonObjectConveyor, JsonValueConveyor {
  T parse(CharAutomate CharAutomate);
}
