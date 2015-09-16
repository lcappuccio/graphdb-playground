/**
 *
 * @author leo
 * @date 28/02/2015 20:03
 *
 */
package org.systemexception.graphdbplayground.test;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.systemexception.graphdbplayground.test.classes.Person;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

public class TestOrient {

	private final static Logger log = Logger.getLogger(TestOrient.class.getCanonicalName());
	private static OrientGraphFactory orientGraphFactory;
	private static OrientGraphNoTx orientGraph;
	private static final String dbPath = "orientdb_test_database";
	private static final File dbFolder = new File(dbPath);

	@BeforeClass
	public static void setUp() {
		if (dbFolder.exists()) {
			log.info("Deleting previous database folder");
			boolean deleted = dbFolder.delete();
			if (!deleted) {
				log.info("Not found");
			}
		}
		orientGraphFactory = new OrientGraphFactory("plocal:" + dbPath, "admin", "admin");
		orientGraph = orientGraphFactory.getNoTx();
	}

	@AfterClass
	public static void tearDown() {
		orientGraph.drop();
		orientGraph.shutdown();
		orientGraphFactory.close();
		log.log(Level.INFO, "Deleting test database");
		if (dbFolder.exists()) {
			log.info("Found database folder");
		}
		boolean deleted = dbFolder.delete();
		if (!deleted) {
			log.info("Wasn't able to delete");
		}
	}

	@Test
	public void has_a_graph_factory() {
		assertTrue(orientGraphFactory.exists());
	}

	@Test
	public void add_vertex() {
		Person person = new Person("John", "Doe", 40);
		Vertex vperson = addPersonVertex(person);
		log.log(Level.INFO, "Added record {0}", vperson.getId());
		for (Vertex vertex : orientGraph.getVertices("name", "John")) {
			assertTrue(person.getName().equals(vertex.getProperty("name")));
		}
	}

	@Test
	public void create_edge_for_vertex() {
		Person person1 = new Person("Foo", "Wombat", 35);
		Vertex vperson1 = addPersonVertex(person1);
		log.log(Level.INFO, "Added record {0}", vperson1.getId());
		Person person2 = new Person("Faa", "Wombat", 30);
		Vertex vperson2 = addPersonVertex(person2);
		log.log(Level.INFO, "Added record {0}", vperson2.getId());
		Edge edge = addPersonEdge(vperson1, vperson2);
		assertTrue(edge.getVertex(Direction.IN).equals(vperson2) && edge.getVertex(Direction.OUT).equals(vperson1));
	}

	/**
	 * Creates a vertex for a Person
	 *
	 * @param person a person
	 * @return a vertex object
	 */
	private Vertex addPersonVertex(Person person) {
		Vertex vperson = orientGraph.addVertex(null);
		vperson.setProperty("name", person.getName());
		vperson.setProperty("surname", person.getSurname());
		vperson.setProperty("age", person.getAge());
		log.log(Level.INFO, "Added vertex: {0}", person.getName());
		return vperson;
	}

	/**
	 * Creates an edge from vperson1 to vperson2
	 *
	 * @param vperson1 the source person
	 * @param vperson2 the destination person
	 * @return an edge object
	 */
	private Edge addPersonEdge(Vertex vperson1, Vertex vperson2) {
		Edge edge = orientGraph.addEdge(null, vperson1, vperson2, "brothers");
		// Add a property to the edge, otherwise it will not be created. Nice feature.
		// see: https://github.com/orientechnologies/orientdb/wiki/Graph-Database-Tinkerpop
		edge.setProperty("type", "relation");
		log.log(Level.INFO, "Added edge {0}", edge.getId());
		return edge;
	}
}