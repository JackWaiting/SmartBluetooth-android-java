package com.smartcodeunited.lib.bluetooth.commands;

/**
 * Created by misparking on 16/12/4.
 */
public class CommandManager {
    /**
     * <p>
     * Determine whether custom commands are valid.
     * </p>
     *
     * @param command
     *            Custom communication command： new byte[]{*********}。
     *
     * @return TRUE：valid. FALSE：Invalid
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
     * Send command communication example
     * @param deviceType
     * @param commandType
     * @param subCommandType
     * @param otherCommand
     * @return
     */
    public static byte[] build(int deviceType, int commandType,
                               int subCommandType,int... otherCommand)
    {

        // Total length of custom command ＝ (head+tail+length) + (deviceType + commandType +
        // subCommandType) + otherCommand.length
        int length = 3 + 3 + otherCommand.length;

        byte[] command = new byte[length];

        // Custom command header
        command[0] = 1;
        // Custom command length
        command[1] = (byte) length;
        // Custom command device types such as: lights, speakers, car MP3, etc..
        command[2] = (byte) deviceType;
        // Custom command types: verification, inquery, control, feedback.
        command[3] = (byte) commandType;
        // Custom command type: sub command type, depending on the actual situation and different.
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
     * Convert byte array into int.
     * </p>
     *
     * @param bytes
     * @return int。
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
