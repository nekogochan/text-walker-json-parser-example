package nekogochan.example.jsonparser.exception;

import nekogochan.textwalker.charautomate.CharAutomate;

public class InvalidJsonObjectException extends InvalidJsonException {
  public InvalidJsonObjectException(CharAutomate auto, String comment) {
    super(auto, comment, "object");
  }
}
