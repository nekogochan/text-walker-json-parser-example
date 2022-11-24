package nekogochan.example.jsonparser.exception;

import nekogochan.example.jsonparser.util.Strings;
import nekogochan.textwalker.charautomate.CharAutomate;

import java.util.StringJoiner;

import static java.util.Arrays.stream;

public class InvalidJsonException extends IllegalStateException {
  private final int pos;
  private final String rawJson;
  private final String comment;
  private final String parsedType;

  public InvalidJsonException(int pos, String rawJson, String comment, String parsedType) {
    this.pos = pos;
    this.rawJson = rawJson;
    this.comment = comment;
    this.parsedType = parsedType;
  }

  public InvalidJsonException(CharAutomate auto, String comment, String parsedType) {
    this(auto.currentPos(), new String(auto.source()), comment, parsedType);
  }

  private String message = null;

  @Override
  public String getMessage() {
    if (message != null) {
      return message;
    }
    var pos = this.pos;
    var lines = rawJson.split("\n");
    var lengths = stream(lines).mapToInt(String::length).map(i -> i + 1).toArray();
    var pos_line = 0;
    var pos_col = 0;
    while (true) {
      if (pos < lengths[pos_line]) {
        pos_col = pos;
        break;
      }
      pos -= lengths[pos_line];
      pos_line++;
    }
    var line = lines[pos_line];
    var msg = new StringJoiner("\n");
    msg.add("");
    msg.add(comment);
    msg.add("While parsing: " + parsedType);
    if (pos_line >= 2) {
      msg.add(lines[pos_line - 2]);
    }
    if (pos_line >= 1) {
      msg.add(lines[pos_line - 1]);
    }
    msg.add(line);
    msg.add("%s^ at line = %d; col = %d; pos = %d"
              .formatted(
                " ".repeat(pos_col),
                pos_line + 1,
                pos_col + 1,
                this.pos));
    message = msg.toString();
    return message;
  }
}
