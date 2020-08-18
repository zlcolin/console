package console;

import console.client.ConsoleClientFace;
import console.contract.ConsoleContractFace;
import console.precompiled.PrecompiledFace;
import console.precompiled.permission.PermissionFace;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class TestBase {

    protected static ConsoleClientFace consoleClientFace;
    protected static PrecompiledFace precompiledFace;
    protected static PermissionFace permissionFace;
    protected static ConsoleContractFace consoleContractFace;
    protected static ConsoleInitializer consoleInitializer;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        consoleInitializer = new ConsoleInitializer();
        consoleInitializer.init(new String[0]);
        consoleClientFace = consoleInitializer.getConsoleClientFace();
        precompiledFace = consoleInitializer.getPrecompiledFace();
        permissionFace = consoleInitializer.getPermissionFace();
        consoleContractFace = consoleInitializer.getConsoleContractFace();
    }

    @AfterClass
    public static void setUpAfterClass() throws Exception {}
}
