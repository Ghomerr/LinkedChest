package com.ghomerr.linkedchest.constants;

public interface Constants
{
	public static final String ROOT_PLUGINS_DIR = "plugins/";
	
	public static final String PLUGIN_DIR_NAME = "LinkedChest/";
	
	public static final String PLUGIN_DIR_PATH = ROOT_PLUGINS_DIR + PLUGIN_DIR_NAME;
	
	public static final String MASTERCHESTS_FILE_NAME = "masterchest.properties";
	
	public static final String LINKEDCHESTS_FILE_NAME = "linkedchest.properties";
	
	public static final String CONFIG_FILE_NAME = "configuration.properties";
	
	public static final String MASTERCHESTS_FILE_PATH = PLUGIN_DIR_PATH + MASTERCHESTS_FILE_NAME;
	
	public static final String LINKEDCHESTS_FILE_PATH = PLUGIN_DIR_PATH + LINKEDCHESTS_FILE_NAME;
	
	public static final String CONFIG_FILE_PATH = PLUGIN_DIR_PATH + CONFIG_FILE_NAME;
	
	public static final String LANGUAGE_TAG = "{{LANG}}";
	
	public static final String LANGUAGE_FILE_NAME = "messages_" + LANGUAGE_TAG + ".properties";
	
	public static final String LANGUAGE_FILE_PATH = PLUGIN_DIR_PATH + LANGUAGE_FILE_NAME;
	
	public static final String DEFAULT_LANGUAGE = "default";
	
	public static final String DISABLED = "disabled";
	
	public static final String TAG = "[LinkedChest] ";
	
	public static final String MINECRAFT = "Minecraft";
	
	public static final String WILDCARD = "*";
	
	public static final String BLANK_STRING_PATTERN = "^[ ]+$";
	
	public static final String CHEST_NAME_PATTERN = "^[A-Za-z0-9_-]{1,20}$";
	
	public static final String DELIMITER = ",";
	
	public static final String SEPARATOR = ";";
	
	public static final String EMPTY_STRING = " ";
	
	public static final String OPTION_DELIM = "~";
	
	public static final String EQUAL = "=";
	
	public static final String VAR_MESSAGE_TAG = "\\{[a-zA-Z0-9_-]+\\}";
	
	public static final char VAR_MESSAGE_OPEN = '{';
	
	public static final String PERMISSIONS_ROOT = "linkedchest.";
	
	public static final String PLUGIN_PERMISSIONS_BUKKIT = "PermissionsBukkit";
	
	public static final String PLUGIN_PERMISSIONS_EX = "PermissionsEx";
	
	public static final long ONE_MINUTE_IN_MILLISECS = 60000;

	public static final String NUMBER_PATTERN = "^[0-9]+$";
	
	public static final String HOPPER_CREATOR_KEY = "hoppercreator";
}
