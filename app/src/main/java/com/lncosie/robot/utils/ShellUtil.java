package com.lncosie.robot.utils;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by qishui on 16/3/9.
 * shell命令
 */
public class ShellUtil {
    static OutputStreamWriter os;
    static Process su = null;

    static public void openShell() {
        if (su == null)
            try {
                su = Runtime.getRuntime().exec("su");
                os = new OutputStreamWriter(su.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    static public void closeShell() {
        if (su != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            su.destroy();
            su = null;
        }

    }

    static public void executeCmd(String command) {
        openShell();
        try {
            os.write(command);
            os.write("\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//
//public class ShellUtil {
//    public static ShellCmdRsp executeCmd(String... strings) {
//        InputStream inputStream = null;
//        DataOutputStream outputStream = null;
//        try {
//            Process su = Runtime.getRuntime().exec("su");
//            outputStream = new DataOutputStream(su.getOutputStream());
//            for (String str : strings) {
//                outputStream.writeBytes(str + "\n");
//            }
//            outputStream.writeBytes("exit\n");
//            outputStream.flush();
//
//            su.waitFor();
//
//            int exit = su.exitValue();
//
//            Log.i(GlobalData.TAG, "exit " + exit);
//            if (exit != 0) {
//                return new ShellCmdRsp(null, ShellCmdRsp.FAIL);
//            }
//
//            inputStream = su.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            List<String> respList = new ArrayList<>();
//            String nextLine = reader.readLine();
//            while (nextLine != null) {
//                respList.add(nextLine);
//                nextLine = reader.readLine();
//            }
//
//            return new ShellCmdRsp(respList, ShellCmdRsp.SUCC);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            close(inputStream);
//            close(outputStream);
//        }
//
//        return new ShellCmdRsp(null, ShellCmdRsp.FAIL);
//
//    }
//
//    public static void close(Closeable c) {
//        if (c == null) return;
//        try {
//            c.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
