package com.stevewedig.blog.digraph;

import static com.stevewedig.blog.digraph.node.DownNodeLib.downNode;
import static com.stevewedig.blog.digraph.node.UpNodeLib.upNode;
import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.*;
import com.stevewedig.blog.digraph.node.*;
import com.stevewedig.blog.digraph.node_graph.*;

// example graph containing cycles (a->b->c->d->a, and a->e->a)
//
// id -> parentIdSet...
// a -> d, e
// b -> a
// c -> b
// d -> c
// e -> a
// f ->
//
// childIdSet <- id...
// b, e <- a
// c <- b
// d <- c
// a <- d
// a <- e
// <- f
public class TestSampleNodeGraph {

  // ===========================================================================
  // tests
  // ===========================================================================

  @Test
  public void testGraphWithUpNodes() {

    // a -> d, e
    // b -> a
    // c -> b
    // d -> c
    // e -> a
    // f ->
    Graph<String, UpNode<String>> graph =
        GraphLib.up(upNode("a", "d", "e"), upNode("b", "a"), upNode("c", "b"), upNode("d", "c"),
            upNode("e", "a"), upNode("f"));

    verifyNodeGraph(graph);
  }

  @Test
  public void testGraphWithDownNodes() {

    // b, e <- a
    // c <- b
    // d <- c
    // a <- d
    // a <- e
    // <- f
    Graph<String, DownNode<String>> graph =
        GraphLib.down(downNode("a", "b", "e"), downNode("b", "c"), downNode("c", "d"),
            downNode("d", "a"), downNode("e", "a"), downNode("f"));

    verifyNodeGraph(graph);
  }

  // ===========================================================================
  // verify graph
  // ===========================================================================

  private <Node> void verifyNodeGraph(Graph<String, Node> graph) {

    // =================================
    // nested idGraph
    // =================================

    // verify the nested IdGraph
    TestSampleIdGraph.verifyIdGraph(graph.idGraph());

    // verify that Graph properly implements IdGraph
    TestSampleIdGraph.verifyIdGraph(graph);

    // =================================
    // nodes
    // =================================

    Node a = graph.node("a");
    Node b = graph.node("b");
    Node c = graph.node("c");
    Node d = graph.node("d");
    Node e = graph.node("e");
    Node f = graph.node("f");

    ImmutableBiMap<String, Node> id__node =
        ImmutableBiMap.<String, Node>builder().put("a", a).put("b", b).put("c", c).put("d", d)
            .put("e", e).put("f", f).build();

    assertEquals(id__node, graph.id__node());

    // =================================
    // nodeSet
    // =================================

    @SuppressWarnings("unchecked")
    ImmutableSet<Node> nodeSet = ImmutableSet.of(a, b, c, d, e, f);

    assertEquals(nodeSet, graph.nodeSet());

    assertEquals(nodeSet.size(), graph.nodeSize());

    // =================================
    // optNodeList
    // =================================

    assertFalse(graph.optionalTopsortNodeList().isPresent());

    // =================================
    // unboundIds (nodes without ids)
    // =================================

    assertEquals(ImmutableSet.of(), graph.unboundIdSet());

    assertTrue(graph.isComplete());
    assertFalse(graph.isPartial());

    // =================================
    // parents
    // =================================

    // a -> d, e
    // b -> a
    // c -> b
    // d -> c
    // e -> a
    // f ->
    assertEquals(ImmutableSet.of(d, e), graph.parentNodeSet("a"));
    assertEquals(ImmutableSet.of(a), graph.parentNodeSet("b"));
    assertEquals(ImmutableSet.of(b), graph.parentNodeSet("c"));
    assertEquals(ImmutableSet.of(c), graph.parentNodeSet("d"));
    assertEquals(ImmutableSet.of(a), graph.parentNodeSet("e"));
    assertEquals(ImmutableSet.of(), graph.parentNodeSet("f"));

    // =================================
    // children
    // =================================

    // b, e <- a
    // c <- b
    // d <- c
    // a <- d
    // a <- e
    // <- f
    assertEquals(ImmutableSet.of(b, e), graph.childNodeSet("a"));
    assertEquals(ImmutableSet.of(c), graph.childNodeSet("b"));
    assertEquals(ImmutableSet.of(d), graph.childNodeSet("c"));
    assertEquals(ImmutableSet.of(a), graph.childNodeSet("d"));
    assertEquals(ImmutableSet.of(a), graph.childNodeSet("e"));
    assertEquals(ImmutableSet.of(), graph.childNodeSet("f"));

    // =================================
    // ancestors
    // =================================

    assertEquals(ImmutableSet.of(b, c, d, e), graph.ancestorNodeSet("a"));
    assertEquals(ImmutableSet.of(a, c, d, e), graph.ancestorNodeSet("b"));
    assertEquals(ImmutableSet.of(a, b, d, e), graph.ancestorNodeSet("c"));
    assertEquals(ImmutableSet.of(a, b, c, e), graph.ancestorNodeSet("d"));
    assertEquals(ImmutableSet.of(a, b, c, d), graph.ancestorNodeSet("e"));
    assertEquals(ImmutableSet.of(), graph.ancestorNodeSet("f"));

    // =================================
    // descendants
    // =================================

    assertEquals(ImmutableSet.of(b, c, d, e), graph.descendantNodeSet("a"));
    assertEquals(ImmutableSet.of(a, c, d, e), graph.descendantNodeSet("b"));
    assertEquals(ImmutableSet.of(a, b, d, e), graph.descendantNodeSet("c"));
    assertEquals(ImmutableSet.of(a, b, c, e), graph.descendantNodeSet("d"));
    assertEquals(ImmutableSet.of(a, b, c, d), graph.descendantNodeSet("e"));
    assertEquals(ImmutableSet.of(), graph.descendantNodeSet("f"));

    // =================================
    // roots (sources)
    // =================================

    assertEquals(ImmutableSet.of(f), graph.rootNodeSet());

    // =================================
    // leaves (sinks)
    // =================================

    assertEquals(ImmutableSet.of(f), graph.leafNodeSet());

    // =================================
    // implementing set
    // =================================

    assertEquals(nodeSet.size(), graph.size());
    assertFalse(graph.isEmpty());

    assertTrue(graph.contains(a));
    assertFalse(graph.contains(new Object()));

    assertTrue(graph.containsAll(nodeSet));
    assertFalse(graph.containsAll(ImmutableSet.of(a, new Object())));

    assertEquals(graph.nodeSet(), ImmutableSet.copyOf(graph.iterator()));
    assertEquals(graph.nodeSet(), ImmutableSet.copyOf(graph.toArray()));
    assertEquals(graph.nodeSet(), ImmutableSet.copyOf(graph));

  }

}
