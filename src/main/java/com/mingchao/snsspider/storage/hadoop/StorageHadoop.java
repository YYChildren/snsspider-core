package com.mingchao.snsspider.storage.hadoop;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mingchao.snsspider.exception.StorageException;
import com.mingchao.snsspider.model.HadoopString;
import com.mingchao.snsspider.storage.StorageAdaptor;

public class StorageHadoop extends StorageAdaptor {

	private Map<String, PrintWriter> pwMap = new HashMap<String, PrintWriter>();

	private String basePath = "c:\\snsspider";

	public StorageHadoop() {
	}

	@Override
	public void insert(Object object) {
		PrintWriter pw = getOrCreatePrintWriter(object);
		pw.print(((HadoopString) object).toHadoopString() + "\n");
		pw.flush();
	}

	@Override
	public void insert(List<?> list) {
		for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			HadoopString object = (HadoopString) iterator.next();
			PrintWriter pw = getOrCreatePrintWriter(object);
			pw.print(((HadoopString) object).toHadoopString() + "\n");
			pw.flush();
		}
	}

	@Override
	public void insertIgnore(Object object) {
		insert(object);
	}

	@Override
	public void insertIgnore(List<?> list) {
		insert(list);
	}

	@Override
	public void insertDuplicate(Object object) {
		insert(object);
	}

	@Override
	public void insertDuplicate(List<?> list) {
		insert(list);
	}

	public synchronized void updatePwMap() {
		for (Iterator<Entry<String, PrintWriter>> iterator = pwMap.entrySet()
				.iterator(); iterator.hasNext();) {
			Entry<String, PrintWriter> pwEntry = iterator.next();
			pwEntry.getValue().flush();
			pwEntry.getValue().close();
			pwEntry.setValue(updatePrintWriter(pwEntry.getKey()));
		}
	}

	private synchronized PrintWriter getOrCreatePrintWriter(Object object) {
		String clazzName = object.getClass().getName().toLowerCase();
		int i = clazzName.length() - 1;
		for (; i >= 0; i--) {
			if (clazzName.charAt(i) == '.') {
				break;
			}
		}
		if (pwMap.containsKey(clazzName)) {
			return pwMap.get(clazzName);
		} else {
			return updatePrintWriter(clazzName);
		}
	}

	private PrintWriter updatePrintWriter(String filename) {
		try {
			File file = new File(basePath + File.separator + filename + "_"
					+ getFormatTime() + ".txt");
			file.getParentFile().mkdirs();
			if (!file.exists()) {
				file.createNewFile();
			}
			PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
					Paths.get(file.toURI()), StandardCharsets.UTF_8,
					StandardOpenOption.APPEND));
			pwMap.put(filename, pw);
			return pw;
		} catch (IOException e) {
			throw new StorageException(e);
		}
	}

	private String getFormatTime() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
		return dateFormat.format(now);
	}

}
