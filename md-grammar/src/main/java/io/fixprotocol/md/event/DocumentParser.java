package io.fixprotocol.md.event;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.fixprotocol.md.antlr.MarkdownLexer;
import io.fixprotocol.md.antlr.MarkdownParser;
import io.fixprotocol.md.antlr.MarkdownParser.DocumentContext;
import io.fixprotocol.md.antlr.MarkdownEventSource;

public final class DocumentParser {

  private static class SyntaxErrorListener extends BaseErrorListener {
    private final Logger logger = LogManager.getLogger(getClass());

    private int errors = 0;

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
        int charPositionInLine, String msg, RecognitionException e) {
      errors++;
      logger.error("Markdown parser failed at line {} position {} due to {}", line,
          charPositionInLine, msg);
    }

    public int getErrors() {
      return errors;
    }
  }

  public void parse(InputStream inputStream, Consumer<Context> contextConsumer)
      throws IOException {
    MarkdownLexer lexer = new MarkdownLexer(CharStreams.fromStream(inputStream));
    MarkdownParser parser = new MarkdownParser(new CommonTokenStream(lexer));
    SyntaxErrorListener errorListener = new SyntaxErrorListener();
    parser.addErrorListener(errorListener);
    ParseTreeListener listener =
        new MarkdownEventSource(contextConsumer);
    ParseTreeWalker walker = new ParseTreeWalker();
    DocumentContext documentContext = parser.document();
    walker.walk(listener, documentContext);

    final int errors = errorListener.getErrors();
    if (errors > 0) {
      throw new IOException("Markdown parser failed; " + errors);
    }
  }

}