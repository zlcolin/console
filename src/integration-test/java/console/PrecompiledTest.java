package console;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.util.Random;

public class PrecompiledTest extends TestBase {

    @Rule
    public final SystemOutRule log = new SystemOutRule().enableLog();

    @Test
    public void crudTest() throws Exception {
        String tableName = "t_demo" + Math.abs(new Random().nextInt());
        String createSql = "create table " + tableName + "(name varchar, item_id varchar, item_name varchar, primary key(name))";
        precompiledFace.createTable(createSql);
        Assert.assertTrue(log.getLog().contains("Ok"));
        log.clearLog();

        precompiledFace.listDir(new String[]{"", "/tables"});
        Assert.assertTrue(log.getLog().contains(tableName));
        log.clearLog();

        String insertSql = "insert into " + tableName + " (name, item_id, item_name) values (fruit, 1, apple1)";
        precompiledFace.insert(insertSql);
        Assert.assertTrue(log.getLog().contains("OK"));
        log.clearLog();

        // select
        String selectSql = "select * from " + tableName + " where name = fruit";
        precompiledFace.select(selectSql);
        Assert.assertTrue(log.getLog().contains("fruit"));
        log.clearLog();

        selectSql = "select name, item_id, item_name from " + tableName + " where name = fruit";
        precompiledFace.select(selectSql);
        Assert.assertTrue(log.getLog().contains("fruit"));
        log.clearLog();

        insertSql = "insert into " + tableName + " (name, item_id, item_name) values (fruit2, 2, orange)";
        precompiledFace.insert(insertSql);
        Assert.assertTrue(log.getLog().contains("OK"));
        log.clearLog();

        selectSql = "select * from " + tableName + " where name >= fruit";
        precompiledFace.select(selectSql);
        Assert.assertTrue(log.getLog().contains("fruit2"));
        log.clearLog();

        selectSql = "select * from " + tableName + " where name >= fruit limit 0,1";
        precompiledFace.select(selectSql);
        Assert.assertFalse(log.getLog().contains("fruit2"));
        log.clearLog();

        String updateSql = "update " + tableName + " set item_name = orange where name = fruit";
        precompiledFace.update(updateSql);
        Assert.assertTrue(log.getLog().contains("1 row affected"));
        log.clearLog();

        String removeSql = "delete from " + tableName + " where name = fruit2";
        precompiledFace.remove(removeSql);
        Assert.assertTrue(log.getLog().contains("OK"));
        log.clearLog();

        selectSql = "select * from " + tableName + " where name = fruit2";
        precompiledFace.select(selectSql);
        Assert.assertTrue(log.getLog().contains("Empty"));
        log.clearLog();

        String newField = "comment" + Math.abs(new Random().nextInt());
        String alterSql = "alter table " + tableName + " add " + newField + " varchar";
        precompiledFace.alterTable(alterSql);
        Assert.assertTrue(log.getLog().contains("Ok"));
        log.clearLog();

        precompiledFace.desc(new String[]{"", tableName});
        Assert.assertTrue(log.getLog().contains(newField));
        log.clearLog();
    }

    @Test
    public void bfsTest() throws Exception {
        String contractAddress;
        String[] deployParams = {"", "contracts/solidity/HelloWorld.sol"};
        consoleContractFace.deploy(deployParams, "/apps");
        String[] split = log.getLog().split("\n");
        contractAddress = split[1].split(": ")[1];
        log.clearLog();

        String[] sendTxParams = {"", "HelloWorld", contractAddress, "set", "testHelloWorld"};
        consoleContractFace.call(sendTxParams, "/apps");
        Assert.assertTrue(log.getLog().contains("transaction status: 0"));
        log.clearLog();

        String[] callParams = {"", "HelloWorld", contractAddress, "get"};
        consoleContractFace.call(callParams, "/apps");
        Assert.assertTrue(log.getLog().contains("testHelloWorld"));
        log.clearLog();

        String version = String.valueOf(Math.abs(new Random().nextInt()));
        String[] deployWithLinkParams = {"", "contracts/solidity/HelloWorld.sol", "-l", "hello/" + version};
        consoleContractFace.deploy(deployWithLinkParams, "/apps");
        Assert.assertTrue(log.getLog().contains("/apps/hello/" + version));
        log.clearLog();

        String[] sendTxWithLinkParams = {"", "/apps/hello/" + version, "set", "testLink"};
        consoleContractFace.call(sendTxWithLinkParams, "/apps");
        Assert.assertTrue(log.getLog().contains("transaction status: 0"));
        log.clearLog();

        String[] callWithLinkParams = {"", "/apps/hello/" + version, "get"};
        consoleContractFace.call(callWithLinkParams, "/apps");
        Assert.assertTrue(log.getLog().contains("testLink"));
        log.clearLog();

        String[] lsParams = {"", "/apps"};
        precompiledFace.listDir(lsParams);
        Assert.assertTrue(log.getLog().contains("hello"));
        log.clearLog();

        String[] cdParams = {"", "/apps/hello"};
        precompiledFace.changeDir(cdParams);
        Assert.assertEquals("/apps/hello", precompiledFace.getPwd());
        precompiledFace.changeDir(lsParams);
        log.clearLog();


        String newDir = String.valueOf(Math.abs(new Random().nextInt()));
        String[] mkdirParams = {"", "/apps/hello" + newDir};
        precompiledFace.makeDir(mkdirParams);
        log.clearLog();
        precompiledFace.listDir(lsParams);
        Assert.assertTrue(log.getLog().contains("hello" + newDir));
        log.clearLog();
    }

    @Test
    public void authTest() throws Exception {
        if (!isAuthCheck){
            return;
        }
        String[] emptyParams = {};
        authFace.getCommitteeInfo(emptyParams);
        Assert.assertTrue(log.getLog().contains("Governor Address"));
        log.clearLog();
    }
}
