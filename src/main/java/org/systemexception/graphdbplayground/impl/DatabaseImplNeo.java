/**
 * @author leo
 * @date 16/09/15 15:53
 */
package org.systemexception.graphdbplayground.impl;

import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONWriter;

import org.neo4j.index.impl.lucene.LowerCaseKeywordAnalyzer;
import org.neo4j.kernel.impl.util.FileUtils;
import org.systemexception.graphdbplayground.enums.GraphDatabaseConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DatabaseImplNeo extends DatabaseImplDefault {

	private Neo4jGraph graph;
	private String dbFolder;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialSetup(String dbFolder, String storageType) {
		this.dbFolder = dbFolder;
		graph = new Neo4jGraph(dbFolder);
		super.graph = graph;
		index = graph.createIndex(GraphDatabaseConfiguration.VERTEX_INDEX.toString(), Vertex.class, new Parameter
				(GraphDatabaseConfiguration.NEO_INDEX_PARAMETER.toString(), LowerCaseKeywordAnalyzer.class.getName()));
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void exportDatabase(String exportFileName) throws IOException {
        OutputStream outStream = new FileOutputStream(exportFileName);
        GraphSONWriter.outputGraph(graph, outStream);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void backupDatabase(String backupFileName) {
		logger.error("Unsupported Neo backup operation, use export instead");
		throw new UnsupportedOperationException();
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void drop() throws IOException {
        graph.shutdown();
        FileUtils.deleteRecursively(new File(dbFolder));
    }
}
