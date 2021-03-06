package org.systemexception.graphdbplayground.test;

import org.junit.Before;
import org.junit.Test;
import org.systemexception.graphdbplayground.exception.TerritoriesException;
import org.systemexception.graphdbplayground.model.Territories;
import org.systemexception.graphdbplayground.model.Territory;

import static org.junit.Assert.assertTrue;

/**
 * @author leo
 * @date 17/09/15 20:45
 */
public class TerritoriesTest {

	private final String territory = "Territory";
	private Territories sut;
	private Territory territoryA, territoryB;

	@Before
	public void setUp() {
		territoryA = new Territory("0", "1", "TerritoryA", territory);
		territoryB = new Territory("0", "2", "TerritoryB", territory);
	}

	@Test
	public void add_territories() throws TerritoriesException {
		sut = new Territories();
		sut.addTerritory(territoryA);
		sut.addTerritory(territoryB);
		assertTrue(sut.getTerritories().size() == 2);
	}

	@Test(expected = TerritoriesException.class)
	public void add_duplicate_territory() throws TerritoriesException {
		sut = new Territories();
		sut.addTerritory(territoryA);
		Territory badTerritory = new Territory("0", "1", "TerritoryBad", territory);
		sut.addTerritory(badTerritory);
	}
}