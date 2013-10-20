LinkedChest by Ghomerr

INTRODUCTION:
	This plugin allows to link chests together. 
	You can link normal and trapped chests. 
	Master trapped chests will generate current, but not Linked trapped chests.
	A command is used to create a master chest. 
	Then, use a sign to link another chest to a master chest. 
	The sign must contain the chest name between wildcards (*) on the first line. 
	The sign can be placed around the chest, just above and around this block and two blocks under the chest. 
	You wont be able to destroy a master chest, unless you remove it with the REMOVE command. 
	Master chests are protected against explosions. Linked chests are not protected at all. 
	You wont be able to place a chest near a simple master chest. Place it before adding the master chest.
	Double master chests seems to not work in versions older than 1.2.3 of Craftbukkit.
	You cannot open linked or master chests if a block is in hand, to avoid a conflict with the shift-click.
	You can easily move a master chest while keeping all linked chests. You can even move a single master chest to a double master chest and vice versa.

1) How to install:
	Only put the "LinkedChest-x.x.x.jar" file in your plugins directory, inside the minecraft server directory.
	At the first run, the directory "LinkedChest" will be created, with the default configuration file "configuration.properties".
	Place in the "LinkedChest" directory the "message_xx.properties" file corresponding to the language configured in the configuration file.
	Without any messages files, or with a default messages configuration, there will be internal default English messages which will be used ingame.
	Plugin data is stored in the files "masterchest.properties" and "linkedchest.properties". 
	The first one contains chest names, their locations and options.
	The second file contains the linked chest locations, and master chests names.
	
2) How to configure:
	Fill the configuration file linkedchest.config has you want, using these parameters:
		debug: Values (true|false) -> Enable/Disable the debug mode. (There is barely no debug messages for now)
		displayopenmessage : Values (true|false) -> Display or not the messages when linked or master chests are open.
		language: Values (language code) -> Select the messages file corresponding to this configuration. You can use "default" value for default English messages.
		usepermissions: Values (true|false) -> Enable permissions (PermissionsBukkit, PermissionsEx and Native Bukkit's Permissions system are supported).
		saveallperiod: Values (number or "disabled") -> Enabled forcing save-all command when a linked/master chest is closed, or when there is a plugins /reload.
	
3) Multilanguage messages:
	You can choose the language of displayed messages ingame.
	Only put a language code in the language configuration and get or make the corresponding messages file.
	For example, with a configuration "language=en", you have to use a file named "messages_en.properties".
	You must let the messages variables inside braces "{}" to let the plugin replace them by dynamic parameters, as chest names.
	Messages variables names does not matter. It is based on variables order. You must keep the same number of variables, otherwise the default message will be used.
	If a message is missing, it will be its default value which will be displayed ingame.
	You can also use messages file in UTF-8 encoding. Just enable it in the configuration: useutf8=true.
	
4) Permissions Nodes:
	Commands permissions
		linkedchest.add
		linkedchest.adminchest
		linkedchest.aliases
		linkedchest.config
		linkedchest.details
		linkedchest.help
		linkedchest.link
		linkedchest.list
		linkedchest.move
		linkedchest.openremote
		linkedchest.positions
		linkedchest.remove
		linkedchest.unlink
		linkedchest.version
		
	Actions permissions:
		linkedchest.openadminchest
		linkedchest.openmasterchest
		linkedchest.openlinkedchest
	
5) How to use:
	This plugin use the main command /linkedchest. Available aliases: /lc, /lchest, /linkchest
	Following commands use parameters. <c> = command, <n> = chest name, <o> = options, <p> = conf param, <v> = conf value, <r> = researched letters
	Admins Commands:
		/lc add <n> <o> : create a master chest from the target chest. Only "a" option is available, to make an admin-only chest
		/lc admin <n> : toggle the admin status of the given chest
		/lc config <p> <v> : display the current config or update a configuration parameter. Boolean parameters can be toggle if no value is typed.
		/lc link <n> : link the targeted chest to a master chest
		/lc open : remotely open a chest
		/lc positions <n> : list all linked chests positions of the chosen master chest. Double linked chests will appear twice for each part of the double chest. 
		/lc move <n> : move the given master chest to the targeted chest. The target chest must be empty and must be able to contain all items from the chosen master chest. Does not unlink the master chest if it was previously linked.
		/lc remove <n> : remove a master chest
		/lc unlink (<n>) : unlink the targeted chest or all chests of the given master chests
		
	Users commands:
		/lc alias <c> : display the aliases of the given command
		/lc details <n> : display the details of the given chest
		/lc help <c> : display general help without parameter and the help message of the given command
		/lc list <r>: display chests list or only chests which start with letters in <r> (use empty parameter or * to display all chests)
		/lc version : display the current plugin version
		
6) Known issue:
    - Chest is not a chest:
        When you place a chest surrounded by natural snow, you may encounter difficulties to use commands on this chest. 
        This is because the snow is selected instead of the chest. 
        Try to use commands by being on top of the chest and targeting it from above.