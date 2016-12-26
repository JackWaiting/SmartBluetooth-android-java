package com.smartcodeunited.lib.bluetooth.commands;

/**
 * Created by misparking on 16/12/4.
 */
public class CommandManager {
    /**
     * <p>
     * 判断自定义命令是否有效。
     * </p>
     *
     * @param command
     *            自定义通信命令 new byte[]{*********}。
     *
     * @return TRUE：自定义命令有效。FALSE：自定义命令无效。
     */
    public static boolean isCommandValid(byte[] command)
    {
        if ((command != null)&&(command[1] == command.length))
        {
            return true;
        }

        return false;
    }

    /**
     * 发命令通信的例子
     * @param deviceType
     * @param commandType
     * @param subCommandType
     * @param otherCommand
     * @return
     */
    public static byte[] build(int deviceType, int commandType,
                               int subCommandType,int... otherCommand)
    {

        // 自定义命令的总长度 ＝ (头+尾+长度) + (deviceType + commandType +
        // subCommandType) + otherCommand.length
        int length = 3 + 3 + otherCommand.length;

        byte[] command = new byte[length];

        // 自定义命令头部。
        command[0] = 1;
        // 自定义命令长度。
        command[1] = (byte) length;
        // 自定义命令设备类型如：灯、音箱、车载MP3等。
        command[2] = (byte) deviceType;
        // 自定义命令类型：校验、查询、控制、反馈。
        command[3] = (byte) commandType;
        // 自定义命令类型：子命令类型，依据实际情况不同而不同。
        command[4] = (byte) subCommandType;

        if ((otherCommand != null) && (otherCommand.length > 0))
        {
            for (int i = 0; i < otherCommand.length; i++)
            {
                command[i + 5] = (byte) otherCommand[i];
            }
        }

        command[length - 1] = 2;

        return command;
    }

    /**
     * <p>
     * 将byte数组转换成int。
     * </p>
     *
     * @param bytes
     *            byte数组。
     * @return int数。
     *
     * @since 1.0.0
     */
    public static int convertByteArrayToInt(byte[] bytes) {
        int value = 0;
        int byteLength = bytes.length;
        for (int i = 0; i < byteLength; i++) {
            int shift = (byteLength - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;
        }
        return value;
    }

    public static void send(byte[] command){

    }
}
