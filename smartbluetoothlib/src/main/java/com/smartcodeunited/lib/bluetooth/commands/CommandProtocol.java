package com.smartcodeunited.lib.bluetooth.commands;

public class CommandProtocol {

	/**
	 * <p>
	 * 自定义命令的头部。
	 * </p>
	 * 
	 * @since 1.0.0
	 */
	public static final int COMMAND_HEADER = 13;
	/**
	 * <p>
	 * 自定义命令的最小长度。
	 * </p>
	 * 
	 * @since 1.0.0
	 */
	public static final int COMMAND_LENGTH_MINIMUM = 7;
	/**
	 * <p>
	 * 自定义命令的尾部。
	 * </p>
	 * 
	 * @since 1.0.0
	 */
	public static final int COMMAND_TAIL = 14;
	
	/**
	 *设备识别类型
	 */
	public static final class DeviceType
	{
		/**
		 * <p>
		 * 默认蓝牙设备。
		 * </p>
		 *
		 * @since 1.0.0
		 */
		public static final int DEFAULT = 0x0;

		/**
		 * <p>
		 * 蓝牙普通灯设备。
		 * </p>
		 *
		 * @since 1.0.0
		 */
		public static final int LAMP_COMMON = 0x1;

		/**
		 * <p>
		 * 蓝牙彩灯设备。
		 * </p>
		 *
		 * @since 1.0.0
		 */
		public static final int LAMP_COLOR = 0x2;

		/**
		 * <p>
		 * 蓝牙车载MP3设备。
		 * </p>
		 *
		 * @since 1.0.0
		 */
		public static final int FM_SENDER = 0x3;

		/**
		 * <p>
		 * 蓝牙故事机设备。
		 * </p>
		 *
		 * @since 1.0.0
		 */
		public static final int STORY_MACHINE = 4;

		/**
		 * <p>
		 * 蓝牙音箱设备。
		 * </p>
		 *
		 * @since 1.0.0
		 */
		public static final int SOUND_BOX = 5;

		/**
		 * <p>
		 * 蓝牙遥控器设备。
		 * </p>
		 *
		 * @since 1.0.0
		 */
		public static final int REMOTE_CONTROLLER = 6;

		/**
		 * <p>
		 * 蓝牙点读笔设备。
		 * </p>
		 *
		 * @since 1.0.0
		 */
		public static final int READING_PEN = 7;
		/**
		 *  <p>
		 *  蓝牙闹钟
		 * </p>
		 *
		 * @since 1.0.6
		 */
		public static final int ALARM_CLOCK = 8;
		/**
		 * <p>
		 * OBD设备。
		 * </p>
		 *
		 * @since 1.0.5
		 */
		public static final int OBD = 9;
		/**
		 * <p>
		 * 智能积木
		 * </p>
		 *
		 * @since 1.0.7
		 */
		public static final int BUILDING_BLOCK = 0x0A;
		/**
		 * <p>
		 * 存钱罐
		 * </p>
		 *
		 * @since 1.0.7
		 */
		public static final int PIGGY_BANK = 0x0B;
		/**
		 * <p>
		 * LED Panel
		 * </p>
		 *
		 * @since 1.0.9
		 */
		public static final int LED_PANEL = 0x0C;

		/**
		 * <p>
		 * 通用蓝牙设备。
		 * </p>
		 *
		 * @since 1.0.0
		 */
		public static final int ALL = 255;
	}
	
	/**
	 * <p>
	 * 命令类型：大类型分四类：校验、查询、控制、反馈。
	 * </p>
	 * 
	 * @since 1.0.0
	 */
	public static class Type
	{
		/**
		 * <p>
		 * 校验。
		 * </p>
		 * 
		 * @since 1.0.0
		 */
		public static final int VERIFICATION = 1;

		/**
		 * <p>
		 * 查询。
		 * </p>
		 * 
		 * @since 1.0.0
		 */
		public static final int INQUIRY = 2;
		/**
		 * <p>
		 * 查询反馈
		 * </p>
		 * 
		 * @since 1.0.0
		 */
		public static final int FEEDBACK_INQUIRY = 2;

		/**
		 * <p>
		 * 控制。
		 * </p>
		 * 
		 * @since 1.0.0
		 */
		public static final int CONTROL = 3;

		/**
		 * <p>
		 * 控制反馈。
		 * </p>
		 * 
		 * @since 1.0.0
		 */
		public static final int FEEDBACK_CONTROL = 4;

	}

	/**
	 *功能位
	 */
	public static final class Function
	{

	}
}
