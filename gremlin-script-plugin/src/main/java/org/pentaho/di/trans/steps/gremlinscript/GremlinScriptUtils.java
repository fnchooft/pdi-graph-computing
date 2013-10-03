package org.pentaho.di.trans.steps.gremlinscript;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraphFactory;

public class GremlinScriptUtils {
  
  private static ScriptEngineManager scriptEngineManager;
  
  /**
   * Instantiates the right scripting language interpreter, falling back to Gremlin-Groovy for backward compatibility
   * @param engineName
   * @return the desired ScriptEngine, or null if none can be found
   */
  public static ScriptEngine createNewScriptEngine(String engineName) {
    
    ScriptEngine scriptEngine = getScriptEngineManager().getEngineByName(engineName);
    if (scriptEngine == null) {//falls back to Gremlin-Groovy
      scriptEngine = getScriptEngineManager().getEngineByName("gremlin-groovy");
    }
    return scriptEngine;
  }
  
  public static ScriptEngineManager getScriptEngineManager() {
    if(scriptEngineManager == null) {
      System.setProperty("org.jruby.embed.localvariable.behavior", "persistent");//required for JRuby, transparent for others
      scriptEngineManager = new ScriptEngineManager(GremlinScriptUtils.class.getClassLoader());
    }
    return scriptEngineManager;
  }
  
  public static List<String> getScriptEngineNames() {
    List<String> scriptEngineNames = new ArrayList<String>();
    List<ScriptEngineFactory> engineFactories = getScriptEngineManager().getEngineFactories();
    if(engineFactories != null) {
      for(ScriptEngineFactory factory : engineFactories) {
        final String engineName = factory.getEngineName();
        if(engineName.contains("gremlin") || engineName.contains("Gremlin")) {
          scriptEngineNames.add(factory.getEngineName());
        }
      }
    }   
    return scriptEngineNames;
  }
  
  public static Graph createEmptyGraph() {
    // Create empty graph
    Graph emptyGraph = TinkerGraphFactory.createTinkerGraph();
    for(Edge e : emptyGraph.getEdges()) {
      emptyGraph.removeEdge(e);
    }
    for(Vertex v : emptyGraph.getVertices()) {
      emptyGraph.removeVertex(v);
    }
    return emptyGraph;
  }
}
