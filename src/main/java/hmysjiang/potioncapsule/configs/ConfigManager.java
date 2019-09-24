package hmysjiang.potioncapsule.configs;

import java.io.File;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigManager {
	
	public static final ForgeConfigSpec.Builder SConfigBuilder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SConfig;
	
	public static final ForgeConfigSpec.Builder CConfigBuilder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec CConfig;
	
	public static void loadCommonConfigFromPath(String path) {
		loadConfigFromPath(SConfig, path);
	}
	
	public static void loadClientConfigFromPath(String path) {
		loadConfigFromPath(CConfig, path);
	}
	
	private static void loadConfigFromPath(ForgeConfigSpec config, String path) {
		CommentedFileConfig fileConfig = CommentedFileConfig.builder(new File(path)).autosave().sync().writingMode(WritingMode.REPLACE).build();
		fileConfig.load();
		config.setConfig(fileConfig);
	}
	
	static {
		CommonConfigs.init(SConfigBuilder);
		ClientConfigs.init(CConfigBuilder);
		SConfig = SConfigBuilder.build();
		CConfig = CConfigBuilder.build();
	}
	
}
