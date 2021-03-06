package com.github.bestheroz.context.log;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import java.text.MessageFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.jdbc.internal.FormatStyle;

@Slf4j
public class P6spyPrettySqlFormatter implements MessageFormattingStrategy {
  @Override
  public String formatMessage(
      final int connectionId,
      final String now,
      final long elapsed,
      final String category,
      final String prepared,
      final String sql,
      final String url) {
    return MessageFormat.format(
        "OperationTime: {0}ms | connectionId : {1} | {2}{3}\n",
        elapsed,
        connectionId,
        category,
        StringUtils.isEmpty(sql) ? "" : "\n" + this.formatSql(category, sql));
  }

  private String formatSql(final String category, final String sql) {
    if (StringUtils.isEmpty(sql)) {
      return StringUtils.EMPTY;
    }

    if (Category.STATEMENT.getName().equals(category)) {
      if (sql.startsWith("create") || sql.startsWith("alter") || sql.startsWith("comment")) {
        return FormatStyle.DDL.getFormatter().format(sql);
      } else {
        return FormatStyle.HIGHLIGHT
            .getFormatter()
            .format(FormatStyle.BASIC.getFormatter().format(sql));
      }
    }
    return sql;
  }
}
