package com.wenge.common.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.wenge.common.util.CommonUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

/**
 * @author HAO灏 2021/2/26 09:29
 */
public class EasyExcelUtil {

	public static <T> void export(Class<T> clazz, List<T> data, String fileName, Set<String> fields, HttpServletResponse response) throws Exception {
		setHeader(fileName, response);
		export(clazz, data, fileName, fields, null, response);
	}

	public static <T> void export(Class<T> clazz, List<T> data, String fileName, Set<String> fields, List<WriteHandler> writeHandlers, HttpServletResponse response) throws Exception {
		setHeader(fileName, response);
		ExcelWriterSheetBuilder builder = EasyExcel.write(response.getOutputStream(), clazz).sheet("sheet1")
				.includeColumnFieldNames(fields);
		builder.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy());
		if (CommonUtil.isNotEmpty(writeHandlers)) {
			writeHandlers.forEach(builder::registerWriteHandler);
		}
		builder.doWrite(data);
	}

	public static <T> void export(Class<T> clazz, List<T> data, Set<String> fields, OutputStream outputStream) {
		ExcelWriterSheetBuilder builder = EasyExcel.write(outputStream, clazz).sheet("sheet1")
				.includeColumnFieldNames(fields);
		builder.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy());
		builder.doWrite(data);
	}

	private static void setHeader(String fileName, HttpServletResponse response) throws Exception {
		fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setHeader("Content-Disposition", "attachment;filename*=" + StandardCharsets.UTF_8.name() + "''" + fileName + ".xlsx");
	}

}
