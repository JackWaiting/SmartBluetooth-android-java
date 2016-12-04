package com.smartcodeunited.lib.bluetooth.commands;

public class CommandProtocol {

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
