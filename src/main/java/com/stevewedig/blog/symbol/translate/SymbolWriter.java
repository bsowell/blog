package com.stevewedig.blog.symbol.translate;

import java.util.Map;

import com.stevewedig.blog.symbol.*;
import com.stevewedig.blog.translate.*;


/**
 * A Writer that converts from SymbolMap to Map&lt;String, String&gt;.
 */
public interface SymbolWriter extends Writer<Map<String, String>, SymbolMap> {

  <Value> String write(Symbol<Value> symbol, Value value);

  interface Builder {

    SymbolWriter build();

    <Value> Builder add(Symbol<Value> symbol, FormatWriter<Value> Writer);

    Builder add(Symbol<String> symbol);
  }

}
