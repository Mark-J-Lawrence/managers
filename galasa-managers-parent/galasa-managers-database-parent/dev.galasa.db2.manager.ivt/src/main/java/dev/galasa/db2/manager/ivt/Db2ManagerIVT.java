/*
 * Copyright contributors to the Galasa project
 */
package dev.galasa.db2.manager.ivt;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import dev.galasa.AfterClass;
import dev.galasa.Test;
import dev.galasa.artifact.BundleResources;
import dev.galasa.artifact.IBundleResources;
import dev.galasa.artifact.TestBundleResourceException;
import dev.galasa.db2.Db2Instance;
import dev.galasa.db2.Db2ManagerException;
import dev.galasa.db2.Db2Schema;
import dev.galasa.db2.IDb2Instance;
import dev.galasa.db2.IDb2Schema;
import dev.galasa.db2.IResultMap;

@Test
public class Db2ManagerIVT {
	@Db2Instance (tag = "PRIMARY")
	public IDb2Instance db2;
	
	@Db2Schema (tag = "PRIMARY", db2Tag = "PRIMARY", archive = true)
	public IDb2Schema schema;
	
	@BundleResources
    public IBundleResources resources;
	
	final private String tableName = "TESTTABLE"; 
	
	@Test
	public void TestDb2NotNull() {
		assertThat(db2).isNotNull();
	}
	
	@Test
	public void TestSchemaNotNull() {
		assertThat(schema).isNotNull();
	}
	
	// Test Basic SQL Statements
	@Test
	public void CreateTable() throws Db2ManagerException {
		String tableDef = "("
				+ "ID INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,"
				+ "NAME VARCHAR(20),"
				+ "PRIMARY KEY (ID)"
				+ ");";
		
		IResultMap rm = schema.executeSql("CREATE TABLE "+this.tableName+tableDef);
		assertThat(rm.getIntValue("RC")).isEqualByComparingTo(0);
		rm = schema.executeSql("INSERT INTO "+this.tableName+"(NAME) VALUES "
				+ "('john'),"
				+ "('steve'),"
				+ "('bob'),"
				+ "('craig'),"
				+ "('trevor');");
		assertThat(rm.getIntValue("RC")).isEqualByComparingTo(5);
	}
	
	@Test
	public void StatementExecute() throws SQLException, Db2ManagerException {
		IResultMap rm = schema.executeSql("SELECT * FROM "+this.tableName);
			assertThat(rm.getIntValue("ID")).isEqualTo(1);
			assertThat(rm.getStringValue("NAME")).isEqualTo("john");
	}
	
	@Test
	public void StatementExecuteList() throws SQLException, Db2ManagerException {
		List<IResultMap> rmList = schema.executeSqlList("SELECT * FROM "+this.tableName);
		assertThat(rmList).hasSize(5);
	}
	
	// Run several SQL statements from file
	@Test
	public void CSVLoadTable() throws TestBundleResourceException, Db2ManagerException {
		InputStream in = resources.retrieveFile("/Batch.txt");
		schema.executeSqlFile(in);
	}	
	
	// Test Parameterized Statements
	@Test
	public void SelectParam() throws Db2ManagerException {
		IResultMap rm = schema.executeSql("SELECT * FROM "+this.tableName+" WHERE ID=?", 2);
		assertThat(rm.getStringValue("NAME")).isEqualTo("steve");
		rm = schema.executeSql("SELECT * FROM "+this.tableName+" WHERE NAME=?", "john");
		assertThat(rm.getIntValue("ID")).isEqualTo(1);
	}
	
	// Remove Tables used
	@AfterClass
	public void DropTable() throws SQLException, Db2ManagerException {
		IResultMap rm = schema.executeSql("drop table "+this.tableName);
		assertThat(rm.getIntValue("RC")).isEqualByComparingTo(0);	
	}
}