package com.ghomerr.linkedchest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.ChatColor;

import com.ghomerr.linkedchest.constants.Constants;
import com.ghomerr.linkedchest.enums.Configurations;
import com.ghomerr.linkedchest.enums.Messages;

public class MessagesUtils
{
	private static final Logger _LOGGER = Logger.getLogger(Constants.MINECRAFT);
	private static HashMap<Messages, String> _messages = new HashMap<Messages, String>();
	public static boolean useDefaultLanguage = false;

	public static boolean loadMessages(final String language)
	{
		if (!Constants.DEFAULT_LANGUAGE.equals(language))
		{
			useDefaultLanguage = false;
			
			FileInputStream in = null;

			final File languageFile = new File(Constants.LANGUAGE_FILE_PATH.replace(Constants.LANGUAGE_TAG, language.toLowerCase()));

			if (!languageFile.exists())
			{
				_LOGGER.warning(Constants.TAG + "Messages file named 'messages_" + language + ".properties' doesn't exist.");
				return false;
			}

			try
			{
				in = new FileInputStream(languageFile);
			}
			catch (final Throwable th)
			{
				_LOGGER.warning(Constants.TAG + "Error while opening messages file '" + languageFile + "'. See stack traces: ");
				th.printStackTrace();
			}

			final Properties pluginMessages = new Properties();
			try
			{
				final boolean useUtf8 = ConfigurationUtils.getConfigValueByType(Configurations.USE_UTF8, Boolean.class);
				if (useUtf8) 
				{
					pluginMessages.load(new InputStreamReader (in, Constants.ENCODING_UTF8));
				}
				else 
				{
					pluginMessages.load(in);
				}
				in.close();
			}
			catch (final IOException ex)
			{
				_LOGGER.severe(Constants.TAG + "Error while loading messages file '" + languageFile + "'. See stack traces: ");
				ex.printStackTrace();
				return false;
			}

			for (final Object key : pluginMessages.keySet())
			{
				try
				{
					final String strKey = String.valueOf(key).toUpperCase();
					final Messages message = Messages.valueOf(strKey);
					final String strMessage = pluginMessages.getProperty(strKey);

					if (StringUtils.isNotBlank(strMessage))
					{
						int nbMessageVariables;

						if (message.varNumber > 0)
						{
							nbMessageVariables = StringUtils.countChar(strMessage, Constants.VAR_MESSAGE_OPEN);
						}
						else
						{
							nbMessageVariables = 0;
						}

						if (nbMessageVariables == message.varNumber)
						{
							_messages.put(message, strMessage);
						}
						else
						{
							_LOGGER.warning(Constants.TAG + "There is not the correct number of message variable(s) for the message "
									+ message.name() + ". Default message value will be used for it.");
						}
					}
					else
					{
						_LOGGER.warning(Constants.TAG + "Invalid value for the message " + message.name()
								+ ". Default message value will be used for it.");
					}
				}
				catch (final IllegalArgumentException ex)
				{
					_LOGGER.warning(Constants.TAG + "Unknown message key '" + key + "' in the messages file.");
				}
				catch (final Throwable th)
				{
					_LOGGER.severe(Constants.TAG + "Error while reading messages file '" + languageFile + "'. See stack traces: ");
					th.printStackTrace();
					return false;
				}
			}
		}
		else
		{
			useDefaultLanguage = true;
		}
		
		_LOGGER.info(Constants.TAG + "Messages for the language '" + language + "' have been correctly loaded.");
		return true;
	}

	public static String get(final Messages message, final String... vars)
	{
		String completeMessage = _messages.get(message);
		if (useDefaultLanguage || completeMessage == null)
		{
			completeMessage = message.message;
		}

		if (completeMessage != null)
		{
			try
			{
				for (final String var : vars)
				{
					completeMessage = completeMessage.replaceFirst(Constants.VAR_MESSAGE_TAG, var);
				}
			}
			catch (final Throwable th)
			{
				_LOGGER.warning(Constants.TAG + "Error while parsing message : " + completeMessage);
				th.printStackTrace();
			}
		}
		else
		{
			completeMessage = ChatColor.RED + "<" + message.name() + ">";
		}

		return completeMessage;
	}

	public static String getWithColor(final Messages message, final ChatColor defaultColor)
	{
		return defaultColor + get(message);
	}

	public static String getWithColors(final Messages message, final ChatColor defaultColor, final ChatColor paramColor,
			final String... vars)
	{
//		if (DebugUtils.isDebugEnabled())
//		{
//			_LOGGER.info(Constants.TAG + "getWithColors(message=" + message + ", defaultColor=" + defaultColor + ", paramColor="
//					+ paramColor + ", vars=" + vars);
//		}

		String[] tab = vars.clone();

		for (int i = 0; i < tab.length; i++)
		{
			final String param = tab[i];
//			if (DebugUtils.isDebugEnabled())
//			{
//				_LOGGER.info(Constants.TAG + "Param before #" + i + " = " + param);
//			}

			final StringBuilder strBld = new StringBuilder();
			strBld.append(paramColor).append(param).append(defaultColor);
			tab[i] = strBld.toString();

//			if (DebugUtils.isDebugEnabled())
//			{
//				_LOGGER.info(Constants.TAG + "Param after #" + i + " = " + tab[i]);
//			}
		}

		return defaultColor + get(message, tab);
	}
}
