package com.lifaming.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lifaming.data.ConfigData;

/*
 * 预处理启动程序
 * 
 * */
public class FormatToKafkaRun {

	private static final Logger logger = LoggerFactory.getLogger(FormatToKafkaRun.class);

	private static Gson gson = new Gson();
	private List<ConfigData> configDataList;

	private String inputPath;
	private int timeout;
	private Pattern pattern;

	public static void main(String[] args) {
		try {
			domain(args);
		} catch (Exception e) {
			logger.error("FormatToKafkaRun error :", e);
		}
	}

	private static void domain(String[] args) throws ParseException, IOException {
		Options option;
		CommandLineParser parser;
		CommandLine command;

		option = getOption();
		parser = new PosixParser();
		command = parser.parse(option, args);
		if (!validCommand(command)) {
			logger.error("invalid command!");
			return;
		}

		FormatToKafkaRun formatToKafkaRun = new FormatToKafkaRun();

		formatToKafkaRun.init(command);
		formatToKafkaRun.run();

	}

	private void run() throws FileNotFoundException {

		File filedir = new File(inputPath);
		if (!filedir.exists() || !filedir.isDirectory()) {
			throw new FileNotFoundException();
		}

		while (true) {
			
			List<File> fileList = getFileList(filedir);
			
			//TODO
		}

	}

	private List<File> getFileList(File filedir) {
		List<File> fileList = new ArrayList<File>();
		for (File file : filedir.listFiles()) {
			if(pattern.matcher(file.getName()).matches()){
				fileList.add(file);
			}
		}
		return fileList;
	}

	private static boolean validCommand(CommandLine command) {

		if (command.getOptionValue("input-path").equals(""))
			return false;
		if (command.getOptionValue("config-path").equals(""))
			return false;

		return true;
	}

	@SuppressWarnings("static-access")
	private static Options getOption() {
		Options options = new Options();
		options.addOption(OptionBuilder.withArgName("input-path").withLongOpt("input-path").hasArg()
				.withDescription("watch input path").create());
		options.addOption(OptionBuilder.withArgName("config-path").withLongOpt("config-path").hasArg()
				.withDescription("config file path").create());
		options.addOption(OptionBuilder.withArgName("match-regexp").withLongOpt("config-path").hasArg()
				.withDescription("config file path").create());
		options.addOption(OptionBuilder.withArgName("timeout").withLongOpt("timeout").hasArg()
				.withDescription("every one file process timeout").create());
		options.addOption(null, "help", false, "show help list");
		return options;
	}

	private void init(CommandLine command) throws IOException {

		String configFile = command.getOptionValue("config-path");
		String configContext = ReadFile(configFile);
		configDataList = gson.fromJson(configContext, new TypeToken<List<ConfigData>>() {
		}.getType());

		inputPath = command.getOptionValue("input-path");

		timeout = Integer.valueOf(command.getOptionValue("timeout", "0"));
		String regexp = command.getOptionValue("match-regexp","*.bcp$");
		pattern = Pattern.compile(regexp);
	}

	/**
	 * 读取文件,返回文件内容
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public String ReadFile(String path) throws IOException {
		File file = new File(path);
		if (!file.exists() || file.isDirectory()) {
			throw new FileNotFoundException();
		}
		StringBuffer sb = new StringBuffer();
		BufferedReader bufferedReader = null;
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
			bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				sb.append(lineTxt);
			}
		} catch (Exception e) {
			logger.error("read configfile error : ", e);
		} finally {
			bufferedReader.close();
		}
		return sb.toString();
	}

}
