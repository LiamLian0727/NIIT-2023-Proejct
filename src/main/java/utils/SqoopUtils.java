package utils;

import static config.Config.*;

public class SqoopUtils {

    public static void sqoopExec(String sqoopCommand){
        SSHShellUtils shell = new SSHShellUtils(ZOOKEEPER_QUOEUM, LINUX_USER_NAME, LINUX_USER_PASSWORD);
        String execLog = shell.execCommand(sqoopCommand);
        System.out.println(execLog);
    }

    public static void main(String[] args) {
        String sqoopCommand = SQOOP_HOME + " import --connect " + JDBC_URL +
                " --username " + DATABASE_USER +
                " --password " + DATABASE_PASSWORD +
                " --query 'select * from user where $CONDITIONS'" +
                " --m 2" +
                " --as-textfile" +
                " --split-by UserName" +
                " --target-dir /sqoop1" +
                " --fields-terminated-by ','" +
                " --lines-terminated-by '\\n'"; // 要执行的 Sqoop 命令，根据需要自行更改
        sqoopCommand = sqoopCommand.replaceAll("&", "\\\\&");
        System.out.println(sqoopCommand);
        sqoopExec(sqoopCommand);
    }

}