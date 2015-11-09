package info.nich.visiblewifipsw;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public final class RootCmd {
    // 执行linux命令并且输出结果
    public static DataInputStream execRootCmd(String paramString) {
        // String result = "result : ";
        try {
            Process process = Runtime.getRuntime().exec("su");
            // 经过Root处理的android系统即有su命令
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            InputStream localInputStream = process.getInputStream();
            DataInputStream localDataInputStream = new DataInputStream(
                    localInputStream);
            String str = paramString + "\n";
            dataOutputStream.writeBytes(str);
            dataOutputStream.flush();

            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            return localDataInputStream;
        } catch (Exception localException) {
            localException.printStackTrace();
            return null;
        }
    }

    // 执行linux命令但不关注结果输出
    protected static int execRootCmdSilent(String paramString) {
        try {
            Process localProcess = Runtime.getRuntime().exec("su");
            Object localObject = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(
                    (OutputStream) localObject);
            String str = String.valueOf(paramString);
            localObject = str + "\n";
            localDataOutputStream.writeBytes((String) localObject);
            localDataOutputStream.flush();
            localDataOutputStream.writeBytes("exit\n");
            localDataOutputStream.flush();
            localProcess.waitFor();
            int result = localProcess.exitValue();
            return (Integer) result;
        } catch (Exception localException) {
            localException.printStackTrace();
            return -1;
        }
    }

    // 判断机器Android是否已经root，即是否获取root权限
    public static boolean haveRoot() {

        int i = execRootCmdSilent("echo test"); // 通过执行测试命令来检测
        if (i != -1) {
            return true;
        }
        return false;
    }

}